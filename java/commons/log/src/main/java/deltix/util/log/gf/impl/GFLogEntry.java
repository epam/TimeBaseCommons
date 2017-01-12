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
    public LogEntry append(long mantissa, int scale) {
        entry.append(mantissa, scale);
        return this;
    }

    @Override
    public LogEntry appendTimestamp(long timestamp) {
        entry.appendTimestamp(timestamp);
        return this;
    }

    @Override
    public LogEntry append(Loggable e) {
        if(e == null)
            append((CharSequence) null);
        else
            e.appendTo(this);

        return this;
    }

    @Override
    public LogEntry append(Throwable e) {
        entry.append(e);
        return this;
    }

    @Override
    public LogEntry append(Object o) {
        entry.append(o);
        return this;
    }

    @Override
    public void appendLast(char c) {
        entry.appendLast(c);
    }

    @Override
    public void appendLast(CharSequence csq) {
        entry.appendLast(csq);
    }

    @Override
    public void appendLast(CharSequence csq, int start, int end) {
        entry.appendLast(csq, start, end);
    }

    @Override
    public void appendLast(boolean b) {
        entry.appendLast(b);
    }

    @Override
    public void appendLast(int i) {
        entry.appendLast(i);
    }

    @Override
    public void appendLast(long i) {
        entry.appendLast(i);
    }

    @Override
    public void appendLast(double i) {
        entry.appendLast(i);
    }

    @Override
    public void appendLast(double i, int precision) {
        entry.appendLast(i, precision);
    }

    @Override
    public void appendLast(long mantissa, int scale) {
        entry.appendLast(mantissa, scale);
    }

    @Override
    public void appendTimestampLast(long timestamp) {
        entry.appendTimestampLast(timestamp);
    }

    @Override
    public void appendLast(Loggable e) {
        append(e).commit();
    }

    @Override
    public void appendLast(Throwable e) {
        entry.appendLast(e);
    }

    @Override
    public void appendLast(Object o) {
        entry.appendLast(o);
    }

    @Override
    public void commit() {
        entry.commit();
    }

}