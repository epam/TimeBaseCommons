package deltix.util.collections;

import java.util.*;

import deltix.util.collections.generated.*;

/**
 *
 */
public class IndexedArrayList <E> implements List <E> {
    private ObjectToIntegerHashMap <Object> mElemToIdxMap;
    private List <E>                        mElemList;
    
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

    private void        map (E e, int idx) {
        if (e == null)
            throw new IllegalArgumentException ("Cannot add null");
        
        if (!mElemToIdxMap.put (e, idx))
            throw new IllegalArgumentException (
                "Duplicate element: " + e + " at index " + idx
            );
    }
    
    public boolean      remove (Object o) {  
        try {
            mElemList.remove (mElemToIdxMap.remove (o));
            return (true);
        } catch (ObjectToIntegerHashMap.KeyNotFoundException x) {
            return (false);
        }
    }

    public boolean      contains (Object o) {
        return (mElemToIdxMap.containsKey (o));
    }

    public int          indexOf (Object o) {
        return (mElemToIdxMap.get (o, -1));
    }

    public int          lastIndexOf (Object o) {
        return (indexOf (o));
    }

    private void        unmap (E e) {
        try {
            mElemToIdxMap.remove (e);
        } catch (ObjectToIntegerHashMap.KeyNotFoundException x) {
            throw new RuntimeException ("unexpected: " + x, x);
        }
    }
    
    public E            remove (int index) {
        throw new UnsupportedOperationException ("Removal is not supported");
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
        int     existIdx = mElemToIdxMap.get (element, -1);
        
        if (existIdx == index)
            return (element);
        
        if (existIdx >= 0)
            throw new IllegalArgumentException (
                "Element " + element + " being set at index " + index + 
                " already exists at index " + existIdx
            );
        
        E       prev = mElemList.get (index);
        
        unmap (prev);        
        mElemList.set (index, element);
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
        if (o == null)
            throw new IllegalArgumentException ("null element");
        
        int     existIdx = mElemToIdxMap.get (o, -1);
        
        if (existIdx >= 0)
            throw new IllegalArgumentException (
                "Element " + o + 
                " already exists at index " + existIdx
            );
        
        map (o, mElemList.size ());
        mElemList.add (o);
        return (true);
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
