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
package com.epam.deltix.util.text;

import com.epam.deltix.util.lang.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.*;

/**
 *  Reverse-engineers Java format from date text.
 */ 
public class DateFormatDetector {
    
    private static final Pattern SLASH_PATTERN_MM_DD_YY   = Pattern.compile("([^\\d]*)\\d{1,2}/\\d{1,2}/\\d{2}(.*)"    );
    private static final Pattern SLASH_PATTERN_MM_DD_YYYY = Pattern.compile("([^\\d]*)\\d\\d?/\\d\\d?/\\d{4}(.*)"      );
    private static final Pattern SLASH_PATTERN_YYYY_MM_DD = Pattern.compile("([^\\d]*)\\d\\d\\d\\d/\\d\\d?/\\d\\d?(.*)");
    private static final Pattern DASH_PATTERN_YYYY_MM_DD  = Pattern.compile("([^\\d]*)\\d\\d\\d\\d-\\d\\d?-\\d\\d?(.*)");
    private static final Pattern DASH_PATTERN_MM_DD_YYYY  = Pattern.compile("([^\\d]*)\\d{1,2}-\\d{1,2}-\\d{4}(.*)"    );
    private static final Pattern DASH_PATTERN_MM_DD_YY    = Pattern.compile("([^\\d]*)\\d{1,2}-\\d{1,2}-\\d{2}(.*)"    );
    private static final Pattern NSEP_PATTERN_YYYY_MM_DD  = Pattern.compile("([^\\d]*)\\d\\d\\d\\d\\d\\d\\d\\d(.*)"    );

    public static final String DF_AM_MARKER = "AM";
    public static final String DF_PM_MARKER = "PM";

    private enum TPS {
        BEFORE_HOURS,
        HOURS,
        BEFORE_MINUTES,
        MINUTES,
        BEFORE_SECONDS,
        SECONDS,
        BEFORE_MILLIS,
        MILLIS,
        STOP
    };

