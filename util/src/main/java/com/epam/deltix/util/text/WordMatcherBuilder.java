package com.epam.deltix.util.text;

import com.epam.deltix.util.collections.EmptyEnumeration;
import java.util.*;

/**
 *  Builds a decision tree for matching words from the given set.
 *  This matches about 13,600 times faster than java regular expressions.
 */
public class WordMatcherBuilder implements WordMatcher {
    private Node        mRoot = new Node ();
    private int         mMaxLength = -1;
    
    public WordMatcherBuilder () {
    }
    
    public WordMatcherBuilder (Collection <? extends CharSequence> css) {
        for (CharSequence cs : css)
            add (cs);
    }
    

    public void             add (CharSequence s) {
        mMaxLength = Math.max (mMaxLength, s.length ());        
        mRoot.add (s, 0);
    } 
    
    public void             dump () {
        mRoot.dump ("");
    }
    
    WordMatcher             compile32 () {           
        return (new WordMatcher32 (mRoot, mMaxLength));
    }
    
    public WordMatcher      compile () {           
        try {
            return (new WordMatcher16 (mRoot, mMaxLength));
        } catch (WordMatcher16.CodeTooBigException x) {
            return (new WordMatcher32 (mRoot, mMaxLength));
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
     *  @param bytes    String to match
     *  @return         Whether it matches the vocabulary.
     */
    public boolean          matches (byte [] bytes, int offset, int length) {
        return (mRoot.match (bytes, offset, length));
    }    
    
    public Enumeration <CharSequence>     vocabulary () {
        if (mMaxLength < 0)
            return (new EmptyEnumeration <CharSequence> ());
        
        return (new NodeVocabularyEnumeration (mRoot, mMaxLength));
    }    
    
    public static void main (String [] args) {
        WordMatcherBuilder  wmb = new WordMatcherBuilder ();
        
        wmb.add ("");
        wmb.add ("a");
        wmb.add ("ba");
        wmb.add ("bx");
        wmb.add ("ab");
        wmb.add ("ass");
        
        WordMatcher m = wmb.compile32 ();
        
        for (Enumeration <CharSequence> e = m.vocabulary (); e.hasMoreElements (); ) {
            CharSequence cs = e.nextElement ();
            
            System.out.println (cs);     
            System.out.println (m.matches(cs));
        }
        
    }
}
