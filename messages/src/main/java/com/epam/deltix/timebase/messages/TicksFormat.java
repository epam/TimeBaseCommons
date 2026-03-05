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

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class TicksFormat {
    private static final int                NANOS_PER_MS = 1000000;
    private static final StringBuffer       sb = new StringBuffer();
    private static final Calendar           calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private static final FastDateFormat     TICKS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.", TimeZone.getTimeZone("GMT"));
    private static final FastDateFormat     MS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getTimeZone("GMT"));
    private static final DecimalFormat      DF = new DecimalFormat("000000000");

    public static synchronized String         format(long milliseconds, int nanosComponent) {
        sb.setLength(0);
        calendar.setTimeInMillis(milliseconds);

        if (nanosComponent == 0 || milliseconds == Long.MIN_VALUE) {
            MS.format(calendar, sb);
        } else {
            TICKS.format(calendar, sb);

            long ms = milliseconds % 1000;
            DF.format(ms * NANOS_PER_MS + nanosComponent, sb, new FieldPosition(0));
        }

        return sb.toString();
    }

}