package deltix.util.text;

import deltix.util.lang.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.*;

/**
 *  Reverse-engineers Java format from date text.
 */ 
public class DateFormatDetector {
    private static final Pattern    SLASH_DATE_PATTERN_MDY = 
        Pattern.compile ("([^\\d]*)\\d\\d?/\\d\\d?/\\d\\d\\d\\d(.*)");
    
    private static final Pattern    SLASH_DATE_PATTERN_YMD = 
        Pattern.compile ("([^\\d]*)\\d\\d\\d\\d/\\d\\d?/\\d\\d?(.*)");
    
    private static final Pattern    DASH_DATE_PATTERN = 
        Pattern.compile ("([^\\d]*)\\d\\d\\d\\d-\\d\\d?-\\d\\d?(.*)");
    
    private static final Pattern    DASH_DATE_PATTERN2 = 
        Pattern.compile ("([^\\d]*)\\d\\d?-\\d\\d?-\\d\\d\\d\\d(.*)");
    
    private static final Pattern    NSEP_DATE_PATTERN = 
        Pattern.compile ("([^\\d]*)\\d\\d\\d\\d\\d\\d\\d\\d(.*)");

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
        
        if ((m = SLASH_DATE_PATTERN_MDY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yyyy";
        else if ((m = SLASH_DATE_PATTERN_YMD.matcher (text)).matches ()) 
            dateFormat = "yyyy/MM/dd";
        else if ((m = DASH_DATE_PATTERN.matcher (text)).matches ())
            dateFormat = "yyyy-MM-dd";
        else if ((m = DASH_DATE_PATTERN2.matcher (text)).matches ())
            dateFormat = "MM-dd-yyyy";
        else if ((m = NSEP_DATE_PATTERN.matcher (text)).matches ()) 
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
        
        if ((m = SLASH_DATE_PATTERN_MDY.matcher (text)).matches ()) 
            dateFormat = "MM/dd/yyyy";
        else if ((m = SLASH_DATE_PATTERN_YMD.matcher (text)).matches ()) 
            dateFormat = "yyyy/MM/dd";
        else if ((m = DASH_DATE_PATTERN.matcher (text)).matches ())
            dateFormat = "yyyy-MM-dd";
        else if ((m = DASH_DATE_PATTERN2.matcher (text)).matches ())
            dateFormat = "MM-dd-yyyy";
        else if ((m = NSEP_DATE_PATTERN.matcher (text)).matches ()) 
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
    
    private static boolean      getTimeFormatStringFor (
        CharSequence                text, 
        int                         timeStartIdx,
        StringBuilder               timeFormat
    )
    {
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
                    "15:34",
                    "x'2008-04-02 23:44",
                    "20080402 1200", 
                    "2008040212:00",
                    "02-04-2009 19:53:39.205" ,
                    "02-04-2009"
                };
        
        for (String s : args) {            
            System.out.println (s);
            
            testFormat (s, "TIME", getTimeFormatStringFor (s));
            testFormat (s, "DATE", getDateFormatStringFor (s));
            testFormat (s, "D/T", getDateTimeFormatStringFor (s));
        }
    }
}

