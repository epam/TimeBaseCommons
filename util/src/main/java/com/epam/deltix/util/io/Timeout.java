/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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