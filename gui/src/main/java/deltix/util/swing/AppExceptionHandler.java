package deltix.util.swing;

/**
 *
 */
public class AppExceptionHandler {
    public static SwingAbstractApp     currentApp;
    
    public AppExceptionHandler () {
    }
    
    public static void      staticHandle (Throwable x) {
        assert currentApp != null;
        
        currentApp.handle (x);
    }
    
    public void             handle (Throwable x) {
        staticHandle (x);
    }
}
