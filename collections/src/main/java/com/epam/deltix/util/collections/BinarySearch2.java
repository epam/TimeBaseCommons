package com.epam.deltix.util.collections;

import java.util.Comparator;
import java.util.List;
import javax.swing.ListModel;

/**
 *
 */
public class BinarySearch2 {
    public static <A, B> int            binarySearch (
        List <? extends A>                  list,
        B                                   key,
        Comparator2 <? super A, ? super B>  c
    )
    {
        int         low = 0;
        int         high = list.size () - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            A       midVal = list.get(mid);
            int     cmp = c.compare (midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }

    public static <A, B> int            binarySearch (
        A []                                arr,
        B                                   key,
        Comparator2 <? super A, ? super B>  c
    )
    {
        return (binarySearch (arr, 0, arr.length, key, c));
    }

    public static <A, B> int            binarySearch (
        A []                                arr,
        int                                 offset,
        int                                 length,
        B                                   key,
        Comparator2 <? super A, ? super B>  c
    )
    {
        int         low = offset;
        int         high = offset + length - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            A       midVal = arr [mid];
            int     cmp = c.compare (midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }

    public static <A> int            binarySearch (
        A []                                arr,
        int                                 offset,
        int                                 length,
        A                                   key,
        Comparator <? super A>              c
    )
    {
        int         low = offset;
        int         high = offset + length - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            A       midVal = arr [mid];
            int     cmp = c.compare (midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }

    @SuppressWarnings ("unchecked")
    public static <A> int               binarySearch (
        ListModel                           model,
        int                                 offset,
        int                                 length,
        A                                   key,
        Comparator <A>                      c
    )
    {
        int         low = offset;
        int         high = offset + length - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            A       midVal = (A) model.getElementAt (mid);
            int     cmp = c.compare (midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }

    @SuppressWarnings ("unchecked")
    public static <A,B> int            binarySearch (
        ListModel                           model,
        int                                 offset,
        int                                 length,
        B                                   key,
        Comparator2 <A,B>                   c
    )
    {
        int         low = offset;
        int         high = offset + length - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            A       midVal = (A) model.getElementAt (mid);
            int     cmp = c.compare (midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }
    
    public static int                   binarySearch (
        int []                              arr,
        int                                 offset,
        int                                 length,
        int                                 key
    )
    {
        int         low = offset;
        int         high = offset + length - 1;

        while (low <= high) {
            int     mid = (low + high) >>> 1;
            int     midVal = arr [mid];
            
            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }

        return -(low + 1);  // key not found
    }

    
}
