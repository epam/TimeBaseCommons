package deltix.util.log.gf;


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

    void appendLast(Loggable e);

    void appendLast(Throwable e);

    void appendLast(Object o);


    void commit();
}

