package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Filter;
import java.util.Iterator;

/**
 *  Filtering Iterable
 */
public final class FilterIterable <T> implements Iterable <T> {
    private Iterable <T>            delegate;
    private Filter <? super T>      filter;

    public FilterIterable (Iterable <T> delegate, Filter <? super T> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }
        
    @Override
    public Iterator <T>             iterator () {  
        Iterator <T>        di = delegate.iterator ();
        
        return (filter == null ? di : new FilterIterator <T> (di, filter));
    }        
}
