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

    AppendableEntry append(Loggable value);

    AppendableEntry append(Enum value);


}
