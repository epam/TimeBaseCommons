package deltix.util.log.gf;

public interface AppendableEntry extends Appendable {

    @Override
    AppendableEntry append(char c);

    @Override
    AppendableEntry append(CharSequence csq);

    @Override
    AppendableEntry append(CharSequence csq, int start, int end);

    AppendableEntry append(boolean b);

    AppendableEntry append(int i);

    AppendableEntry append(long i);

    AppendableEntry append(double i);

    AppendableEntry append(double i, int precision);

    /**
     * Appends decimal. Decimal = mantissa * (10 ^ (- scale)).
     * @param mantissa - mantissa
     * @param scale - negative decimal exponent
     */
    AppendableEntry append(long mantissa, int scale);

    /**
     * Appends timestamp in format "uuuu-MM-ddTHH:mm:ss.SSS".
     * @param timestamp timestamp in ms
     */
    AppendableEntry appendTimestamp(long timestamp);

    AppendableEntry append(Loggable e);

    AppendableEntry append(Throwable e);

    AppendableEntry append(Object e);

}
