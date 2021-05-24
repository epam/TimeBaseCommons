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
package com.epam.deltix.util.concurrent;

import com.epam.deltix.util.lang.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 *  Limits the frequency of an action by delaying it by a specified period.
 */
public abstract class FrequencyLimiter {
    private final Timer                 timer;
    private TimerTask                   task = null;

    public FrequencyLimiter (Timer timer) {
        this.timer = timer;
    }

    /**
     *  Override to define reaction to exceptions thrown from {@link #run}.
     *  Default implementation logs with Severe level.
     *
     *  @param x    The exception thrown.
     */
    protected void                      onError (Throwable x) {
        Util.logException(this + " failed", x);
    }

    /**
     *  Override to define action to perform.
     */
    protected abstract void             run () throws Exception;

    /**
     *  Override to define action delay (in ms). Default implementation returns
     *  1000 (= 1s).
     *
     *  @return  Action delay in milliseconds.
     */
    protected long                      getDelay () {
        return (1000);
    }

    public void                         execute () {
        try {
            run ();
        } catch (Throwable x) {
            onError (x);
        } finally {
            synchronized (this) {
                task = null;
            }
        }
    }

    /**
     *  Arms this action, if it's not yet armed.
     */
    public synchronized void            arm () {
        if (task != null)
            return;

        task =
            new TimerTask () {
                @Override
                public void             run () {
                    execute ();
                }
            };

        timer.schedule (task, getDelay ());
    }

    /**
     *  Disarms this action, if it's armed.
     */
    public synchronized boolean         disarm () {
        if (task == null)
            return (false);

        task.cancel ();
        task = null;
        return (true);
    }   
}