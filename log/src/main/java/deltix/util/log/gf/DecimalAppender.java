package deltix.util.log.gf;

public class DecimalAppender {

    private static final int MIN_DECIMAL_SCALE = 0;
    private static final int MAX_DECIMAL_SCALE = 18;

    private final static long[] SIZE_TABLE = {
            10L,
            100L,
            1000L,
            10000L,
            100000L,
            1000000L,
            10000000L,
            100000000L,
            1000000000L,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };

    private static final long[] MULTIPLIER_BY_SCALE = {
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000,
            1000000000,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L
    };


    private static final String[] MIN_VALUE_BY_SCALE = {
            "-9223372036854775808",
            "-922337203685477580.8",
            "-92233720368547758.08",
            "-9223372036854775.808",
            "-922337203685477.5808",
            "-92233720368547.75808",
            "-9223372036854.775808",
            "-922337203685.4775808",
            "-92233720368.54775808",
            "-9223372036.854775808",
            "-922337203.6854775808",
            "-92233720.36854775808",
            "-9223372.036854775808",
            "-922337.2036854775808",
            "-92233.72036854775808",
            "-9223.372036854775808",
            "-922.3372036854775808",
            "-92.23372036854775808",
            "-9.223372036854775808",
    };

    public static void append(long mantissa, int scale, StringBuilder builder) {
        if (scale < MIN_DECIMAL_SCALE || scale > MAX_DECIMAL_SCALE) {
            throw new IllegalArgumentException(
                    String.format("Scale %s out of range [%s, %s]", scale, MIN_DECIMAL_SCALE, MAX_DECIMAL_SCALE)
            );
        }

        if (mantissa == Long.MIN_VALUE) {
            String string = MIN_VALUE_BY_SCALE[scale];
            builder.append(string);
            return;
        }

        if (mantissa < 0) {
            mantissa = -mantissa;
            builder.append('-');
        }

        long multiplier = MULTIPLIER_BY_SCALE[scale];
        long integer = mantissa / multiplier;
        long fractional = mantissa - integer * multiplier;

        builder.append(integer);

        if (fractional > 0) {
            builder.append('.');

            while ((fractional % 10) == 0) {
                fractional /= 10;
                scale--;
            }

            int leadingZeros = scale - stringSize(fractional);

            for (int i = 0; i < leadingZeros; i++) {
                builder.append('0');
            }

            builder.append(fractional);
        }
    }

    private static int stringSize(long x) {
        for (int i = 0; i < SIZE_TABLE.length; i++) {
            if (x < SIZE_TABLE[i]) {
                return i + 1;
            }
        }

        return SIZE_TABLE.length + 1;
    }

}
