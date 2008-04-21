package deltix.util.swing;

/**
 *
 */
public class AppExceptionHandler {
    public static AbstractApp     currentApp;
    
    public AppExceptionHandler () {
    }
    
    public void     handle (Throwable x) {
        assert currentApp != null;
        
        currentApp.handle (x);
    }
}
