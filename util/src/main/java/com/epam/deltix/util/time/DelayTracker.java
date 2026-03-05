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
package com.epam.deltix.util.time;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.gflog.api.LogLevel;

/**
 *  Logs a message if an activity takes longer than specified time.
 *  This class is very efficient and imposes completely negligible overhead.
 *  It uses {@link TimeKeeper} for time tracking, and does not even call
 *  System.currentTimeMillis. Instances of this class are obviously
 *  not thread-aware. Usage:
 *<pre>
//One-time preparation:
DelayTracker   dr = new DelayTracker (1000, "Some activity", MyLogger, Level.WARNING);
...
//Now, to report delays in performing an activity of some sort:
dr.in ();
... perform activity ...
dr.out ();
</pre>
*/
public final class DelayTracker {
    private final long          delayThreshold;
    private final String        activityName;
    private final Log logger;
    private final LogLevel level;
    private long                timeIn = 0;

    public DelayTracker (long delayThreshold, String activityName) {
        this (delayThreshold, activityName, LogFactory.getLog(DelayTracker.class), LogLevel.INFO);
    }

    public DelayTracker (long delayThreshold, String activityName, Log logger, LogLevel level) {
        this.delayThreshold = delayThreshold;
        this.activityName = activityName;
        this.logger = logger;
        this.level = level;
    }

    public void                 in () {
        timeIn = System.nanoTime ();
    }

    public void                 out () {
        if (timeIn == 0) {
            logger.warn().append("Out of sequence call: out () not called after in ()").commit();
            return;
        }

        long    delay = System.nanoTime () - timeIn;

        if (delay >= delayThreshold && logger.isEnabled (level))
            logger.log (level).append(activityName).append(" took ").append(delay * 1E-9).append(" s").commit();

        timeIn = 0;
    }
}