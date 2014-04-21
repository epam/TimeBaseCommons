package deltix.util.log.gf.impl;

import org.gflogger.Appender;
import org.gflogger.LoggerService;
import org.gflogger.appender.AbstractAppenderFactory;

public class ConsoleAppenderFactory extends AbstractAppenderFactory {

    @Override
    public Appender createAppender(Class<? extends LoggerService> loggerServiceClass) {
        preinit(loggerServiceClass);

        ConsoleAppender appender = new ConsoleAppender(bufferSize, multibyte);

        appender.setLogLevel(logLevel);
        appender.setLayout(layout);
        appender.setImmediateFlush(immediateFlush);
        appender.setBufferedIOThreshold(bufferedIOThreshold);
        appender.setAwaitTimeout(awaitTimeout);
        appender.setEnabled(enabled);
        appender.setIndex(index);

        return appender;
    }

}
