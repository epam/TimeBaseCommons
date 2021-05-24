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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SlowTimeFormatter {
    public static final SlowTimeFormatter   GMT_INSTANCE =
        new SlowTimeFormatter ();

    private DateFormat          mDF;

    public SlowTimeFormatter () {
        this ("GMT");
    }

    public SlowTimeFormatter (String tz) {
        this ("yyyy-MM-dd HH:mm:ss z", tz);
    }

    public SlowTimeFormatter (String formatSpec, String tz) {
        this (formatSpec, TimeZone.getTimeZone (tz));
    }

    public SlowTimeFormatter (String formatSpec, TimeZone tz) {
        mDF = new SimpleDateFormat (formatSpec);
        mDF.setTimeZone (tz);
    }

    public String                   format (long t) {
        return (format (new Date (t)));
    }

    public synchronized String      format (Date t) {
        return (mDF.format (t));
    }

}