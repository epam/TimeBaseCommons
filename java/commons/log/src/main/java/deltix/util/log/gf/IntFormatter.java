package deltix.util.log.gf;

class IntFormatter {


    protected static void format4DigitUInt(int value, StringBuilder builder) {
        formatNDigitUShort(value, 4, builder);
    }

    protected static void format3DigitUInt(int value, StringBuilder builder) {
        formatNDigitUShort(value, 3, builder);
    }

    protected static void format2DigitUInt(int value, StringBuilder builder) {
        formatNDigitUShort(value, 2, builder);
    }

    /**
     * Div is replaced by inverse mul (52429 / 524288 = 0.100000381...).
     * It works for value < 8 * 10 ^ 4 that covers all shorts.
     */
    private static void formatNDigitUShort(int value, int digits, StringBuilder builder) {
        int offset = builder.length();
        int index = offset + digits;

        builder.setLength(index);

        do {
            int integer = (value * 52429) >>> (16 + 3);
            int remainder = value - ((integer << 3) + (integer << 1));
            builder.setCharAt(--index, (char) (remainder + '0'));
            value = integer;
        } while (index > offset);
    }

}
