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
package com.epam.deltix.qsrv.hf.pub;

import com.epam.deltix.util.time.GMT;

/**
 * @author Andy
 *         Date: Jan 14, 2010 4:20:52 PM
 */
public final class TimeRange implements TimeInterval {

    public static final long UNDEFINED = Long.MIN_VALUE;

    public final long from;
    public final long to;

    public TimeRange() {
       this.from = UNDEFINED;
       this.to = UNDEFINED;
   }

    public TimeRange(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public TimeRange(long [] range) {
        this.from = range[0];
        this.to = range [1];
    }

    public TimeRange(long from) {
        this.from = from;
        this.to = UNDEFINED;
    }

    @Override
    public String toString() {
        String f = (from == UNDEFINED) ? "*" : GMT.formatDateTime(from);
        String t = (to == UNDEFINED) ? "*" : GMT.formatDateTime(to);

        return f + '-' + t;
    }

    public boolean isUndefined() {
        return (from == UNDEFINED || to == UNDEFINED);
    }

//    public static TimeRange parse(String timeRangeProp) {
//        return null;  //To change body of created methods use File | Settings | File Templates.
//    }

    public static TimeRange union(TimeRange r1, TimeRange r2) {
        if (r1 == null)
            return r2;
        if (r2 == null)
            return r1;

        long from, to;
        if (r1.from == UNDEFINED)
            from = r2.from;
        else
            if (r2.from != UNDEFINED)
                from = Math.min(r1.from, r2.from);
            else
                from = r1.from;

        if (r1.to == UNDEFINED)
            to = r2.to;
        else
            if (r2.to != UNDEFINED)
                to = Math.max(r1.to, r2.to);
            else
                to = r1.to;

        return new TimeRange (from, to);
    }

    @Override
    public long getFromTime() {
        return from;
    }

    @Override
    public long getToTime() {
        return to;
    }
}