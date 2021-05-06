package com.epam.deltix.util.text;

/**
 *  Checks that text matches a pattern in different ways.
 */
public interface CSMatcher {
    public boolean  matches (CharSequence text, CharSequence pattern);
}
