package deltix.util.log.gf;


public class NullLogEntry implements LogEntry, FormattedLogEntry {

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
    public LogEntry append(Enum value) {
        return this;
    }

    @Override
    public void commit() {
        // skip
    }

    @Override
    public FormattedLogEntry with(char value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value, int start, int end) {
        return this;
    }

    @Override
    public FormattedLogEntry with(boolean value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(int value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(long value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(double value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(double value, int precision) {
        return this;
    }

    @Override
    public FormattedLogEntry with(Loggable value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(Enum value) {
        return this;
    }

    @Override
    public FormattedLogEntry with(Throwable e) {
        return this;
    }

    @Override
    public void withLast(char value) {
    }

    @Override
    public void withLast(CharSequence value) {
    }

    @Override
    public void withLast(CharSequence value, int start, int end) {
    }

    @Override
    public void withLast(boolean value) {
    }

    @Override
    public void withLast(int value) {
    }

    @Override
    public void withLast(long value) {
    }

    @Override
    public void withLast(double value) {
    }

    @Override
    public void withLast(double value, int precision) {
    }

    @Override
    public void withLast(Loggable value) {
    }

    @Override
    public void withLast(Enum value) {
    }

    @Override
    public void withLast(Throwable e) {
    }

}
