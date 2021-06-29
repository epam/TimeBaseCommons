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
package com.epam.deltix.util.lang;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.epam.deltix.gflog.api.LogLevel;

/**
 * Logs exceptions to specified logger.
 */
public class LoggingExceptionHandler implements ExceptionHandler {
    private final Log logger;
    private final LogLevel level;
    private final String msg;

    public LoggingExceptionHandler(Log logger, LogLevel level) {
        this(logger, level, null);
    }

    public LoggingExceptionHandler(Log logger, String msg) {
        this(logger, LogLevel.ERROR, msg);
    }

    public LoggingExceptionHandler(Log logger) {
        this(logger, LogLevel.ERROR, null);
    }

    public LoggingExceptionHandler(Log logger, LogLevel level, String msg) {
        this.logger = logger;
        this.level = level;
        this.msg = msg;
    }

    public void handle(Throwable x) {
        logger.log(level).append(msg).append(x).commit();
    }

    /**
     * Logs to "deltix.util".
     */
    public static final LoggingExceptionHandler INSTANCE =
            new LoggingExceptionHandler(LogFactory.getLog(Util.LOGGER_NAME));
}