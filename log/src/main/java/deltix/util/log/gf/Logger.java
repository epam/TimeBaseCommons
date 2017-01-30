package deltix.util.log.gf;


public interface Logger {

    void setLevel(Level level);

    Level getLevel();

    boolean isLoggable(Level level);

    LogEntry level(Level level);

    LogEntry trace();

    LogEntry debug();

    LogEntry info();

    LogEntry warn();

    LogEntry error();

    LogEntry fatal();

    FormattedLogEntry trace(String template);

    FormattedLogEntry debug(String template);

    FormattedLogEntry info(String template);

    FormattedLogEntry warn(String template);

    FormattedLogEntry error(String template);

    FormattedLogEntry fatal(String template);

}
