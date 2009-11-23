package deltix.util.collections;

import java.util.*;

/**
 *
 */
public class ArrayIterator <T> implements Iterator <T>, Iterable <T> {
    private T []            mArray;
    private int             mIdx = 0;
    
    public ArrayIterator (T ... array) {
        mArray = array;
    }
    
    public void             remove () {
        throw new UnsupportedOperationException ();
    }

    public T                next () {
        return (mArray [mIdx++]);
    }

    public boolean          hasNext () {
        return (mIdx < mArray.length);
    }

    public Iterator<T> iterator() {
        return this;
    }
}
