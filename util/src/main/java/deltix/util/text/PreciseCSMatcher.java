package deltix.util.text;

import deltix.util.lang.Util;

/**
 *
 */
public final class PreciseCSMatcher implements CSMatcher {
    public static CSMatcher     INSTANCE = new PreciseCSMatcher ();
    
    private PreciseCSMatcher () { }
    
    public boolean  matches (CharSequence text, CharSequence pattern) {
        return (Util.equals (text, pattern));
    }    
}
