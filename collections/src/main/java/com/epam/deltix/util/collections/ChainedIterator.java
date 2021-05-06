package com.epam.deltix.util.collections;

import java.util.Iterator;

/**
 *  Chains several iterators into one.
 */
public class ChainedIterator <T> implements Iterator <T> {
    private final Iterator <Iterator <? extends T>>     metaIterator;
    private boolean                                     firstTime = true;
    private Iterator <? extends T>                      nextIter = null;
    private Iterator <? extends T>                      lastIter = null;

    public ChainedIterator (Iterator <Iterator <? extends T>> metaIterator) {
        this.metaIterator = metaIterator;
    }

    @SafeVarargs
    public ChainedIterator (Iterator <? extends T> ... chain) {
        this (new ArrayIterator <> (chain));
    }
    
    @SafeVarargs @SuppressWarnings ("unchecked")
    public ChainedIterator (Iterable <? extends T> ... chain) {
        this (new IterableToIteratorTransformer (new ArrayIterator <> (chain)));
    }
    
    @Override
    public boolean                              hasNext () {
        if (firstTime) {
            firstTime = false;
            
            do {
                if (!metaIterator.hasNext ()) {
                    nextIter = null;
                    break;
                }
                                    
                nextIter = metaIterator.next ();                                
            } while (!nextIter.hasNext ());
        }
        
        return (nextIter != null);
    }

    @Override
    public T                                    next () {
        lastIter = nextIter;
        
        T                       ret = nextIter.next ();
        
        while (!nextIter.hasNext ()) {
            if (!metaIterator.hasNext ()) {
                nextIter = null;
                break;
            }
            
            nextIter = metaIterator.next ();            
        }
        
        return (ret);
    }

    @Override
    public void                                 remove () {
        if (lastIter == null)
            throw new IllegalStateException ("next () was not called");
        
        lastIter.remove ();
    }        
}
