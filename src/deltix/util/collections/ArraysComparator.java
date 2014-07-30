package deltix.util.collections;


import java.util.Collection;
import java.util.Iterator;

public abstract class ArraysComparator<A, B> {

    public abstract int compare(A e1, B e2);

    public int          compare(Collection<A> c1, Collection<B> c2) {
        if (c1 == null)
            return c2 == null ? 0 : -1;
        else if (c2 == null)
            return 1;

        if (c1.size() != c2.size())
            return c1.size() - c2.size() > 0 ? 1 : -1;

        Iterator<A> i1 = c1.iterator();
        Iterator<B> i2 = c2.iterator();

        while (i1.hasNext() && i2.hasNext()) {
            int result = compare(i1.next(), i2.next());
            if (result != 0)
                return result;
        }

        return 0;
    }

    public int          compare(A[] a1, B[] a2) {
        if (a1 == null)
            return a2 == null ? 0 : -1;
        else if (a2 == null)
            return 1;

        if (a1.length != a2.length)
            return a1.length - a2.length > 0 ? 1 : -1;

        for (int i = 0; i < a1.length; i++) {
            int result = compare(a1[i], a2[i]);
            if (result != 0)
                return result;
        }

        return 0;
    }
}
