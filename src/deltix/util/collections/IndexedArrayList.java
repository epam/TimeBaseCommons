package deltix.util.collections;

import java.util.*;

import deltix.util.collections.generated.*;

/**
 *  <p>A special implementation of List which provides very fast indexOf and 
 *  contains operations.</p>
 *  <p>Notes:
 *  <ul>
 *      <li>Null elements can be added, but are not indexed, 
 *          therefore indexOf (null) is not allowed.
 *      <li>Duplicate elements are not allowed. Therefore, indexOf () 
 *          and lastIndexOf () always return identical results.
 *      <li>Removal of elements is not allowed.
 *      <li>Insertion into the middle is not allowed.
 *  </ul></p>
 */
public class IndexedArrayList <E> implements List <E> {
    private ObjectToIntegerHashMap <Object> mElemToIdxMap;
    private List <E>                        mElemList;
    private boolean                         mAllowAddingDuplicates = false;
    
    public void         setAllowAddingDuplicates (boolean flag) {
        mAllowAddingDuplicates = flag;
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
        if (!mElemToIdxMap.put (e, idx))
            throw new IllegalArgumentException (
                "Duplicate element: " + e + " at index " + idx
            );
    }
    
    private void        unmap (E e) {
        try {
            mElemToIdxMap.remove (e);
        } catch (ObjectToIntegerHashMap.KeyNotFoundException x) {
            throw new RuntimeException ("unexpected: " + x, x);
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
            throw new UnsupportedOperationException ("contains (null)");
        
        return (mElemToIdxMap.containsKey (o));
    }

    public int          indexOf (Object o) {
        if (o == null)
            return (mElemList.indexOf (null));
        
        return (mElemToIdxMap.get (o, -1));
    }

    public int          lastIndexOf (Object o) {
        if (o == null)
            return (mElemList.lastIndexOf (null));
        
        return (mElemToIdxMap.get (o, -1));
    }

    public E            remove (int index) {
        if (index != size () - 1)
            throw new UnsupportedOperationException (
                "Removal from the middle is not supported"
            );
        
        E       e = mElemList.remove (index);
        
        if (e != null)
            unmap (e);
        
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
            
            map (element, index);
        }
        
        E       prev = mElemList.set (index, element);
        
        if (prev != null)
            unmap (prev);        
        
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
