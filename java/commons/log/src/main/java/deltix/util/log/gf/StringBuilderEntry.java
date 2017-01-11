package deltix.util.log.gf;

import deltix.util.lang.Util;
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
    public AppendableEntry append(double d) {
        builder.append(d);
        return this;
    }

    @Override
    public AppendableEntry append(double d, int precision) {
        builder.append(formatDouble(d, precision));
        return this;
    }

    @Override
    public AppendableEntry append(long mantissa, int scale) {
        DecimalAppender.append(mantissa, scale, builder);
        return this;
    }

    @Override
    public AppendableEntry append(Loggable value) {
        value.appendTo(this);
        return this;
    }

    @Override
    public AppendableEntry append(Throwable e) {
        builder.append(Util.NATIVE_LINE_BREAK)
                .append(Util.printStackTrace(e))
                .append(Util.NATIVE_LINE_BREAK);

        return this;
    }

    @Override
    public AppendableEntry append(Object e) {
        append(e == null ? null : e.toString());
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    private static String formatDouble(double d, int precision) {
        if (Double.isNaN(d))
            return "NaN";
        if (Double.isInfinite(d))
            return "Infinity";
        return DecimalFormatter.format(d, precision);
    }
}
