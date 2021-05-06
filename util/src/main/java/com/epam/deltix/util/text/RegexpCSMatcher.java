package com.epam.deltix.util.text;

import java.util.regex.Pattern;

/**
 *
 */
public final class RegexpCSMatcher implements CSMatcher {
    public static CSMatcher     INSTANCE = new RegexpCSMatcher ();
    
    private RegexpCSMatcher () { }
    
    public boolean  matches (CharSequence text, CharSequence pattern) {
        return (Pattern.matches (pattern.toString (), text));
    }    
}
