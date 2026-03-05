package deltix.util.jide;


public class JideAppExceptionHandler {
    public static JideAbstractApp currentApp;

    public JideAppExceptionHandler() {
    }

    public void handle(Throwable x) {
        assert currentApp != null;

        currentApp.handle(x);
    }
}
