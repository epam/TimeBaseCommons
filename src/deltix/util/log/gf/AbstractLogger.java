package deltix.util.log.gf;


public abstract class AbstractLogger implements Logger {

    @Override
    public LogEntry level(Level level) {
        LogEntry result;

        try {
            result = log(level);
        } catch (Exception e) {
            ErrorManager.error(e);
            result = NullLogEntry.getInstance();
        }

        return result;
    }

    protected abstract LogEntry log(Level level);
}
