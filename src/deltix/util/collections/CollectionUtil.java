package deltix.util.collections;

import java.util.*;

public class CollectionUtil {

    public static <T> void remove (Collection<T> collection,
                                   Condition<T> condition) {
        ArrayList<T> r = new ArrayList<T> ();
        for (T o : collection) {
            if (condition.check (o))
                r.add (o);
        }

        while (r.size () > 0) {
            collection.remove (r.get (0));
        }
    }

    public static interface Condition<T> {
        boolean check (T o);
    }

}
