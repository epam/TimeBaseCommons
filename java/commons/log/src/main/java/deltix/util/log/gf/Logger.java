package deltix.util.log.gf;


public interface Logger {

    LogEntry level(Level level);

    FormattedLogEntry level(Level level, String template);

    boolean isLoggable(Level level);

    void setLevel(Level level);

}
