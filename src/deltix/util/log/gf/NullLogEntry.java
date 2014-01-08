package deltix.util.log.gf;


public class NullLogEntry implements LogEntry {

    public static NullLogEntry getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final NullLogEntry INSTANCE = new NullLogEntry();
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
    public void commit() {
        // skip
    }
}
