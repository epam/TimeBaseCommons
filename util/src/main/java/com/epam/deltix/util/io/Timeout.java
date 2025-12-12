package com.epam.deltix.util.io;

/**
 *	Class that will execute an action after a specified
 *	period of time.
 */
public class Timeout implements Runnable {
    private Runnable	mAction;
    private long		mTimeout;
    private Thread		mThread = null;
    
    /**
     *	Constructs a new instance of the timer. The timer is
     *	not armed on construction.
     *
     *	@param action		The Runnable to run after the timeout.
     *	@param timeout		The timeout in milliseconds.
     */
    public Timeout (Runnable action, long timeout) {
    	mAction = action;
    	mTimeout = timeout;
    }
    
    /**
     *	Constructs a new instance of the timer. The timer is
     *	not armed on construction.
     *
     *	@param timeout		The timeout in milliseconds.
     */
    public Timeout (long timeout) {
    	mAction = this;
    	mTimeout = timeout;
    }
    
    private void		executeAction () {
    	mAction.run ();
    }
    
    /**
     *	Runs after the timeout. Override to determine behavior.
     *	By default, this method throws a RuntimeException.
     */
    public void			run () {
    	throw (new RuntimeException ());
    }
    
    /**
     *	Arms the timeout.
     */
    public void			arm () {    	
    	mThread = 
            new Thread () {
    		public void 	run () {
                    try {
                            sleep (mTimeout);
                            executeAction ();
                    } catch (InterruptedException x) {
                    }
    		}
            };
    	
    	mThread.start ();
    }
    
    /**
     *	Disarms the timeout.
     */
    public void			disarm () {    	
    	mThread.interrupt ();
    	mThread = null;
    }
}
