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

public abstract class TimeConstants {
    public static final int     SECOND = 1000;
    public static final int     MINUTE = 60 * SECOND;
    public static final int     HOUR = 60 * MINUTE;
    public static final int     HALF_DAY = MINUTE * 60 * 12;
    public static final int     DAY = MINUTE * 60 * 24;

    public static final long    TIMESTAMP_UNKNOWN = Long.MIN_VALUE;
}