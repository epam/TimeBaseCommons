package deltix.util.collections;

import java.util.*;

import deltix.util.lang.*;

public class CollectionUtil {

    public static <T> void remove (Collection<T> collection,
                                   Filter<T> filter) {
        ArrayList<T> r = new ArrayList<T> ();
        for (T o : collection) {
            if (filter.accept (o))
                r.add (o);
        }

        for (T o : r) {
            collection.remove (o);
        }
    }
}
