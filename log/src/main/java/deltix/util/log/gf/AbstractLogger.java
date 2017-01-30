package deltix.util.log.gf;


public abstract class AbstractLogger implements Logger {

    @Override
    public LogEntry trace() {
        return log(Level.TRACE);
    }

    @Override
    public LogEntry debug() {
        return log(Level.DEBUG);
    }

    @Override
    public LogEntry info() {
        return log(Level.INFO);
    }

    @Override
    public LogEntry warn() {
        return log(Level.WARN);
    }

    @Override
    public LogEntry error() {
        return log(Level.ERROR);
    }

    @Override
    public LogEntry fatal() {
        return log(Level.FATAL);
    }

    @Override
    public FormattedLogEntry trace(String template) {
        return log(Level.TRACE, template);
    }

    @Override
    public FormattedLogEntry debug(String template) {
        return log(Level.DEBUG, template);
    }

    @Override
    public FormattedLogEntry info(String template) {
        return log(Level.INFO, template);
    }

    @Override
    public FormattedLogEntry warn(String template) {
        return log(Level.WARN, template);
    }

    @Override
    public FormattedLogEntry error(String template) {
        return log(Level.ERROR, template);
    }

    @Override
    public FormattedLogEntry fatal(String template) {
        return log(Level.FATAL, template);
    }

    @Override
    public final LogEntry level(Level level) {
        return log(level);
    }

    protected final LogEntry log(Level level) {
        return isLoggable(level) ? logEntry(level) : NullLogEntry.getInstance();
    }

    protected final FormattedLogEntry log(Level level, String template) {
        return isLoggable(level) ? logEntry(level, template) : NullLogEntry.getInstance();
    }

    protected abstract LogEntry logEntry(Level level);

    protected abstract FormattedLogEntry logEntry(Level level, String template);

}