    private static void         appendToFormat (char c, StringBuilder out) {
        if (c == '\'')
            out.append ("''");
        else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
            out.append ('\'');
            out.append (c);
            out.append ('\'');
        }
        else
            out.append (c);
    }
    
    public static String        getDateFormatStringFor (CharSequence text) {
        
        if (text == null)
            return null;
        
        int             limit = text.length ();
        StringBuilder   timeFormat = new StringBuilder (limit);        
        int             suffixStart = getDateFormatStringFor (text, timeFormat);
        
        if (suffixStart < 0)
            return (null);
        
        for (int ii = suffixStart; ii < limit; ii++) {
            char        c = text.charAt (ii);
            
            if (Character.isDigit (c))
                return (null);
            
            appendToFormat (c, timeFormat);
        }
        
        return (timeFormat.toString ());
    }
    
    private static int          getDateFormatStringFor (
        CharSequence                text,
        StringBuilder               timeFormat
    )
    {
        Matcher         m;
        String          dateFormat;
        
        if ((m = SLASH_PATTERN_MM_DD_YYYY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yyyy";
        else if ((m = SLASH_PATTERN_MM_DD_YY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yy";
        else if ((m = SLASH_PATTERN_YYYY_MM_DD.matcher (text)).matches ()) 
            dateFormat = "yyyy/MM/dd";
        else if ((m = DASH_PATTERN_YYYY_MM_DD.matcher (text)).matches ())
            dateFormat = "yyyy-MM-dd";
        else if ((m = DASH_PATTERN_MM_DD_YYYY.matcher (text)).matches ())
            dateFormat = "MM-dd-yyyy";
        else if ((m = DASH_PATTERN_MM_DD_YY.matcher (text)).matches ())
            dateFormat = "MM-dd-yy";
        else if ((m = NSEP_PATTERN_YYYY_MM_DD.matcher (text)).matches ()) 
            dateFormat = "yyyyMMdd";
        else
            return (-1);
        
        
        CharSequence    prefix = m.group (1);
        int             plen = prefix.length ();
        
        for (int ii = 0; ii < plen; ii++)
            appendToFormat (prefix.charAt (ii), timeFormat);
                
        timeFormat.append (dateFormat);
        
        return (m.start (2));
    }
    
    public static String        getDateTimeFormatStringFor (CharSequence text) {
        Matcher         m;
        String          dateFormat;
        int             limit = text.length ();
        StringBuilder   timeFormat = new StringBuilder (limit);
        
        if ((m = SLASH_PATTERN_MM_DD_YYYY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yyyy";
        else if ((m = SLASH_PATTERN_MM_DD_YY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yy";
        else if ((m = SLASH_PATTERN_YYYY_MM_DD.matcher (text)).matches ()) 
            dateFormat = "yyyy/MM/dd";
        else if ((m = DASH_PATTERN_YYYY_MM_DD.matcher (text)).matches ())
            dateFormat = "yyyy-MM-dd";
        else if ((m = DASH_PATTERN_MM_DD_YYYY.matcher (text)).matches ())
            dateFormat = "MM-dd-yyyy";
        else if ((m = DASH_PATTERN_MM_DD_YY.matcher (text)).matches ())
            dateFormat = "MM-dd-yy";
        else if ((m = NSEP_PATTERN_YYYY_MM_DD.matcher (text)).matches ()) 
            dateFormat = "yyyyMMdd";
        else
            return (null);
        
        CharSequence    prefix = m.group (1);
        int             plen = prefix.length ();
        
        for (int ii = 0; ii < plen; ii++)
            appendToFormat (prefix.charAt (ii), timeFormat);
                
        timeFormat.append (dateFormat);
        
        if (!getTimeFormatStringFor (text, m.start (2), timeFormat))
            return (null);

        return (timeFormat.toString ());
    }
    
    public static String        getTimeFormatStringFor (CharSequence text) {
        StringBuilder   timeFormat = new StringBuilder (text.length ());
        
        if (!getTimeFormatStringFor (text, 0, timeFormat))
            return (null);
        
        return (timeFormat.toString ());
    }
    
    private static boolean getTimeFormatStringFor(
            CharSequence  text,
            int           timeStartIdx,
            StringBuilder timeFormat   ) {

        String value = text.toString();
        boolean hasAmOrPmMarker = value.endsWith(DF_AM_MARKER) || value.endsWith(DF_PM_MARKER);
        if (hasAmOrPmMarker){
            text = text.subSequence(0, text.length() - DF_AM_MARKER.length());
        }

        int             limit = text.length ();
        boolean         success = false;
        TPS             state = TPS.BEFORE_HOURS;

        for (int ii = timeStartIdx; ii <= limit; ii++) {
            char        c;
            boolean     isdigit;
            
            if (ii < limit) {
                c = text.charAt (ii);            
                isdigit = Character.isDigit (c);
            }
            else {
                c = 0;
                isdigit = false;
            }
            
            switch (state) {
                case BEFORE_HOURS:  
                    if (isdigit) state = TPS.HOURS; 
                    break; 
                
                case HOURS:         
                    if (!isdigit) 
                        state = TPS.BEFORE_MINUTES; 
                    else if (StringUtils.endsWith (timeFormat, "HH"))
                        state = TPS.MINUTES;
                    break;                  
                    
                case BEFORE_MINUTES: 
                    if (isdigit) state = TPS.MINUTES; 
                    break;           
                    
                case MINUTES:       
                    if (!isdigit) 
                        state = TPS.BEFORE_SECONDS;
                    else if (StringUtils.endsWith (timeFormat, "mm"))
                        state = TPS.SECONDS;
                    break;
                    
                case BEFORE_SECONDS:
                    if (isdigit) state = TPS.SECONDS; 
                    break;                    
                    
                case SECONDS: 
                    if (!isdigit) 
                        state = TPS.BEFORE_MILLIS; 
                    else if (StringUtils.endsWith (timeFormat, "ss"))
                        state = TPS.MILLIS;
                    break;                    
                    
                case BEFORE_MILLIS: 
                    if (isdigit) state = TPS.MILLIS; 
                    break;                    
                    
                case MILLIS: 
                    if (!isdigit)
                        if (StringUtils.endsWith (timeFormat, "SSS")) 
                            state = TPS.STOP;
                        else
                            return (false); // Prohibit partial millis to avoid false matches with year!
                    else if (StringUtils.endsWith (timeFormat, "SSS")) 
                        state = TPS.STOP; 
                    break;                                                                
            }
            
            if (c == 0)
                break;
            
            if (isdigit)
                switch (state) {
                    case HOURS:     
                        timeFormat.append ('H'); 
                        success = true;
                        break;         
                        
                    case MINUTES:   timeFormat.append ('m'); break;               
                    case SECONDS:   timeFormat.append ('s'); break;   
                    case MILLIS:    timeFormat.append ('S'); break;
                    case STOP:      
                        return (false);
                }
            else
                appendToFormat (c, timeFormat);
        }

        if (hasAmOrPmMarker){
            timeFormat.append("a");
        }
        
        return (success);
    }

    private static void         testFormat (String s, String header, String fmt) {
        if (fmt != null) {
            SimpleDateFormat    df = new SimpleDateFormat (fmt);
            String              result;
            
            try {
                result = df.format (df.parse (s));
            } catch (ParseException x) {
                result = x.getMessage ();
            }
            
            System.out.println ("    " + header + ": " + fmt + " ==> " + result);
        }
    }           
    
    public static void          main (String [] args) throws Exception {
        if (args.length == 0)
            args = 
                new String [] { 
                    "4/2/2008",
                    "4/2/2008 19:53:39.205",
                    "4/2/08",
                    "4/2/08 23:44",
                    "15:34",
                    "x'2008-04-02 23:44",
                    "20080402 1200", 
                    "2008040212:00",
                    "02-04-2009 19:53:39.205" ,
                    "02-04-2009",
                    "02-04-09",
                    "02-04-09 19:53:39.205" ,
                };
        
        for (String s : args) {            
            System.out.println (s);
            
            testFormat (s, "TIME", getTimeFormatStringFor (s));
            testFormat (s, "DATE", getDateFormatStringFor (s));
            testFormat (s, "D/T", getDateTimeFormatStringFor (s));
        }
    }
}