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

import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.text.SimpleMessageFormat;
import com.epam.deltix.util.time.TimeFormatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Simple formatter that displays only a log message
 *
 * THIS CLASS IS INSTALLED AS JAVA EXTENSION - PLEASE AVOID ADDING ANY DEPENDENCIES.
 *
 */
public class TerseFormatter extends Formatter {
    private static CurrentMonthDate currentMonthDate = CurrentMonthDate.getInstance();

    /**
     * Format the given log record and return the formatted string.
     */
    public String format (LogRecord record) {
        StringBuilder sbuf = new StringBuilder(256);

        // time
        long time = record.getMillis();
        if (time != 0) {
            sbuf.append (currentMonthDate.getDayMonth(time)).append (' ');
            sbuf.append (formatTimestamp(time)).append(' ');
        }

        // level
        sbuf.append(record.getLevel()).append(' ');

        // message
        String message = record.getMessage ();
        Object[] params = record.getParameters();
        if (params != null && params.length > 0) {
            SimpleMessageFormat.format(sbuf, message, params);
        } else {
            sbuf.append(message);
        }

        if (record.getThrown() != null) {
            sbuf.append(Util.NATIVE_LINE_BREAK);
            sbuf.append(Util.printStackTrace(record.getThrown()));
        }

        sbuf.append(Util.NATIVE_LINE_BREAK);
        return sbuf.toString();
    }

    protected String formatTimestamp(long time) {
        return TimeFormatter.formatTimeOfDayGMT(time);
    }
}
