package deltix.util.log.gf;


public interface LogEntry {

    LogEntry append(char c);

    LogEntry append(CharSequence csq);

    LogEntry append(CharSequence csq, int start, int end);

    LogEntry append(boolean b);

    LogEntry append(int i);

    LogEntry append(long i);

    LogEntry append(double i);

    LogEntry append(double i, int precision);

    LogEntry append(Throwable e);

    void commit();
}

