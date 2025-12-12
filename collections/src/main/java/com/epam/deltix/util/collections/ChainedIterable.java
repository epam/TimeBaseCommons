package com.epam.deltix.util.collections;

import java.util.Iterator;

/**
 *  Chains several iterables into one.
 */
public class ChainedIterable <T> implements Iterable <T> {
    private final Iterable <? extends T> []         chain;
    
    @SafeVarargs
    public ChainedIterable (Iterable <? extends T> ... chain) {
        this.chain = chain;
    }

    @Override
    public Iterator <T>                             iterator () {
        return (new ChainedIterator <T> (chain));
    }        
}
