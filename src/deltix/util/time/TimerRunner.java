package deltix.util.time;

/**
 * Date: Mar 8, 2010
 */
public abstract class TimerRunner extends java.util.TimerTask {
    
     @Override
     public void run() {
        try {
            runInternal();
        }
        catch (Throwable e) {
            try {
                onError(e);
            } catch (Throwable ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    void onError(Throwable e) {
        e.printStackTrace(System.out);
    }

    protected abstract void runInternal();
}
