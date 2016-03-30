package deltix.util.log.gf;


public abstract class AbstractLogger implements Logger {

    @Override
    public LogEntry level(Level level) {
        LogEntry result = NullLogEntry.getInstance();

        if (isLoggable(level)) {
            try {
                result = log(level);
            } catch (Exception e) {
                ErrorManager.error(e);
            }
        }

        return result;
    }

    @Override
    public FormattedLogEntry level(Level level, String template) {
        FormattedLogEntry result = NullLogEntry.getInstance();

        if (isLoggable(level)) {
            try {
                result = log(level, template);
            } catch (Exception e) {
                ErrorManager.error(e);
            }
        }

        return result;
    }

    @Override
    public FormattedLogEntry debug(String template) {
        return level(Level.DEBUG, template);
    }

    @Override
    public FormattedLogEntry trace(String template) {
        return level(Level.TRACE, template);
    }

    @Override
    public FormattedLogEntry info(String template) {
        return level(Level.INFO, template);
    }

    @Override
    public FormattedLogEntry warn(String template) {
        return level(Level.WARN, template);
    }

    @Override
    public FormattedLogEntry error(String template) {
        return level(Level.ERROR, template);
    }

    protected abstract LogEntry log(Level level);

    protected abstract FormattedLogEntry log(Level level, String template);

}
