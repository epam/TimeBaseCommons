package deltix.util.log.gf;

import deltix.util.text.DecimalFormatter;

public class StringBuilderEntry implements AppendableEntry {
    private final StringBuilder builder;

    public StringBuilderEntry(StringBuilder builder) {
        this.builder = builder;
    }

    public StringBuilderEntry(int capacity) {
        this(new StringBuilder(capacity));
    }

    public StringBuilder getStringBuilder() {
        return builder;
    }

    @Override
    public AppendableEntry append(char c) {
        builder.append(c);
        return this;
    }

    @Override
    public AppendableEntry append(CharSequence csq) {
        builder.append(csq);
        return this;
    }

    @Override
    public AppendableEntry append(CharSequence csq, int start, int end) {
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public AppendableEntry append(boolean b) {
        builder.append(b);
        return this;
    }

    @Override
    public AppendableEntry append(int i) {
        builder.append(i);
        return this;
    }

    @Override
    public AppendableEntry append(long i) {
        builder.append(i);
        return this;
    }

    @Override
    public AppendableEntry append(double i) {
        builder.append(i);
        return this;
    }

    @Override
    public AppendableEntry append(double i, int precision) {
        builder.append(DecimalFormatter.format(i, precision));
        return this;
    }

    @Override
    public AppendableEntry append(Loggable value) {
        value.appendTo(this);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
