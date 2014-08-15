package deltix.util.log.gf.jul;

import java.util.logging.Level;
import java.util.logging.Logger;

import deltix.util.lang.Util;
import deltix.util.log.gf.AppendableEntry;
import deltix.util.log.gf.LogEntry;
import deltix.util.log.gf.Loggable;
import deltix.util.text.DecimalFormatter;

final class JULLogEntry implements LogEntry {

    private final StringBuilder messageBuilder = new StringBuilder(256);

    private Logger logger;
    private Level level;
    private Throwable exception;
    private boolean committed = true; // for first check

    @Override
    public LogEntry append(char c) {
        if (checkNotCommitted())
            messageBuilder.append(c);

        return this;
    }

    @Override
    public LogEntry append(CharSequence csq) {
        if (checkNotCommitted())
            messageBuilder.append(csq);

        return this;
    }

    @Override
    public LogEntry append(CharSequence csq, int start, int end) {
        if (checkNotCommitted())
            messageBuilder.append(csq, start, end);

        return this;
    }

    @Override
    public LogEntry append(boolean b) {
        if (checkNotCommitted())
            messageBuilder.append(b);

        return this;
    }

    @Override
    public LogEntry append(int i) {
        if (checkNotCommitted())
            messageBuilder.append(i);

        return this;
    }

    @Override
    public LogEntry append(long i) {
        if (checkNotCommitted())
            messageBuilder.append(i);

        return this;
    }

    @Override
    public LogEntry append(double i) {
        if (checkNotCommitted())
            messageBuilder.append(i);

        return this;
    }

    @Override
    public LogEntry append(double i, int precision) {
        if (checkNotCommitted())
            messageBuilder.append(DecimalFormatter.format(i, precision));

        return this;
    }

    @Override
    public LogEntry append(Loggable value) {
        if (checkNotCommitted())
            value.appendTo(this);

        return this;
    }

    @Override
    public AppendableEntry append(Enum value) {
        if (checkNotCommitted())
            messageBuilder.append(value.name());

        return this;
    }

    @Override
    public LogEntry append(Throwable e) {
        if (checkNotCommitted()) {
            if (exception != null) {
                messageBuilder
                        .append(Util.NATIVE_LINE_BREAK)
                        .append(Util.printStackTrace(exception))
                        .append(Util.NATIVE_LINE_BREAK);
            }

            exception = e;
        }

        return this;
    }

    @Override
    public void commit() {
        if (checkNotCommitted()) {
            logger.log(level, messageBuilder.toString(), exception);
            committed = true;
        }
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    void setLevel(Level level) {
        this.level = level;
    }

    void clear() {
        logger = null;
        level = null;
        exception = null;
        committed = false;
        messageBuilder.delete(0, messageBuilder.length());
    }

    boolean isCommitted() {
        return committed;
    }

    private boolean checkNotCommitted() {
        if (committed)
            System.err.println("JUL log entry is already committed");

        return !committed;
    }

    @Override
    public String toString() {
        return messageBuilder.toString();
    }

}
