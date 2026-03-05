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

package com.epam.deltix.util.log;

import com.epam.deltix.util.time.GlobalTimer;
import com.epam.deltix.util.LangUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Used by TerseFormatter to log date. Usage:
 * <pre>
 * CurrentMonthDate cmd = CurrentMonthDate.getInstance();
 * String date = cmd.getDayMonth(timestamp);
 * </pre>
 * @author Andy
*/
public class CurrentMonthDate extends TimerTask {
    private final static long MILLISECONDS_IN_DAY = TimeUnit.DAYS.toMillis(1);

    private final String [] MONTH_CODES = new SimpleDateFormat().getDateFormatSymbols().getShortMonths();
    private final Calendar c = Calendar.getInstance();

    private volatile String currentMonthDay;
    private volatile long goodUntil;

    private static final CurrentMonthDate INSTANCE = create();

    private CurrentMonthDate() {
        roll (System.currentTimeMillis());
    }

    private static CurrentMonthDate create () {
        final long now = System.currentTimeMillis();
        CurrentMonthDate result = new CurrentMonthDate();
        GlobalTimer.INSTANCE.scheduleAtFixedRate(result, result.goodUntil - now, MILLISECONDS_IN_DAY);
        return result;
    }


    public static CurrentMonthDate getInstance () {
        return INSTANCE;
    }

    /** @return Date and Month of given timestamp.
     * Method is slow when it is called at exactly midnight, or if timestamp represent previous day or some moment in the future. */
    public String getDayMonth (long timestamp) {

        final long goodUntilCopy = goodUntil; // volatile
        if (timestamp < goodUntilCopy && timestamp > goodUntilCopy - MILLISECONDS_IN_DAY)
            return currentMonthDay;
        else
            return slowFormat (timestamp);
    }

    private synchronized String slowFormat (long timestamp) {
        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ' ' + MONTH_CODES[c.get(Calendar.MONTH)];
    }

    private synchronized void roll (long timestamp) {
        assert timestamp >= goodUntil;

        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        final long nextMidnight = c.getTimeInMillis() + MILLISECONDS_IN_DAY;

        currentMonthDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ' ' + MONTH_CODES[c.get(Calendar.MONTH)];
        goodUntil = nextMidnight;
    }



    @Override
    public final void run() {
        try {
            // runs at midnight - optional taks that rolls currentMonthDay forward to avoid doing it during logging (if possible)
            roll (System.currentTimeMillis());
        } catch (Throwable e) {
            // this should not happen in the current roll() stack
            // !!! DO NOT USE Logger here !!! TerseFormatter creates CurrentMonthDate statically and hangs during initialization (#13116)
            System.out.println("Error while rolling CurrentMonthDate: " + e.getMessage());
            LangUtil.propagateIfError(e);
        }
    }
}
