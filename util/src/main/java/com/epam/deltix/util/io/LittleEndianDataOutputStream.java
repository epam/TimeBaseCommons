package com.epam.deltix.util.io;

import java.io.*;

/**
 * Little-Endian implementation of {@link DataOutput} interface
 */
public class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {

    /**
     * bytearr is initialized on demand by writeUTF
     */
    private byte[] bytearr = null;

    /**
     * constructor
     */
    public LittleEndianDataOutputStream(OutputStream out) {
        super(out);
        this.d = new DataOutputStream(out);
        w = new byte[8]; // work array for composing output
    }

    // L I T T L E   E N D I A N   W R I T E R S
    // Little endian methods for multi-byte numeric types.
    // Big-endian do fine for single-byte types and strings.

    /**
     * like DataOutputStream.writeShort.
     * also acts as a writeUnsignedShort
     */
    public final void writeShort(int v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        d.write(w, 0, 2);
    }

    /**
     * like DataOutputStream.writeChar.
     * Note the parm is an int even though this as a writeChar
     */
    public final void writeChar(int v) throws IOException {
        // same code as writeShort
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        d.write(w, 0, 2);
    }

    /**
     * like DataOutputStream.writeInt.
     */
    public final void writeInt(int v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        w[2] = (byte) (v >> 16);
        w[3] = (byte) (v >> 24);
        d.write(w, 0, 4);
    }

    /**
     * like DataOutputStream.writeLong.
     */
    public final void writeLong(long v) throws IOException {
        w[0] = (byte) v;
        w[1] = (byte) (v >> 8);
        w[2] = (byte) (v >> 16);
        w[3] = (byte) (v >> 24);
        w[4] = (byte) (v >> 32);
        w[5] = (byte) (v >> 40);
        w[6] = (byte) (v >> 48);
        w[7] = (byte) (v >> 56);
        d.write(w, 0, 8);
    }

    /**
     * like DataOutputStream.writeFloat.
     */
    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    /**
     * like DataOutputStream.writeDouble.
     */
    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    /**
     * like DataOutputStream.writeChars, flip each char.
     */
    public final void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            writeChar(s.charAt(i));
        }
    } // end writeChars

    // p u r e l y   w r a p p e r   m e t h o d s
    // We cannot inherit since DataOutputStream is final.

    /* This method writes only one byte, even though it says int */

    public final synchronized void write(int b) throws IOException {
        d.write(b);
    }

    public final synchronized void write(byte b[], int off, int len)
            throws IOException {
        d.write(b, off, len);
    }

    public void flush() throws IOException {
        d.flush();
    }

    /* Only writes one byte */
    public final void writeBoolean(boolean v) throws IOException {
        d.writeBoolean(v);
    }

    public final void writeByte(int v) throws IOException {
        d.writeByte(v);
    }

    public final void writeBytes(String s) throws IOException {
        d.writeBytes(s);
    }

    public final void writeUTF(String str) throws IOException {
        writeUTF(str, this);
    }

    static int writeUTF(String str, DataOutput out) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                    "encoded string too long: " + utflen + " bytes");

        byte[] bytearr = null;
        if (out instanceof LittleEndianDataOutputStream) {
            LittleEndianDataOutputStream dos = (LittleEndianDataOutputStream) out;
            if (dos.bytearr == null || (dos.bytearr.length < (utflen + 2)))
                dos.bytearr = new byte[(utflen * 2) + 2];
            bytearr = dos.bytearr;
        } else {
            bytearr = new byte[utflen + 2];
        }

        bytearr[count++] = (byte) ((utflen) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F))) break;
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;

            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen + 2);
        return utflen + 2;
    }

    public final int size() {
        return d.size();
    }

    public final void write(byte b[]) throws IOException {
        d.write(b, 0, b.length);
    }

    public final void close() throws IOException {
        d.close();
    }

    // i n s t a n c e   v a r i a b l e s

    protected DataOutputStream d; // to get at high level write methods of DataOutputStream
    byte w[]; // work array for composing output

} 

