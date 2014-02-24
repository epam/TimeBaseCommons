package deltix.util.log.gf.impl;


import org.gflogger.Appender;
import org.gflogger.LoggerService;
import org.gflogger.appender.FileAppenderFactory;

public class DailyRollingFileAppenderFactory extends FileAppenderFactory {

    @Override
    public Appender createAppender(Class<? extends LoggerService> loggerServiceClass) {
        preinit(loggerServiceClass);
        final DailyRollingFileAppender appender = new DailyRollingFileAppender(bufferSize, multibyte);

        appender.setLogLevel(logLevel);
        appender.setLayout(layout);
        appender.setImmediateFlush(immediateFlush);
        appender.setBufferedIOThreshold(bufferedIOThreshold);
        appender.setAwaitTimeout(awaitTimeout);
        appender.setEnabled(enabled);
        appender.setIndex(index);

        appender.setFileName(fileName);
        appender.setCodepage(codepage);
        appender.setAppend(append);
        return appender;
    }

}
