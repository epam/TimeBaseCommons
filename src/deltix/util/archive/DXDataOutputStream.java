package deltix.util.archive;

import deltix.util.lang.Util;
import deltix.util.memory.MemoryDataOutput;
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

//        /**
//         * Offset for CRC entry in the local file header data for the
//         * current entry starts here.
//         */
//
//        private long localDataStart = 0;
        
        /**
         * Start offset in file.
         */
        private long offset = 0;

        /**
         * Data offset in file.
         */
        private long dataOffset = 0;
        
        /**
         * Number of bytes read for the current entry (can't rely on
         * Deflater#getBytesRead) when using DEFLATED.
         */
        private long bytesRead = 0;
        
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
     * @param file the file to zip to
     * @throws IOException on error
     */
    public DXDataOutputStream (File file) throws IOException {
        (raf = new RandomAccessFile(file, "rw")).setLength(0);
        
        writeOut(MAGIC);
        write(VERSION);
        writeOut(header, MAGIC.length + 1, header.length - (MAGIC.length + 1));
    }

    public void putHeaderEntry(DXHeaderEntry entry) throws IOException {
        Long offset = offsets.get(entry);
        
        if (offset != null) {
            raf.seek(offset);
            raf.writeLong(entry.size);
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
        out.writeLong(-1L); // entry length
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
        raf.seek(current.offset);
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

    protected final void    writeOut(byte[] data) throws IOException {
        writeOut(data, 0, data.length);
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
        
        offsets.clear();
        entries.clear();
        finished = true;
    }

    @Override
    public void             close() throws IOException {
        if (!finished)
            finish();
        
        raf.getChannel().force(true);
        Util.close(raf);
    }

    @Override
    public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
        return new DXDataEntry(entryName);
    }
}
