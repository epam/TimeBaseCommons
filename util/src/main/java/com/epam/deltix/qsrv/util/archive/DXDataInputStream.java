package com.epam.deltix.qsrv.util.archive;

import com.epam.deltix.util.io.RandomAccessFileToInputStreamAdapter;
import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.memory.MemoryDataInput;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class DXDataInputStream extends ArchiveInputStream {

     private static final class DataEntry {

        /**
         * Start offset in file.
         */
        private long offset = 0;

        /**
         * Start offset in file.
         */
        private long size = 0;

        /**
         * Number of bytes of entry content read by the client.
         */
        private long bytesRead;

        /**
         * Current data entry.
         */
        private DXDataEntry entry;

        long getNextOffset() {
            return offset + size + DXDataOutputStream.ENTRY_HEADER_SIZE;
        }
     }

    private final RandomAccessFile                  raf;
    private RandomAccessFileToInputStreamAdapter    in;
    private final List<DataEntry>                   entries = new LinkedList<DataEntry>();
    private DataEntry                               current;
    private boolean                                 eof = false;
    private final MemoryDataInput                   header =
            new MemoryDataInput(DXDataOutputStream.ENTRY_HEADER_SIZE);

    public DXDataInputStream (File file) throws IOException {
        raf = new RandomAccessFile(file, "r");
        in = new RandomAccessFileToInputStreamAdapter(raf);
        
        readHeader();
    }

    @Override
    public ArchiveEntry     getNextEntry() throws IOException {
        DataEntry dataEntry = current = advance();
        
        return dataEntry != null ? dataEntry.entry : null;
    }

    private DataEntry    advance() throws IOException {
        if (eof)
            return null;

        DataEntry entry = new DataEntry();
        
        try {
            if (current != null) {
                if (current.bytesRead < current.size)
                    in.skip(current.size - current.bytesRead);
                else if (current.bytesRead > current.size)
                    in.seek(current.getNextOffset());
            } else {
                in.seek(DXDataOutputStream.HEADER_SIZE);
            }

            entry.offset = in.getPosition();
            
            if (in.read(header.getBytes()) > 0) {
                header.setBytes(header.getBytes());

                byte type = header.readByte();
                entry.size = header.readLong();
                if (entry.size != -1) {
                    String name = header.readString();

                    if (type == 0) {
                        entry.entry = new DXDataEntry(entry.size, name);
                    } else if (type == 1) {
                        byte[] data = new byte[(int) entry.size];
                        if (in.read(data) == entry.size)
                            entry.entry = new DXHeaderEntry(name, data);
                        
                        entry.bytesRead += entry.size;
                    }

                } else {
                    entry = null;
                }
            } else {
                entry = null;
            }

        } catch (EOFException e) {
            entry = null;
        }
        
        eof = (entry == null);

        return entry;
    }

    public  int             getVersion() {
        return 0;
    }

    private void            readHeader() {
        
    }

    @Override
    public int read(byte[] buffer, int start, int length) throws IOException {
        int count = in.read(buffer, start, length);
        current.bytesRead += count;
        count(count);
        
        return count;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        Util.close(raf);
    }
}
