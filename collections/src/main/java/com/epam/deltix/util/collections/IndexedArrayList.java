package com.epam.deltix.util.collections;

import java.util.*;

import com.epam.deltix.util.collections.generated.*;
import java.io.Serializable;

/**
 *  <p>A special implementation of List which provides very fast indexOf and 
 *  contains operations.</p>
 *  Notes:
 *  <ul>
 *      <li>Duplicate elements are not allowed. Therefore, indexOf () 
 *          and lastIndexOf () always return identical results.</li>
 *      <li>Insertion into the middle is not allowed.</li>
 *  </ul>
 */
public class IndexedArrayList <E> implements List <E>, Serializable {
    static final long   serialVersionUID = 1L;
    
    private ObjectToIntegerHashMap <Object> mElemToIdxMap;
    private List <E>                        mElemList;
    private int                             mFirstNullIdx = -1;
    private boolean                         mAllowAddingDuplicates = false;
    
    public void         setAllowAddingDuplicates (boolean ignoreDuplicates) {
        mAllowAddingDuplicates = ignoreDuplicates;
    }
    
    public static <E> IndexedArrayList <E>  wrapIfNecessary (List <E> in) {
        if (in == null)
            return (null);
        
        if (in instanceof IndexedArrayList)
            return ((IndexedArrayList <E>) in);
        
        return (new IndexedArrayList <E> (in));
    }
    
    public IndexedArrayList () {  
        this (32);
    }

    public IndexedArrayList (int capacity) {    
        mElemToIdxMap = new ObjectToIntegerHashMap <Object> (capacity);
        mElemList = new ObjectArrayList <E> (capacity);
    }
    
    public IndexedArrayList (int capacity, boolean ignoreDuplicates) {    
        this (capacity);
        setAllowAddingDuplicates(ignoreDuplicates);
    }

    public IndexedArrayList (List <E> list) {
        int             num = list.size ();
        
        mElemList = list;
        mElemToIdxMap = new ObjectToIntegerHashMap <Object> (num);
        
        for (int ii = 0; ii < num; ii++)
            map (list.get (ii), ii);
    }

    public IndexedArrayList (Collection <E> list) {
        this (new ObjectArrayList <E> (list));
    }

    public IndexedArrayList (E [] array) {
        int             num = array.length;
        
        mElemList = new ArrayList <E> (num);
        mElemToIdxMap = new ObjectToIntegerHashMap <Object> (num);
        
        for (int ii = 0; ii < num; ii++) {
            E   e = array [ii];
            mElemList.add (e);
            map (e, ii);
        }
    }

    private void        map (E e, int idx) {
        if (e == null) {
            if (mFirstNullIdx < 0 || idx < mFirstNullIdx)
                mFirstNullIdx = idx;            
        }            
        else if (!mElemToIdxMap.put (e, idx))
            throw new IllegalArgumentException (
                "Duplicate element: " + e + " at index " + idx
            );        
    }
    
    private void        unmap (E e, int idx) {
        if (e == null) {
            assert mFirstNullIdx >= 0 && idx >= mFirstNullIdx : 
                "idx: " + idx + "; mFirstNullIdx: " + mFirstNullIdx;
            
            if (idx == mFirstNullIdx) {
                int     size = mElemList.size ();
                
                for (;;) {
                    mFirstNullIdx++;
                    
                    if (mFirstNullIdx == size) {
                        mFirstNullIdx = -1;
                        break;
                    }
                    
                    if (mElemList.get (mFirstNullIdx) == null)
                        break;
                }
            }
        }
        else {            
            int     prevIdx = mElemToIdxMap.remove (e, -1);

            assert prevIdx == idx : "prevIdx: " + prevIdx + "; idx: " + idx;
        }
    }
    
    public boolean      remove (Object o) {
        int     idx = indexOf (o);
        
        if (idx < 0)
            return (false);
        
        remove (idx);        
        return (true);
    }

