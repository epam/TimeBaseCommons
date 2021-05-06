package com.epam.deltix.util.concurrent;

/**
 *
 */
public class UncheckedInterruptedException extends RuntimeException {
    public UncheckedInterruptedException () {
    }   
    
    public UncheckedInterruptedException (Throwable ix) {
        super (ix);
    }           
    
    public static void      uncheckedWait (Object obj) {
        try {
            obj.wait ();
        } catch (InterruptedException x) {
            throw new UncheckedInterruptedException (x);
        }
    }
}
