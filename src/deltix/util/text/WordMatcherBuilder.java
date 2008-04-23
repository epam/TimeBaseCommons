package deltix.util.text;

import java.util.*;

/**
 *  Builds a decision tree for matching words from the given set.
 *  This matches about 13,600 times faster than java regular expressions.
 */
public class WordMatcherBuilder implements WordMatcher {
    private Node        mRoot = new Node ();
    
    public WordMatcherBuilder () {
    }
    
    public WordMatcherBuilder (Collection <? extends CharSequence> ... adds) {
        for (Collection <? extends CharSequence> c : adds)
            for (CharSequence cs : c)
                add (cs);
    }
    
    public void             add (CharSequence s) {
        mRoot.add (s, 0);
    } 
    
    public void             dump () {
        mRoot.dump ("");
    }
    
    public WordMatcher      compile () {        
        try {
            return (new WordMatcher16 (mRoot));
        } catch (WordMatcher16.CodeTooBigException x) {
            return (new WordMatcher32 (mRoot));
        }                
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (CharSequence s) {
        return (matches (s, 0, s.length ()));
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (CharSequence s, int offset, int length) {
        return (mRoot.match (s, offset, length));
    }
    
    /**
     *  A relatively slow, interpreted version of matching logic. Used for testing.
     *  @param s        String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (byte [] bytes, int offset, int length) {
        return (mRoot.match (bytes, offset, length));
    }    
}
