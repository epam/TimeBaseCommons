package com.epam.deltix.util.collections;


import java.util.Collection;
import java.util.Iterator;

public abstract class ArraysComparator<A, B> {

    public abstract int compare(A e1, B e2);

    public int          compare(Collection<A> a, Collection<B> b) {
        if (a == null)
            return b == null ? 0 : -1;
        else if (b == null)
            return 1;

        if (a.size() != b.size())
            return a.size() - b.size() > 0 ? 1 : -1;

        Iterator<A> i1 = a.iterator();
        Iterator<B> i2 = b.iterator();

        while (i1.hasNext() && i2.hasNext()) {
            int result = compare(i1.next(), i2.next());
            if (result != 0)
                return result;
        }

        return 0;
    }

    public int          compare(A[] a, B[] b) {
        if (a == null)
            return b == null ? 0 : -1;
        else if (b == null)
            return 1;

        if (a.length != b.length)
            return a.length - b.length > 0 ? 1 : -1;

        for (int i = 0; i < a.length; i++) {
            int result = compare(a[i], b[i]);
            if (result != 0)
                return result;
        }

        return 0;
    }
}
