package com.epam.deltix.util.lang;

/**
 * This class contains several useful methods to dump buffer content (in hex codes).
 */
public class Dump {

    /**
     * dump byte [] as StringBuilder  "0x00, ..., 0xFF"
     */
    public static StringBuilder dump(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("null bytes");
        return dump (bytes, 0, bytes.length);
    }

    public static StringBuilder dump(byte[] bytes, int offset, int length) {
        if (bytes == null) throw new IllegalArgumentException("null bytes");

        StringBuilder sbuf = new StringBuilder();
        int i, cnt = offset + length;
        for (i = offset; i < cnt; i++) {
            if (i > offset)
                sbuf.append(", ");
            sbuf.append(Integer.toHexString(((int) bytes[i]) & 0xFF));
        }
        return sbuf;
    }

    /**
     * dump char [] as StringBuilder  "0x00, ..., 0xFF"
     */
    public static StringBuilder dump(char[] chars) {
        if (chars == null) throw new IllegalArgumentException("null chars");
        StringBuilder sbuf = new StringBuilder();
        int i, cnt = chars.length;
        for (i = 0; i < cnt; i++) {
            if (i > 0)
                sbuf.append(", ");
            sbuf.append(Integer.toHexString(chars[i]));
        }
        return sbuf;
    }

    /**
     * Dumps byte[] buffer as table:
     * <pre>
     * 00000000:  64 74 64 3d 66 69 6c 65 25 33 41 44 25 33 41 25  [dtd=file%3AD%3A%]
     * 00000010:  32 46 70 72 6f 6a 65 63 74 73 25 32 46 77 70 63  [2Fprojects%2Fwpc]
     * 00000020:  25 32 46 65 61 70 70 73 25 32 46 72 65 73 6f 75  [%2Feapps%2Fresou]
     * </pre>
     */
    public static StringBuilder table(byte[] bytes) {
        if (bytes == null)
            return null;

        return table(bytes, 0, bytes.length);
    }

    public static StringBuilder table(byte[] bytes, int offset, int length) {
        if (bytes == null)
            return null;

        if (bytes.length < length)
            length = bytes.length;


        final int COLS_IN_ROW = 0x10;
        int cnt = length+offset;
        StringBuilder sbuf = new StringBuilder((8 + 3 + 16 * 3 + 2 + 16 + 1) * (cnt / COLS_IN_ROW));
        char[] row = new char[COLS_IN_ROW];
        for (int rowbase = offset; rowbase < cnt; rowbase += COLS_IN_ROW) {
            dump(sbuf, rowbase, 8);
            sbuf.append(":  ");
            for (int col = 0; col < COLS_IN_ROW; col++) {
                int pos = rowbase + col;
                if (pos < cnt) {
                    byte bt = bytes[pos];
                    //dump (sbuf, bt, 2);
                    sbuf.append(Character.forDigit((bt >> 4) & 0x0F, 0x10));
                    sbuf.append(Character.forDigit(bt & 0x0F, 0x10));
                    row[col] = (char) bt;
                } else { // fill blanks
                    sbuf.append("  ");
                    row[col] = 0x20; //space
                }
                sbuf.append(' ');
            }
            sbuf.append(" [");
            for (char c : row) {
                if (c < 32 || c > 126)
                    c = '.';
                sbuf.append (c);
            }
            sbuf.append("]\n");
        }
        return sbuf;
    }

    /**
     * Dumps char[] buffer as table:
     * <pre>
     * 00000000:  0064 0074 0064 003d 0066 0069 006c 0065 0025 0033 0041 0044 0025 0033 0041 0025  [dtd=file%3AD%3A%]
     * 00000010:  0032 0046 0070 0072 006f 006a 0065 0063 0074 0073 0025 0032 0046 0077 0070 0063  [2Fprojects%2Fwpc]
     * 00000020:  0025 0032 0046 0065 0061 0070 0070 0073 0025 0032 0046 0072 0065 0073 006f 0075  [%2Feapps%2Fresou]
     * </pre>
     */
    public static StringBuilder table(char[] chars) {
        if (chars == null)
            return null;

        return table(chars, 0, chars.length);
    }

    public static StringBuilder table(char[] chars, int offset, int length) {
        if (chars == null)
            return null;

        if (chars.length < length)
            length = chars.length;

        final int COLS_IN_ROW = 0x10;
        int cnt = length + offset;
        StringBuilder sbuf = new StringBuilder((8 + 3 + 16 * 5 + 2 + 16 + 1) * (cnt / COLS_IN_ROW));
        char[] row = new char[COLS_IN_ROW];
        for (int rowbase = offset; rowbase < cnt; rowbase += COLS_IN_ROW) {
            dump(sbuf, rowbase, 8);
            sbuf.append(":  ");
            for (int col = 0; col < COLS_IN_ROW; col++) {
                int pos = rowbase + col;
                if (pos < cnt) {
                    char ch = chars[pos];
                    dump(sbuf, ch, 4);
                    row[col] = ch;
                } else { // fill blanks
                    sbuf.append("    ");
                    row[col] = 0x20; //space
                }
                sbuf.append(' ');
            }
            sbuf.append(" [");
            sbuf.append(row);
            sbuf.append("]\n");
        }
        return sbuf;
    }

    /**
     * Function converts char[] =&gt; byte []. Characters &gt; 0xFF are illegal!
     */
    public static byte[] c2b(char[] chars) {
        int i, cnt = chars.length;
        byte[] result = new byte[cnt];
        for (i = 0; i < cnt; i++) {
            int ch = chars[i];
            if ((ch & 0xFF00) != 0)
                throw new IllegalArgumentException("Dump.c2b() error: buffer contains non-ACSII character: " + Integer.toHexString(ch));
            result[i] = (byte) ch;
        }
        return result;
    }


    private static void dump(StringBuilder sbuf, int number, int nDigits) {
        char[] buf = new char[nDigits];
        for (int i = nDigits; i > 0;) {
            buf[--i] = Character.forDigit(number & 0xF, 0x10);
            number = number >> 4;
        }
        sbuf.append(buf);
    }

}
