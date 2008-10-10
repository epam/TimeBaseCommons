package deltix.util.text;

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
    
    public static String        getFormatStringFor (CharSequence text) {
        Matcher         m;
        String          dateFormat;
        int             limit = text.length ();
        StringBuilder   timeFormat = new StringBuilder (limit);
        
        if ((m = SLASH_DATE_PATTERN_MDY.matcher (text)).matches ()) 
            dateFormat = "M/d/y";
        else if ((m = SLASH_DATE_PATTERN_YMD.matcher (text)).matches ()) 
            dateFormat = "y/M/d";
        else if ((m = DASH_DATE_PATTERN.matcher (text)).matches ())
            dateFormat = "y-M-d";
        else if ((m = NSEP_DATE_PATTERN.matcher (text)).matches ()) 
            dateFormat = "yyyyMMdd";
        else
            return (null);
        
        CharSequence    prefix = m.group (1);
        int             plen = prefix.length ();
        
        for (int ii = 0; ii < plen; ii++)
            appendToFormat (prefix.charAt (ii), timeFormat);
                
        timeFormat.append (dateFormat);
        
        int             timeStartIdx = m.start (2);        
        TPS             state = TPS.BEFORE_HOURS;
        
        for (int ii = timeStartIdx; ii < limit; ii++) {
            char        c = text.charAt (ii);
            boolean     isdigit = Character.isDigit (c);
            
            switch (state) {
                case BEFORE_HOURS:  if (isdigit) state = TPS.HOURS; break;                    
                case HOURS:         if (!isdigit) state = TPS.BEFORE_MINUTES; break;                    
                case BEFORE_MINUTES: if (isdigit) state = TPS.MINUTES; break;                    
                case MINUTES:       if (!isdigit) state = TPS.BEFORE_SECONDS; break;                    
                case BEFORE_SECONDS: if (isdigit) state = TPS.SECONDS; break;                    
                case SECONDS: if (!isdigit) state = TPS.BEFORE_MILLIS; break;                    
                case BEFORE_MILLIS: if (isdigit) state = TPS.MILLIS; break;                    
                case MILLIS: if (!isdigit) state = TPS.STOP; break;                                                                
            }
            
            if (isdigit)
                switch (state) {
                    case HOURS:     timeFormat.append ('H'); break;               
                    case MINUTES:   timeFormat.append ('m'); break;               
                    case SECONDS:   timeFormat.append ('s'); break;   
                    case MILLIS:    timeFormat.append ('S'); break;
                    case STOP:      appendToFormat (c, timeFormat); break;
                }
            else
                appendToFormat (c, timeFormat);
        }
        
        return (timeFormat.toString ());
    }
    
    public static void          main (String [] args) throws Exception {
        if (args.length == 0)
            args = 
                new String [] { 
                    "4/2/2008",
                    "x2008-04-02 23:44",
                    "20080402 1200", 
                    "2008040212:00"
                };
        
        for (String s : args) {
            String              fmt = getFormatStringFor (s);
            
            System.out.println (s + " => " + fmt);
            
            if (fmt != null) {
                SimpleDateFormat    df = new SimpleDateFormat (fmt);

                System.out.println ("    = " + df.parse (s));
            }
        }
    }
}
