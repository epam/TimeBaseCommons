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
package com.epam.deltix.util.progress;

/**
 *  Estimnates time remaining in a lengthy process. Methods are thread-safe.
 */
public class Estimator {
    private double      mWorkDone = 0;
    private double      mTotalWork = 0;
    private double      mWorkDoneAtBaseTime = 0;
    private long        mBaseTime = System.currentTimeMillis ();
    
    public Estimator () {        
    }
    
    public synchronized void         setTotalWork (double v) {
        if (v <= 0)
            throw new IllegalArgumentException ("totalWork == " + v + " <= 0");
        
        mTotalWork = v;
    }
    
    public synchronized double       getTotalWork () {
        return (mTotalWork);
    }
        
    public synchronized void         setWorkDone (double workDone) {
        if (workDone < 0)
            throw new IllegalArgumentException ("workDone == " + workDone + " < 0");
        
        if (workDone > mTotalWork)
            throw new IllegalArgumentException ("workDone == " + workDone + " > totalWork == " + mTotalWork);
        
        mWorkDone = workDone;
    }

    public synchronized double       getWorkDone () {
        return (mWorkDone);
    }
    
    public synchronized boolean      isWorkCompleted () {
        return (mWorkDone == mTotalWork);
    }
    
    /**
     *  Reset time estimation
     */
    public synchronized void         setWorkDoneAndResetTime (double workDone) {
        setWorkDone (workDone);
        mWorkDoneAtBaseTime = mWorkDone;
        mBaseTime = System.currentTimeMillis ();
    }
    
    public synchronized double       getFractionDone () {
        return (mWorkDone / mTotalWork);
    }
    
    /**
     *  Return currently estimated rate of work.
     *
     *  @return     Rate in units of work per second. If no work has been
     *              done, return 0. If elapsed time is 0, return NaN.
     */
    public synchronized double       getEstimatedRate () {   
        return ((mWorkDone - mWorkDoneAtBaseTime) / getTimeSinceLastReset ());
    }
    
    /**
     *  Return the elapsed time since last reset.
     *
     *  @return     Time in seconds.
     */
    public synchronized double       getTimeSinceLastReset () {   
        return ((System.currentTimeMillis () - mBaseTime) * 0.001);
    }
    
    public synchronized double       getWorkRemaining () {
        return (mTotalWork - mWorkDone);
    }
    
    /**
     *  Return estimated remaining time. This is equivalent to 
     *  <code>getWorkRemaining () * getEstimatedRate ()</code>.
     *
     *  @return     Estimated remaining time, in seconds. If no work has been
     *              done, return NaN. If elapsed time is 0, return NaN.
     */    
    public synchronized double       getTimeRemaining () {
        return (getWorkRemaining () / getEstimatedRate ());
    }   

    public synchronized void        incrementWorkDone (double inc) {
        setWorkDone (mWorkDone + inc);
    }
}