/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.qsrv.util.archive;

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.memory.DataExchangeUtils;
import com.epam.deltix.util.memory.MemoryDataOutput;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DXDataOutputStream extends ArchiveOutputStream {

    public static final int         ENTRY_HEADER_SIZE = 512;
    public static final int         HEADER_SIZE = 1024;

    public static final byte []     MAGIC = { 'd', 'x', 'm', 's', 'g' };
    public static final byte        VERSION = 1;
    public static final byte[]      header = new byte[HEADER_SIZE];

    private static final class DataEntry {
        
        private DataEntry(DXDataEntry entry) {
            this.entry = entry;
        }

        /**
         * Current data entry.
         */
        private final DXDataEntry entry;
        
        /**
         * Start offset in file.
         */
        private long offset = 0;

        /**
         * Data offset in file.
         */
        private long dataOffset = 0;
        
        /**
         * Whether write() has been called at all.
         *
         * <p>In order to create a valid archive {@link
         * #closeArchiveEntry closeArchiveEntry} will write an empty
         * array to get the CRC right if nothing has been written to
         * the stream at all.</p>
         */
        private boolean hasWritten;
    }
    
    private final RandomAccessFile          raf;
    private final List<DXDataEntry>         entries = new LinkedList<DXDataEntry>();
    private final Map<DXDataEntry, Long>    offsets = new HashMap<DXDataEntry, Long>();
    private DataEntry                       current;
    private boolean                         finished = false;

    // Number of written bytes
    private long                            written;

    /**
     * Creates a new DXDataOutputStream writing to a File.  Will use random access if possible.
     * @param file the archive
     * @throws IOException on error
     */
    public DXDataOutputStream (File file) throws IOException {
        (raf = new RandomAccessFile(file, "rw")).setLength(0);

        System.arraycopy(MAGIC, 0, header, 0, MAGIC.length);
        DataExchangeUtils.writeByte(header, MAGIC.length, VERSION);
        writeOut(header, 0, header.length);
    }

    public void putHeaderEntry(DXHeaderEntry entry) throws IOException {
        Long offset = offsets.get(entry);
        
        if (offset != null) {
            raf.seek(offset + ENTRY_HEADER_SIZE);
            raf.write(entry.data);
            
            raf.seek(written); // rollback to current position
        } else {
            putArchiveEntry(entry);
        }
    }
        
    @Override
    public void putArchiveEntry(ArchiveEntry entry) throws IOException {
        assertFinished();

        if (current != null)
            closeArchiveEntry();

        current = new DataEntry((DXDataEntry) entry);
        entries.add(current.entry);

        writeEntryHeader(current);
        
        if (entry instanceof DXHeaderEntry)
            write(((DXHeaderEntry)entry).data);
    }

    private void writeEntryHeader(DataEntry e) throws IOException {
        offsets.put(e.entry, written);
        e.offset = written;
        
        MemoryDataOutput out = new MemoryDataOutput(ENTRY_HEADER_SIZE);
        out.writeByte(e.entry instanceof DXHeaderEntry ? 1 : 0);
        out.writeLong(e.entry.getSize()); // entry length
        out.writeString(e.entry.name); // entry name

        if (out.getSize() > ENTRY_HEADER_SIZE)
            throw new IllegalArgumentException("Entry name is too long");

        writeOut(out.getBuffer(), 0, ENTRY_HEADER_SIZE);
        e.dataOffset = written;
    }

    private void            assertFinished() throws IOException {
        if (finished)
            throw new IOException("Stream has already been finished");
    }

    @Override
    public void             closeArchiveEntry() throws IOException {
        assertFinished();

        if (current == null)
            throw new IOException("No current entry to close");

        long size = written - current.offset - ENTRY_HEADER_SIZE;
        
        // update entry header
        raf.seek(current.offset + 1);
        raf.writeLong(size);
        raf.seek(written); // rollback to current position
        
        current.entry.size = size;
        current = null;
    }

     /**
     * Write bytes to output or random access file.
     * @param data the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     */
    protected final void    writeOut(byte[] data, int offset, int length) throws IOException {
        if (raf != null) {
            raf.write(data, offset, length);
            written += length;
            count(length);
        } else {
            throw new IllegalStateException("Output stream is not initialized");
        }
    }

    @Override
    public void             write(int b) throws IOException {
        if (raf != null) {
            raf.write(b);
            written += 1;
            count(1);
        } else {
            throw new IllegalStateException("Output stream is not initialized");
        }
    }

    @Override
    public void             write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void             write(byte[] b, int offset, int length) throws IOException {
        current.hasWritten = true;
        writeOut(b, offset, length);
    }

    @Override
    public void             finish() throws IOException {
        assertFinished();

        if (current != null)
            closeArchiveEntry();

        flush();
        
        offsets.clear();
        entries.clear();
        finished = true;
    }

    // return bytes written
    public long             count() {
        return written;
    }

    @Override
    public void             flush() throws IOException {
        raf.getChannel().force(true);
    }

    @Override
    public void             close() throws IOException {
        if (!finished)
            finish();
                
        Util.close(raf);
    }

    @Override
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
        return new DXDataEntry(entryName);
    }
}