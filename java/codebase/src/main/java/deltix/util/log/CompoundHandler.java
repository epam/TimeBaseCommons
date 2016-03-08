package deltix.util.log;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
* Description: deltix.util.log.CompoundHandler
* Date: 8/5/11
*
* @author Nickolay Dul
*/
public class CompoundHandler extends Handler {
    private final Handler[] handlers;

    public CompoundHandler(Handler[] handlers) {
        this.handlers = handlers;
    }

    public Handler[] getHandlers() {
        return handlers.clone();
    }

    @Override
    public void setFormatter(Formatter newFormatter) throws SecurityException {
        for (int i = 0; i < handlers.length; i++)
            handlers[i].setFormatter(newFormatter);
    }

    @Override
    public void setLevel(Level newLevel) throws SecurityException {
        for (int i = 0; i < handlers.length; i++)
            handlers[i].setLevel(newLevel);
    }

    @Override
    public void publish(LogRecord record) {
        for (int i = 0; i < handlers.length; i++)
            handlers[i].publish(record);
    }

    @Override
    public void flush() {
        for (int i = 0; i < handlers.length; i++)
            handlers[i].flush();
    }

    @Override
    public void close() throws SecurityException {
        for (int i = 0; i < handlers.length; i++)
            handlers[i].close();
    }
}
