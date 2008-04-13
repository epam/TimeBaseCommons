package deltix.util.swing;

/**
 *
 */
public class AppExceptionHandler {
    public static AbstractApp     currentApp;
    
    public AppExceptionHandler () {
        System.out.println ("AAA");
        System.exit (1);
    }
    
    public void     handle (Throwable x) {
        currentApp.handle (x);
    }
}
