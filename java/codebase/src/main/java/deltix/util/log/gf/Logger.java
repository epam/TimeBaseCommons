package deltix.util.log.gf;


public interface Logger {

    LogEntry level(Level level);

    boolean isLoggable(Level level);

    void setLevel(Level level);

}
