package deltix.util.concurrent;

/**
 *
 */
public class UncheckedInterruptedException extends RuntimeException {
    public UncheckedInterruptedException () {
    }   
    
    public UncheckedInterruptedException (InterruptedException ix) {
        super (ix);
    }        
}
