package deltix.util.log.gf;


public interface FormattedLogEntry {

    FormattedLogEntry with(char value);

    FormattedLogEntry with(CharSequence value);

    FormattedLogEntry with(CharSequence value, int start, int end);

    FormattedLogEntry with(boolean value);

    FormattedLogEntry with(int value);

    FormattedLogEntry with(long value);

    FormattedLogEntry with(double value);

    FormattedLogEntry with(double value, int precision);

    FormattedLogEntry with(Loggable value);

    FormattedLogEntry with(Enum value);

    FormattedLogEntry with(Throwable e);


    FormattedLogEntry withLast(char value);

    FormattedLogEntry withLast(CharSequence value);

    FormattedLogEntry withLast(CharSequence value, int start, int end);

    FormattedLogEntry withLast(boolean value);

    FormattedLogEntry withLast(int value);

    FormattedLogEntry withLast(long value);

    FormattedLogEntry withLast(double value);

    FormattedLogEntry withLast(double value, int precision);

    FormattedLogEntry withLast(Loggable value);

    FormattedLogEntry withLast(Enum value);

    FormattedLogEntry withLast(Throwable e);

}
