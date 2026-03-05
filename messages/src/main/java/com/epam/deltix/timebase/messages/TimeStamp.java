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
package com.epam.deltix.timebase.messages;


public class TimeStamp implements TimeStampedMessage {

    /** Number of nanos per millisecond (10^6) */
    public static final int     NANOS_PER_MS = 1000000;
	
	/** Maximum value of milliseconds convertible to nanoseconds */
	private static final long   MS_MAX_VALUE = Long.MAX_VALUE / NANOS_PER_MS;

    /** Time is measured in milliseconds that passed since January 1, 1970 UTC */
    //@Title ("Time")
    //@FieldType("TIMESTAMP")
    public long                 timestamp = TIMESTAMP_UNKNOWN;

    /** Nanoseconds part of the TimeStamp#timestamp */
    //@Title ("Nanoseconds Component")
    public int                  nanosComponent = 0;


    public static TimeStamp     fromMilliseconds(long timestamp) {
        TimeStamp ts = new TimeStamp();
        ts.setTime(timestamp);
        return ts;
    }

    public static TimeStamp     fromNanoseconds(long nanoseconds) {
        TimeStamp ts = new TimeStamp();
        ts.setNanoTime(nanoseconds);
        return ts;
    }

    public void                 setNanoTime(long nanoSeconds) {
        if (nanoSeconds != TIMESTAMP_UNKNOWN && nanoSeconds != Long.MAX_VALUE) {
            nanosComponent = (int) (nanoSeconds % TimeStamp.NANOS_PER_MS);
            timestamp = nanoSeconds / TimeStamp.NANOS_PER_MS;
        } else {
            nanosComponent = 0;
            timestamp = nanoSeconds;
        }
    }

    public static int                  getNanosComponent(long nstime) {
        return (int) (nstime % TimeStamp.NANOS_PER_MS);
    }

    /*
     *  Returns number of nanoseconds that passed since January 1, 1970 UTC
     */
    @Override
    public long                 getNanoTime() {
        return timestamp == TIMESTAMP_UNKNOWN || timestamp == Long.MAX_VALUE ? timestamp : (getNanoTime(timestamp) + nanosComponent);
    }

    /**
     * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this message.
     */
    @Override
    public long                 getTimeStampMs() {
        return getTime();
    }

    public void                 setTime(long milliseconds) {
        timestamp = milliseconds;
        nanosComponent = 0;
    }
    
    public long                 getTime() {
        return timestamp + (nanosComponent > 0 ? 1 : 0);
    }

    public boolean              isUndefined() {
        return isUndefined(timestamp);
    }

    public static boolean       isUndefined(long timestamp) {
        return timestamp == TIMESTAMP_UNKNOWN;
    }

    public void                 setUndefined() {
        setTime(TIMESTAMP_UNKNOWN);
    }

    public static long          getMilliseconds (long nanoSeconds) {
        return nanoSeconds != TIMESTAMP_UNKNOWN && nanoSeconds != Long.MAX_VALUE ? nanoSeconds / TimeStamp.NANOS_PER_MS : nanoSeconds;
    }

    public static long          getNanoTime (long milliseconds) {
        if (milliseconds == TIMESTAMP_UNKNOWN) 
			return Long.MIN_VALUE;
		
		if (milliseconds >= MS_MAX_VALUE)
            return Long.MAX_VALUE;

        return milliseconds * NANOS_PER_MS;
    }

    public static long          getNanoTime (long milliseconds, int nanosComponent) {
        return milliseconds == TIMESTAMP_UNKNOWN || milliseconds == Long.MAX_VALUE ? milliseconds : (getNanoTime(milliseconds) + nanosComponent);
    }
}