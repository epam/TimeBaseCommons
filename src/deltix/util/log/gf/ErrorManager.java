package deltix.util.log.gf;


public abstract class ErrorManager {

    private ErrorManager() {
    }

    private static final String TEXT = ErrorManager.class.getName();

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

        String text = TEXT;
        String msg = exception.getMessage();
        if (msg != null)
            text = text + ": " + msg;

        System.err.println(text);
        exception.printStackTrace();
    }
}
