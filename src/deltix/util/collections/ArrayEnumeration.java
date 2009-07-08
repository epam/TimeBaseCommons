package deltix.util.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration <T> implements Enumeration <T> {
    private Object[] mArray;
    private int mIdx;
    private boolean mHasNext;

    public ArrayEnumeration(Object[] array) {
        mArray = array;
        mIdx = 0;
        mHasNext = (array != null && array.length > 0);
    }

    public boolean hasMoreElements() {
        return mHasNext;
    }

    public T nextElement() {

        if (mHasNext) {
            Object o = mArray[mIdx];
            mIdx++;
            mHasNext = (mIdx < mArray.length);
            return (T) o;
        } 
        else
            throw new NoSuchElementException("No Elements left in Enumeration");
    }

}