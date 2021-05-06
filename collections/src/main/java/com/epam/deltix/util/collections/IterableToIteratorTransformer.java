package com.epam.deltix.util.collections;

import java.util.Iterator;

/**
 *  Transforms an iterator over iterable into iterator over iterator. Whew...
 */
public class IterableToIteratorTransformer <T>
    extends IteratorTransformer <Iterable <T>, Iterator <T>>
{
    public IterableToIteratorTransformer (Iterator <Iterable <T>> delegate) {
        super (delegate);
    }
    
    @Override
    protected Iterator <T>      transform (Iterable <T> arg) {
        return (arg.iterator ());
    }
}
