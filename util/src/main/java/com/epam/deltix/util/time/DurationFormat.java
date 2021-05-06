package com.epam.deltix.util.time;

/**
 * For durations less than 24h use TimeFormatter
 */
@Deprecated
public class DurationFormat {
    private static void        f2 (StringBuffer sb, int value) {
        if (value < 10)
            sb.append ("0");
        
        sb.append (value);
    }
    
    public static String       format (double seconds) {
        int         isec = (int) seconds;
        
        int         secComp = isec % 60;
        
        isec /= 60;
        
        int         minComp = isec % 60;
        
        isec /= 60;
        
        int         hourComp = isec;
        
        StringBuffer    sb = new StringBuffer ();
        
        sb.append (hourComp);
        sb.append (":");
        f2 (sb, minComp);
        sb.append (":");
        f2 (sb, secComp);
        
        return (sb.toString ());
    }
}
