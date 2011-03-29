package deltix.util.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import deltix.util.lang.Filter;
import deltix.util.lang.StringUtils;
import deltix.util.lang.Util;

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

    public static String toString (Collection<?> collection, String separator) {
        return toString (collection, "", "", separator);
    }

    public static String toString (Collection<?> collection, String head, String tail, String separator) {
        if (collection == null || collection.isEmpty ())
            return head + tail;

        StringBuilder builder = new StringBuilder (128);
        builder.append (head);
        for (Object value : collection)
            builder.append (value).append (separator);
        builder.setLength (builder.length () - separator.length ());
        builder.append (tail);
        return builder.toString ();
    }

    public static <T extends Enum<T>> EnumSet<T> toEnumSet (Class<T> elementType,
                                                            String... elements) throws IllegalArgumentException {
        if (elements == null || elements.length <= 0)
            return EnumSet.noneOf (elementType);

        EnumSet<T> result = EnumSet.noneOf (elementType);
        for (String element : elements) {
            String elementStr = StringUtils.trim (element);
            if (elementStr != null)
                result.add (Enum.valueOf (elementType, elementStr));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] remove (T[] elementData, T element, Class<T> clazz) {
        for (int index = 0; index < elementData.length; index++)
            if (Util.xequals (element, elementData[index])) {
                int numMoved = elementData.length - index - 1;
                if (numMoved >= 0) {
                    T[] result = (T[]) Array.newInstance (clazz, elementData.length - 1);
                    System.arraycopy (elementData, 0, result, 0, index);
                    System.arraycopy (elementData, index + 1, result, index, numMoved);
                    return result;
                }
            }
        return elementData;
    }

    public static int[] toPrimitiveArray(Collection<Integer> values) {
        return (int[]) toArray(values, int.class);
    }

    private static Object toArray(Collection<?> values, Class<?> componentType) {
        Object array = Array.newInstance(componentType, values.size());
        int count = 0;
        for (Object value : values)
            Array.set(array, count++, value);
        return array;
    }
}
