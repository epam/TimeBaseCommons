package deltix.util.collections;

import java.util.List;

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
}
