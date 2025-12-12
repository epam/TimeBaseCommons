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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Description: deltix.util.log.DetailFormatter
 * Date: Jul 15, 2010
 *
 * @author Nickolay Dul
 */
public class DetailFormatter extends Formatter {
    public static final String PRINT_CONTEXT_PROPERTY = "QuantServer.logging.detailFormatter.printContext";

    private final boolean printContext;

    public DetailFormatter() {
        this(Boolean.getBoolean(PRINT_CONTEXT_PROPERTY));
    }

    public DetailFormatter(boolean printContext) {
        this.printContext = printContext;
    }

    @Override
    public String format(LogRecord record) {
        StringBuilder sbuf = new StringBuilder(256);
        // time
        sbuf.append(String.format("%1$tF %1$tT.%1$tL", record.getMillis())).append(' ');

        // context
        if (printContext) {
            if (record.getSourceClassName() != null) {
                sbuf.append(record.getSourceClassName());
            } else {
                sbuf.append(record.getLoggerName());
            }
            if (record.getSourceMethodName() != null) {
                sbuf.append(' ').append(record.getSourceMethodName());
            }
            sbuf.append(Util.NATIVE_LINE_BREAK);
        }

        // level
        sbuf.append(record.getLevel()).append(' ');
        // thread id
        sbuf.append('[').append(record.getThreadID()).append("] ");

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
}
