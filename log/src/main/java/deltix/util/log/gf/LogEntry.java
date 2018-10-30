package deltix.util.log.gf;

@Deprecated
public interface LogEntry extends AppendableEntry {

    @Override
    LogEntry append(char c);

    @Override
    LogEntry append(CharSequence csq);

    @Override
    LogEntry append(CharSequence csq, int start, int end);

    @Override
    LogEntry append(boolean b);

    @Override
    LogEntry append(int i);

    @Override
    LogEntry append(long i);

    @Override
    LogEntry append(double i);

    @Override
    LogEntry append(double i, int precision);

    @Override
    LogEntry append(long mantissa, int scale);

    @Override
    LogEntry appendTimestamp(long timestamp);

    @Override
    LogEntry append(Loggable e);

    @Override
    LogEntry append(Throwable e);

    @Override
    LogEntry append(Object o);


    void appendLast(char c);

    void appendLast(CharSequence csq);

    void appendLast(CharSequence csq, int start, int end);

    void appendLast(boolean b);

    void appendLast(int i);

    void appendLast(long i);

    void appendLast(double i);

    void appendLast(double i, int precision);

    /**
     * Appends decimal. Decimal = mantissa * (10 ^ (- scale)).
     * @param mantissa - mantissa
     * @param scale - negative decimal exponent
     */
    void appendLast(long mantissa, int scale);

    /**
     * Appends timestamp in format "uuuu-MM-ddTHH:mm:ss.SSS" and commits entry.
     * @param timestamp timestamp in ms
     */
    void appendTimestampLast(long timestamp);

    void appendLast(Loggable e);

    void appendLast(Throwable e);

    void appendLast(Object o);


    void commit();
}

