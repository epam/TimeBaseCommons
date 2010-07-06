package deltix.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import deltix.util.lang.Filter;
import deltix.util.lang.StringUtils;

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

    public static <T extends Enum<T>> EnumSet<T> toEnumSet(Class<T> elementType,
                                                           String... elements) throws IllegalArgumentException {
        if (elements == null || elements.length <= 0)
            return EnumSet.noneOf(elementType);

        EnumSet<T> result = EnumSet.noneOf(elementType);
        for (String element : elements) {
            String elementStr = StringUtils.trim(element);
            if (elementStr != null)
                result.add(Enum.valueOf(elementType, elementStr));
        }
        return result;
    }
}
