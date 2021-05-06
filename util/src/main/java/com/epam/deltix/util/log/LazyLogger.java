package com.epam.deltix.util.log;

import java.util.ArrayDeque;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logger uses lazy initialization on first log() in background thread.
 */
@Deprecated // (Alex K will remove usages from TB client)
public class LazyLogger {

    private final ArrayDeque<LogRecord> buffer = new ArrayDeque<LogRecord>(20);
    private final String name;
    private volatile Logger logger;
    private boolean started = false;

    public LazyLogger(final String name) {
        this.name = name;
    }

    /**
     * Log a message, with no arguments.
     * <p>
     * If the logger is currently enabled for the given message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   msg     The string message (or a key in the message catalog)
     */
    public void log(Level level, String msg) {
        LogRecord lr = new LogRecord(level, msg);
        doLog(lr);
    }

    /**
     * Log a message, with one object parameter.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     * <p>
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   msg     The string message (or a key in the message catalog)
     * @param   param  parameter to the message
     */
    public void log(Level level, String msg, Object param) {
        LogRecord lr = new LogRecord(level, msg);
        Object params[] = { param };
        lr.setParameters(params);

        doLog(lr);
    }

    /**
     * Log a message, with an array of object arguments.
     * <p>
     * If the logger is currently enabled for the given message
     * level then a corresponding LogRecord is created and forwarded
     * to all the registered output Handler objects.
     * <p>
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   msg     The string message (or a key in the message catalog)
     * @param   params  array of parameters to the message
     */
    public void log(Level level, String msg, Object params[]) {
        LogRecord lr = new LogRecord(level, msg);
        lr.setParameters(params);
        doLog(lr);
    }

    /**
     * Log a message, with associated Throwable information.
     * <p>
     * If the logger is currently enabled for the given message
     * level then the given arguments are stored in a LogRecord
     * which is forwarded to all registered output handlers.
     * <p>
     * Note that the thrown argument is stored in the LogRecord thrown
     * property, rather than the LogRecord parameters property.  Thus is it
     * processed specially by output Formatters and is not treated
     * as a formatting parameter to the LogRecord message property.
     * <p>
     * @param   level   One of the message level identifiers, e.g., SEVERE
     * @param   msg     The string message (or a key in the message catalog)
     * @param   thrown  Throwable associated with log message.
     */
    public void log(Level level, String msg, Throwable thrown) {
        LogRecord lr = new LogRecord(level, msg);
        lr.setThrown(thrown);
        doLog(lr);
    }


    /**
     * Log a SEVERE message.
     * <p>
     * If the logger is currently enabled for the SEVERE message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     * @param   msg     The string message (or a key in the message catalog)
     */
    public void severe(String msg) {
        log(Level.SEVERE, msg);
    }

    /**
     * Log a WARNING message.
     * <p>
     * If the logger is currently enabled for the WARNING message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     * @param   msg     The string message (or a key in the message catalog)
     */
    public void warning(String msg) {
        log(Level.WARNING, msg);
    }

    /**
     * Log an INFO message.
     * <p>
     * If the logger is currently enabled for the INFO message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     * @param   msg     The string message (or a key in the message catalog)
     */
    public void info(String msg) {
        log(Level.INFO, msg);
    }


    private void            doLog(LogRecord lr) {
        lr.setLoggerName(name);

        if (logger != null) {
            poll();
            logger.log(lr);
        } else {

            synchronized (buffer) {
                buffer.add(lr);
            }

            synchronized (this) {
                if (!started)
                    new Thread() {
                        @Override
                        public void run() {
                            init();
                        }
                    }.start();

                started = true;
            }
        }
    }

    private void            poll() {
        synchronized (buffer) {
            while (!buffer.isEmpty())
                logger.log(buffer.poll());
        }
    }

    private void            init() {
        if (logger == null)
            logger = Logger.getLogger(name);

        poll();
    }

}
