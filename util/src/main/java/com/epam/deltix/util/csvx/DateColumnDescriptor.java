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
package com.epam.deltix.util.csvx;

import java.text.*;
import java.util.*;

import com.epam.deltix.util.time.GMT;

/**
 *
 */
public class DateColumnDescriptor extends ColumnDescriptor {
    private DateFormat          mFormat;    
    private String              mFormatString;
    
    public DateColumnDescriptor () { 
        setFormat ("yyyy-MM-dd", GMT.TZ);
    }
    
    public DateColumnDescriptor (String header) {
        super (header);
        setFormat ("yyyy-MM-dd", GMT.TZ);
    }   
        
    public String               formatDate (long time) {
        return (mFormat.format (new Date (time)));
    }
    
    public void                 setFormat (String format, String timeZone) {
        setFormat (format, TimeZone.getTimeZone (timeZone));
    }
    
    public void                 setFormat (String format, TimeZone timeZone) {
        mFormatString = format;
        mFormat = new SimpleDateFormat (format);
        mFormat.setTimeZone (timeZone);
    }
        
    public long                 getDate () {
        String      s = getString ();
        
        try {
            synchronized (mFormat) {
                return (mFormat.parse (s).getTime ());
            }
        } catch (ParseException px) {
            throw new NumberFormatException (
                "Date '" + s + "' does not comply with format '" + mFormatString + "'"
            );
        }
    }   
}