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
    LogEntry append(Loggable value);

    @Override
    LogEntry append(Enum value);

    LogEntry append(Throwable e);

    void commit();
}

