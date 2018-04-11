package deltix.util.log.gf;

@Deprecated
public interface Logger {

    void setLevel(Level level);

    Level getLevel();

    boolean isLoggable(Level level);

    /**
     * Will be deprecated soon.
     * Use deltix.util.log.gf.Logger#log(deltix.util.log.gf.Level) instead.
     */
    // @Deprecated
    LogEntry level(Level level);

    LogEntry log(Level level);

    LogEntry trace();

    LogEntry debug();

    LogEntry info();

    LogEntry warn();

    LogEntry error();

    LogEntry fatal();

    FormattedLogEntry log(Level level, String template);

    FormattedLogEntry trace(String template);

    FormattedLogEntry debug(String template);

    FormattedLogEntry info(String template);

    FormattedLogEntry warn(String template);

    FormattedLogEntry error(String template);

    FormattedLogEntry fatal(String template);

}
