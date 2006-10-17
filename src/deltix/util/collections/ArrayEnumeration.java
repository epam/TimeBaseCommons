package deltix.util.collections;

import java.util.*;

public class ArrayEnumeration implements Enumeration {
    
    private Object [] mArray;
    private int mIdx;
    private boolean mHasNext;
    
    public ArrayEnumeration (Object [] array){
        mArray = array;
        mIdx = 0;
        mHasNext =(array != null && array.length > 0);
    }
    
    public boolean hasMoreElements(){
        return mHasNext;
    }
    
    public Object nextElement(){
        
        if (mHasNext){
            Object o = mArray[mIdx];
            mIdx++;
            mHasNext =(mIdx < mArray.length);
            return o;
        }
        else throw new NoSuchElementException("No Elements left in Enumeration");
            
    }

}