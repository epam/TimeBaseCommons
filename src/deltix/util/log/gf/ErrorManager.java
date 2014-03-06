package deltix.util.log.gf;


import deltix.util.lang.Util;

public abstract class ErrorManager {

    private ErrorManager() {
    }

    private static boolean reported = false;

    // print once
    public static void error(Exception exception) {
        if (reported)
            return;

        synchronized (ErrorManager.class) {
            if (reported)
                return;

            reported = true;
        }

        StringBuilder exceptionText = new StringBuilder(ErrorManager.class.getName());
        if (exception.getMessage() != null)
            exceptionText.append(": ").append(exception.getMessage());

        exceptionText.append(Util.NATIVE_LINE_BREAK);

        for (StackTraceElement element : exception.getStackTrace())
            exceptionText.append('\t').append(element.toString()).append(Util.NATIVE_LINE_BREAK);

        System.err.println(exceptionText.toString());
    }

}
