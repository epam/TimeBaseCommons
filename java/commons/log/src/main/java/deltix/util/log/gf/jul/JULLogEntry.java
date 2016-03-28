package deltix.util.log.gf.jul;

import deltix.util.lang.Util;
import deltix.util.log.gf.FormattedLogEntry;
import deltix.util.log.gf.LogEntry;
import deltix.util.log.gf.Loggable;
import deltix.util.text.DecimalFormatter;

import java.util.logging.Level;
import java.util.logging.Logger;


final class JULLogEntry implements LogEntry, FormattedLogEntry {

    private final StringBuilder builder = new StringBuilder(1024);

    private boolean committed = true;

    private Logger logger;
    private Level level;

    private String template;
    private int index;

    private Throwable exception;

    @Override
    public LogEntry append(char c) {
        checkNotCommitted();
        builder.append(c);
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq) {
        checkNotCommitted();
        builder.append(csq);
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq, int start, int end) {
        checkNotCommitted();
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public LogEntry append(boolean b) {
        checkNotCommitted();
        builder.append(b);
        return this;
    }

    @Override
    public LogEntry append(int i) {
        checkNotCommitted();
        builder.append(i);
        return this;
    }

    @Override
    public LogEntry append(long i) {
        checkNotCommitted();
        builder.append(i);
        return this;
    }

    @Override
    public LogEntry append(double d) {
        checkNotCommitted();
        builder.append(d);
        return this;
    }

    @Override
    public LogEntry append(double d, int precision) {
        checkNotCommitted();
        builder.append(formatDouble(d, precision));
        return this;
    }

    @Override
    public LogEntry append(Loggable value) {
        checkNotCommitted();

        if (value == null)
            builder.append((CharSequence) null);
        else
            value.appendTo(this);

        return this;
    }

    @Override
    public LogEntry append(Enum value) {
        checkNotCommitted();
        builder.append(value != null ? value.name() : null);
        return this;
    }

    @Override
    public LogEntry append(Throwable e) {
        checkNotCommitted();

        if (exception != null) {
            builder
                    .append(Util.NATIVE_LINE_BREAK)
                    .append(Util.printStackTrace(exception))
                    .append(Util.NATIVE_LINE_BREAK);
        }

        exception = e;

        return this;
    }

    @Override
    public void commit() {
        checkNotCommitted();

        logger.log(level, builder.toString(), exception);

        logger = null;
        level = null;
        template = null;
        index = 0;
        exception = null;
        builder.delete(0, builder.length());
        committed = true;
    }

    @Override
    public FormattedLogEntry with(char value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value, int start, int end) {
        appendChunk();
        append(value, start, end);
        return this;
    }

    @Override
    public FormattedLogEntry with(boolean value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(int value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(long value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(double value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(double value, int precision) {
        appendChunk();
        append(value, precision);
        return this;
    }

    @Override
    public FormattedLogEntry with(Loggable value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(Enum value) {
        appendChunk();
        append(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(Throwable e) {
        appendChunk();
        append(e);
        return this;
    }

    @Override
    public FormattedLogEntry withLast(char value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(CharSequence value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(CharSequence value, int start, int end) {
        with(value, start, end);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(boolean value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(int value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(long value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(double value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(double value, int precision) {
        with(value, precision);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(Loggable value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(Enum value) {
        with(value);
        appendLastChunk();
        return this;
    }

    @Override
    public FormattedLogEntry withLast(Throwable e) {
        with(e);
        appendLastChunk();
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    void setTemplate(String template) {
        this.template = template;
    }

    void setLogger(Logger logger) {
        this.logger = logger;
    }

    void setLevel(Level level) {
        this.level = level;
    }

    void setCommitted(boolean committed) {
        this.committed = committed;
    }

    boolean isCommitted() {
        return committed;
    }

    private void checkNotCommitted() {
        if (committed)
            throw new IllegalStateException("JUL log entry is already committed");
    }

    private void appendChunk() {
        int i = template.indexOf("%s", index);
        if (i == -1)
            throw new IllegalArgumentException("Too many parameters for template " + template);

        builder.append(template, index, i);
        index = i + 2;
    }

    private void appendLastChunk() {
        int i = template.indexOf("%s", index);
        if (i != -1)
            throw new IllegalArgumentException("Too few parameters for template " + template);

        builder.append(template, index, template.length());
        index = template.length();
    }

    private static String formatDouble(double d, int precision) {
        if (Double.isNaN(d))
            return "NaN";
        if (Double.isInfinite(d))
            return d < 0 ? "-Infinity" : "Infinity";

        return DecimalFormatter.format(d, precision);
    }

}
