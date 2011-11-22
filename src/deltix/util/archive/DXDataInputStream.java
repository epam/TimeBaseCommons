package deltix.util.archive;

import deltix.util.io.RandomAccessFileToInputStreamAdapter;
import deltix.util.lang.Util;
import deltix.util.memory.MemoryDataInput;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                entry.entry = new DXDataEntry(header.readLong(), header.readString());
                entry.size = entry.entry.size;
            } else {
                eof = true;
                entry = null;
            }
        } catch (EOFException e) {
            eof = true;
            entry = null;
        }

        return entry;
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
