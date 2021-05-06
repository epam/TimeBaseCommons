package com.epam.deltix.util.io;

import java.io.*;

/**
 * Little-Endian implementation of {@link DataInput} interface
 */
public class LittleEndianDataInputStream extends FilterInputStream implements DataInput {

    /**
     * to get at the big-Endian methods of a basic DataInputStream
     */
    protected final DataInputStream dis;

    /**
     * to get at the a basic readBytes method.
     */
    protected final InputStream is;

    /**
     * work array for buffering input.
     */
    protected final byte[] work;

    /**
     * working arrays initialized on demand by readUTF
     */
    private byte bytearr[] = new byte[80];
    private char chararr[] = new char[80];


    /**
     * constructor.
     *
     * @param in binary inputstream of little-endian data.
     */
    public LittleEndianDataInputStream(InputStream in) {
        super(in);
        this.is = in;
        this.dis = new DataInputStream(in);
        work = new byte[8];
    }

    /**
     * close.
     *
     * @throws IOException if close fails.
     */
    @Override
    public final void close() throws IOException {
        dis.close();
    }

    /**
     * Read bytes. Watch out, read may return fewer bytes than requested.
     *
     * @param ba  where the bytes go.
     * @param off offset in buffer, not offset in file.
     * @param len count of bytes to read.
     * @return how many bytes read.
     * @throws IOException if read fails.
     */
    @Override
    public final int read(byte ba[], int off, int len) throws IOException {
        // For efficiency, we avoid one layer of wrapper
        return is.read(ba, off, len);
    }

    /**
     * read only a one-byte boolean.
     *
     * @return true or false.
     * @throws IOException if read fails.
     * @see java.io.DataInput#readBoolean()
     */
    public final boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    /**
     * read byte.
     *
     * @return the byte read.
     * @throws IOException if read fails.
     * @see java.io.DataInput#readByte()
     */
    public final byte readByte() throws IOException {
        return dis.readByte();
    }

    /**
     * Read on char. like DataInputStream.readChar except little endian.
     *
     * @return little endian 16-bit unicode char from the stream.
     * @throws IOException if read fails.
     */
    public final char readChar() throws IOException {
        dis.readFully(work, 0, 2);
        return (char) ((work[1] & 0xff) << 8 | (work[0] & 0xff));
    }

    /**
     * Read a double. like DataInputStream.readDouble except little endian.
     *
     * @return little endian IEEE double from the datastream.
     * @throws IOException
     */
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Read one float. Like DataInputStream.readFloat except little endian.
     *
     * @return little endian IEEE float from the datastream.
     * @throws IOException if read fails.
     */
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Read bytes until the array is filled.
     *
     * @see java.io.DataInput#readFully(byte[])
     */
    public final void readFully(byte ba[]) throws IOException {
        dis.readFully(ba, 0, ba.length);
    }

    /**
     * Read bytes until the count is satisfied.
     *
     * @throws IOException if read fails.
     * @see java.io.DataInput#readFully(byte[],int,int)
     */
    public final void readFully(byte ba[],
                                int off,
                                int len) throws IOException {
        dis.readFully(ba, off, len);
    }

    /**
     * Read an int, 32-bits. Like DataInputStream.readInt except little endian.
     *
     * @return little-endian binary int from the datastream
     * @throws IOException if read fails.
     */
    public final int readInt() throws IOException {
        dis.readFully(work, 0, 4);
        return (work[3]) << 24
                | (work[2] & 0xff) << 16
                | (work[1] & 0xff) << 8
                | (work[0] & 0xff);
    }

    /**
     * Read a line.
     *
     * @return a rough approximation of the 8-bit stream as a 16-bit unicode string
     * @throws IOException
     * @deprecated This method does not properly convert bytes to characters. Use a Reader instead with a little-endian
     *             encoding.
     */
    public final String readLine() throws IOException {
        return dis.readLine();
    }

    /**
     * read a long, 64-bits.  Like DataInputStream.readLong except little endian.
     *
     * @return little-endian binary long from the datastream.
     * @throws IOException
     */
    public final long readLong() throws IOException {
        dis.readFully(work, 0, 8);
        return (long) (work[7]) << 56
                |
                /* long cast needed or shift done modulo 32 */
                (long) (work[6] & 0xff) << 48
                | (long) (work[5] & 0xff) << 40
                | (long) (work[4] & 0xff) << 32
                | (long) (work[3] & 0xff) << 24
                | (long) (work[2] & 0xff) << 16
                | (long) (work[1] & 0xff) << 8
                | (long) (work[0] & 0xff);
    }

    /**
     * Read short, 16-bits. Like DataInputStream.readShort except little endian.
     *
     * @return little endian binary short from stream.
     * @throws IOException if read fails.
     */
    public final short readShort() throws IOException {
        dis.readFully(work, 0, 2);
        return (short) ((work[1] & 0xff) << 8 | (work[0] & 0xff));
    }

    /**
     * Read UTF counted string.
     *
     * @return String read.
     */
    public final String readUTF() throws IOException {
        return readUTF(this);
    }

    /**
     * Note. This is a STATIC method!
     *
     * @param in stream to read UTF chars from (endian irrelevant)
     * @return string from stream
     * @throws IOException if read fails.
     */
    public final static String readUTF(DataInput in) throws IOException {
        int utflen = in.readUnsignedShort();
        byte[] bytearr = null;
        char[] chararr = null;
        if (in instanceof LittleEndianDataInputStream) {
            LittleEndianDataInputStream dis = (LittleEndianDataInputStream) in;
            if (dis.bytearr.length < utflen) {
                dis.bytearr = new byte[utflen * 2];
                dis.chararr = new char[utflen * 2];
            }
            chararr = dis.chararr;
            bytearr = dis.bytearr;
        } else {
            bytearr = new byte[utflen];
            chararr = new char[utflen];
        }

        int c, char2, char3;
        int count = 0;
        int chararr_count = 0;

        in.readFully(bytearr, 0, utflen);

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++] = (char) c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                                "malformed input: partial character at end");
                    char2 = (int) bytearr[count - 1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                                "malformed input around byte " + count);
                    chararr[chararr_count++] = (char) (((c & 0x1F) << 6) |
                            (char2 & 0x3F));
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                                "malformed input: partial character at end");
                    char2 = (int) bytearr[count - 2];
                    char3 = (int) bytearr[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                                "malformed input around byte " + (count - 1));
                    chararr[chararr_count++] = (char) (((c & 0x0F) << 12) |
                            ((char2 & 0x3F) << 6) |
                            ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                            "malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
    }

    /**
     * Read an unsigned byte. Note: returns an int, even though says Byte (non-Javadoc)
     *
     * @throws IOException if read fails.
     * @see java.io.DataInput#readUnsignedByte()
     */
    public final int readUnsignedByte() throws IOException {
        return dis.readUnsignedByte();
    }

    /**
     * Read an unsigned short, 16 bits. Like DataInputStream.readUnsignedShort except little endian. Note, returns int
     * even though it reads a short.
     *
     * @return little-endian int from the stream.
     * @throws java.io.IOException if read fails.
     */
    public final int readUnsignedShort() throws IOException {
        dis.readFully(work, 0, 2);
        return ((work[1] & 0xff) << 8 | (work[0] & 0xff));
    }

    /**
     * Skip over bytes in the stream. See the general contract of the <code>skipBytes</code> method of
     * <code>DataInput</code>.
     *
     * Bytes for this operation are read from the contained input stream.
     *
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws java.io.IOException if an I/O error occurs.
     */
    public final int skipBytes(int n) throws IOException {
        return dis.skipBytes(n);
    }
}

