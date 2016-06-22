package deltix.util.log.gf.impl;

import deltix.util.log.gf.FormattedLogEntry;
import deltix.util.log.gf.Loggable;


final class FormattedGFLogEntry implements FormattedLogEntry {

    private org.gflogger.FormattedGFLogEntry entry;

    void setEntry(org.gflogger.FormattedGFLogEntry entry) {
        this.entry = entry;
    }

    @Override
    public FormattedLogEntry with(char value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(CharSequence value, int start, int end) {
        entry.with(value, start, end);
        return this;
    }

    @Override
    public FormattedLogEntry with(boolean value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(int value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(long value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(double value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(double value, int precision) {
        entry.with(value, precision);
        return this;
    }

    @Override
    public FormattedLogEntry with(Loggable value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(Enum value) {
        entry.with(value);
        return this;
    }

    @Override
    public FormattedLogEntry with(Throwable e) {
        entry.with(e);
        return this;
    }

    @Override
    public void withLast(char value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(CharSequence value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(CharSequence value, int start, int end) {
        entry.withLast(value, start, end);
    }

    @Override
    public void withLast(boolean value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(int value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(long value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(double value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(double value, int precision) {
        entry.withLast(value, precision);
    }

    @Override
    public void withLast(Loggable value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(Enum value) {
        entry.withLast(value);
    }

    @Override
    public void withLast(Throwable e) {
        entry.withLast(e);
    }

}