    public boolean      contains (Object o) {
        if (o == null)
            return (mFirstNullIdx >= 0);
        
        return (mElemToIdxMap.containsKey (o));
    }

    public int          indexOf (Object o) {
        if (o == null)
            return (mFirstNullIdx);
        
        return (mElemToIdxMap.get (o, -1));
    }

    public int          lastIndexOf (Object o) {
        if (o == null || mAllowAddingDuplicates)
            return (mElemList.lastIndexOf (o));
        
        return (mElemToIdxMap.get (o, -1));
    }

    public E            remove (int index) {                
        E       e = mElemList.remove (index);
        
        unmap (e, index);

        final int       s = size ();

        for (int ii = index; ii < s; ii++) {
            E   ee = mElemList.get (ii);

            if (ee != null)
                mElemToIdxMap.put (ee, ii);
        }

        return (e);
    }

    public ListIterator <E> listIterator (int index) {
        return (mElemList.listIterator ());
    }

    public E            get (int index) {
        return (mElemList.get (index));
    }

    public <T> T []     toArray (T [] a) {
        return (mElemList.toArray (a));
    }

    public boolean      addAll (Collection <? extends E> c) {
        for (E e : c)
            add (e);
        
        return (!c.isEmpty ());
    }

    public E            set (int index, E element) {
        if (element != null) {
            int     existIdx = mElemToIdxMap.get (element, -1);

            //  Still call set () to ensure correct identities
            if (existIdx == index)
                return (mElemList.set (index, element));

            if (existIdx >= 0)
                throw new IllegalArgumentException (
                    "Element " + element + " being set at index " + index + 
                    " already exists at index " + existIdx
                );            
        }
                 
        E       prev = mElemList.set (index, element);
        
        unmap (prev, index);        
        map (element, index);
        
        return (prev);
    }

    public void         add (int index, E element) {
        if (index != size ())
            throw new UnsupportedOperationException ("Insertion is not supported");
        
        add (element);
    }

    public boolean      addAll (int index, Collection<? extends E> c) {
        if (index != size ())
            throw new UnsupportedOperationException ("Insertion is not supported");
        
        return (addAll (c));
    }

    public boolean      retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ("Removal is not supported");
    }

    public boolean      removeAll (Collection<?> c) {
        throw new UnsupportedOperationException ("Removal is not supported");
    }

    public boolean      containsAll (Collection <?> c) {
        for (Object e : c)
            if (!mElemToIdxMap.containsKey (e))
                return (false);
        
        return (true);
    }

    public Object []    toArray () {
        return (mElemList.toArray ());
    }

    public List <E>     subList (int fromIndex, int toIndex) {
        return (mElemList.subList (fromIndex, toIndex));
    }

    public int          size () {
        return (mElemList.size ());
    }

    public boolean      add (E o) {
        if (o != null) {
            int     existIdx = mElemToIdxMap.get (o, -1);

            if (existIdx >= 0) {
                if (mAllowAddingDuplicates)
                    return (false);
                
                throw new IllegalArgumentException (
                    "Element " + o + 
                    " already exists at index " + existIdx
                );
            }
            
            map (o, mElemList.size ());
        }
                
        mElemList.add (o);
        return (true);
    }

    public int          getIndexOrAdd (E o) {
        int         idx;
        
        if (o == null) {
            idx = mFirstNullIdx;

            if (idx < 0) {
                idx = mElemList.size ();
                mElemList.add (o);
            }
        }
        else {
            idx = mElemToIdxMap.get (o, -1);

            if (idx < 0) {
                idx = mElemList.size ();
                mElemList.add (o);
                map (o, idx);
            }
        }
        
        return (idx);
    }

    public void         clear () {
        mElemList.clear ();
        mElemToIdxMap.clear ();
    }

    public boolean      isEmpty () {
        return (mElemList.isEmpty ());
    }

    public Iterator <E> iterator () {
        return (mElemList.iterator ());
    }

    public ListIterator <E> listIterator () {
        return (mElemList.listIterator ());
    }
}
