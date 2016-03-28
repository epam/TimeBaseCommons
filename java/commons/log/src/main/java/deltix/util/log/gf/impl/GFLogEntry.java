package deltix.util.log.gf.impl;

import deltix.util.log.gf.LogEntry;
import deltix.util.log.gf.Loggable;


final class GFLogEntry implements LogEntry {

    private org.gflogger.GFLogEntry entry;

    void setEntry(org.gflogger.GFLogEntry entry) {
        this.entry = entry;
    }

    @Override
    public LogEntry append(char c) {
        entry.append(c);
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq) {
        entry.append(csq);
        return this;
    }

    @Override
    public LogEntry append(CharSequence csq, int start, int end) {
        entry.append(csq, start, end);
        return this;
    }

    @Override
    public LogEntry append(boolean b) {
        entry.append(b);
        return this;
    }

    @Override
    public LogEntry append(int i) {
        entry.append(i);
        return this;
    }

    @Override
    public LogEntry append(long i) {
        entry.append(i);
        return this;
    }

    @Override
    public LogEntry append(double i) {
        entry.append(i);
        return this;
    }

    @Override
    public LogEntry append(double i, int precision) {
        entry.append(i, precision);
        return this;
    }

    @Override
    public LogEntry append(Loggable value) {
        if(value == null)
            append((CharSequence) null);
        else
            value.appendTo(this);

        return this;
    }

    @Override
    public LogEntry append(Enum value) {
        entry.append(value != null ? value.name() : null);
        return this;
    }

    @Override
    public LogEntry append(Throwable e) {
        entry.append(e);
        return this;
    }

    @Override
    public void commit() {
        entry.commit();
    }
}