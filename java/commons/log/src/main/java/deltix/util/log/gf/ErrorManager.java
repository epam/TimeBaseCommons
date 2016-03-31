package deltix.util.log.gf;

import deltix.util.lang.Util;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class ErrorManager {

    private ErrorManager() {
    }

    private static AtomicBoolean reported = new AtomicBoolean();

    public static void error(Exception exception) {
        if (!reported.getAndSet(true)) {
            StringBuilder builder = new StringBuilder(1024);
            builder.append(ErrorManager.class.getName());
            String message = exception.getMessage();
            if (message != null)
                builder.append(": ").append(message);

            builder.append(Util.NATIVE_LINE_BREAK);
            for (StackTraceElement element : exception.getStackTrace())
                builder.append('\t').append(element.toString()).append(Util.NATIVE_LINE_BREAK);

            System.err.println(builder.toString());
        }
    }

}
