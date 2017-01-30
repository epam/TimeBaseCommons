package deltix.util.log.gf;


public interface FormattedLogEntry {

    FormattedLogEntry with(char value);

    FormattedLogEntry with(CharSequence value);

    FormattedLogEntry with(CharSequence value, int start, int end);

    FormattedLogEntry with(boolean value);

    FormattedLogEntry with(int value);

    FormattedLogEntry with(long value);

    FormattedLogEntry with(double value);

    FormattedLogEntry with(double value, int precision);

    /**
     * Appends decimal. Decimal = mantissa * (10 ^ (- scale)).
     * @param mantissa - mantissa
     * @param scale - negative decimal exponent
     */
    FormattedLogEntry with(long mantissa, int scale);

    /**
     * Appends timestamp in format "uuuu-MM-ddTHH:mm:ss.SSS".
     * @param timestamp timestamp in ms
     */
    FormattedLogEntry withTimestamp(long timestamp);

    FormattedLogEntry with(Loggable value);

    FormattedLogEntry with(Throwable e);

    FormattedLogEntry with(Object o);


    void withLast(char value);

    void withLast(CharSequence value);

    void withLast(CharSequence value, int start, int end);

    void withLast(boolean value);

    void withLast(int value);

    void withLast(long value);

    void withLast(double value);

    void withLast(double value, int precision);

    /**
     * Appends decimal. Decimal = mantissa * (10 ^ (- scale)) and commits entry.
     * @param mantissa - mantissa
     * @param scale - negative decimal exponent
     */
    void withLast(long mantissa, int scale);

    /**
     * Appends timestamp in format "uuuu-MM-ddTHH:mm:ss.SSS" and commits entry.
     * @param timestamp timestamp in ms
     */
    void withTimestampLast(long timestamp);

    void withLast(Loggable value);

    void withLast(Throwable e);

    void withLast(Object o);

}
