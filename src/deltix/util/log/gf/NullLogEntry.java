package deltix.util.log.gf;


public class NullLogEntry implements LogEntry {

    private static final NullLogEntry INSTANCE = new NullLogEntry();

    public static NullLogEntry getInstance() {
        return INSTANCE;
    }

    private NullLogEntry() {
    }

    @Override
    public LogEntry append(char c) {
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq) {
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq, int start, int end) {
        return this;
    }

    @Override
    public LogEntry append(boolean b) {
        return this;
    }

    @Override
    public LogEntry append(int i) {
        return this;
    }

    @Override
    public LogEntry append(long i) {
        return this;
    }

    @Override
    public LogEntry append(double i) {
        return this;
    }

    @Override
    public LogEntry append(double i, int precision) {
        return this;
    }

    @Override
    public LogEntry append(Throwable e) {
        return this;
    }

    @Override
    public LogEntry append(Loggable value) {
        return this;
    }

    @Override
    public AppendableEntry append(Enum value) {
        return this;
    }

    @Override
    public void commit() {
        // skip
    }
}
