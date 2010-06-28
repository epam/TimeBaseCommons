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

    public static String toString(Collection<?> collection, String separator) {
        if (collection == null || collection.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder(128);
        builder.append("[");
        for (Object value : collection)
            builder.append(value).append(separator);
        builder.setLength(builder.length() - separator.length());
        builder.append("]");
        return builder.toString();
    }
}
