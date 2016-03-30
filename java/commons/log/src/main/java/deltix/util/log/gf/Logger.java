package deltix.util.log.gf;


public interface Logger {

    LogEntry level(Level level);

    FormattedLogEntry level(Level level, String template);
    FormattedLogEntry debug(String template);
    FormattedLogEntry trace(String template);
    FormattedLogEntry info(String template);
    FormattedLogEntry warn(String template);
    FormattedLogEntry error(String template);

    boolean isLoggable(Level level);

    void setLevel(Level level);

}
