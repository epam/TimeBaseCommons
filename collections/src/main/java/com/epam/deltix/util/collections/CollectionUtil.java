/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.collections;

import com.epam.deltix.util.collections.generated.*;
import com.epam.deltix.util.lang.Filter;
import com.epam.deltix.util.lang.StringUtils;
import com.epam.deltix.util.lang.Transformer;
import com.epam.deltix.util.lang.Util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class CollectionUtil {

    public static <T> void remove(Collection<T> collection,
                                  Filter<T> filter) {
        ArrayList<T> r = new ArrayList<T>();
        for (T o : collection) {
            if (filter.accept(o))
                r.add(o);
        }

        for (T o : r) {
            collection.remove(o);
        }
    }

    public static String toString(Collection<?> collection, String separator) {
        return toString(collection, "", "", separator);
    }

    public static String toString(Collection<?> collection, String head, String tail, String separator) {
        if (collection == null || collection.isEmpty())
            return head + tail;

        StringBuilder builder = new StringBuilder(128);
        builder.append(head);
        for (Object value : collection)
            builder.append(value).append(separator);
        builder.setLength(builder.length() - separator.length());
        builder.append(tail);
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

    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] elementData, T element, Class<T> clazz) {
        for (int index = 0; index < elementData.length; index++)
            if (Util.xequals(element, elementData[index])) {
                int numMoved = elementData.length - index - 1;
                if (numMoved >= 0) {
                    T[] result = (T[]) Array.newInstance(clazz, elementData.length - 1);
                    System.arraycopy(elementData, 0, result, 0, index);
                    System.arraycopy(elementData, index + 1, result, index, numMoved);
                    return result;
                }
            }
        return elementData;
    }

    public static int[] toPrimitiveArray(Collection<Integer> values) {
        return (int[]) toArray(values, int.class);
    }

    public static <T, E> Set<T> convertToSet(Collection<E> values, Transformer<T, ? super E> transformer) {
        Set<T> result = new HashSet<T>(values.size());
        for (E value : values)
            result.add(transformer.transform(value));
        return result;
    }

    public static <T, E> Map<T, E> convertToMap(Collection<E> values, Transformer<T, ? super E> transformer) {
        Map<T, E> result = new HashMap<T, E>(values.size());
        for (E value : values)
            result.put(transformer.transform(value), value);
        return result;
    }

    private static Object toArray(Collection<?> values, Class<?> componentType) {
        Object array = Array.newInstance(componentType, values.size());
        int count = 0;
        for (Object value : values)
            Array.set(array, count++, value);
        return array;
    }

    // region hashCode

    public static <T> int hashCode(final ObjectList<T> list,
                                   final ToIntFunction<? super T> elementHashFunction) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final T object = list.getObjectNoRangeCheck(i);
                hash = 31 * hash + elementHashFunction.applyAsInt(object);
            }
        }

        return hash;
    }

    public static int hashCode(final ObjectList<?> list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final Object object = list.getObjectNoRangeCheck(i);
                hash = 31 * hash + Objects.hashCode(object);
            }
        }

        return hash;
    }

    public static int hashCode(final LongList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final long value = list.getLongNoRangeCheck(i);
                hash = 31 * hash + Long.hashCode(value);
            }
        }

        return hash;
    }

    public static int hashCode(final IntegerList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final int value = list.getIntegerNoRangeCheck(i);
                hash = 31 * hash + Integer.hashCode(value);
            }
        }

        return hash;
    }

    public static int hashCode(final ShortList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final short value = list.getShortNoRangeCheck(i);
                hash = 31 * hash + Short.hashCode(value);
            }
        }

        return hash;
    }

    public static int hashCode(final ByteList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final byte value = list.getByteNoRangeCheck(i);
                hash = 31 * hash + Byte.hashCode(value);
            }
        }

        return hash;
    }

    public static int hashCode(final CharacterList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final char value = list.getCharacterNoRangeCheck(i);
                hash = 31 * hash + Character.hashCode(value);
            }
        }

        return hash;
    }

    public static int hashCode(final BooleanList list) {
        int hash = 0;

        if (list != null) {
            for (int i = 0, size = list.size(); i < size; i++) {
                final boolean value = list.getBooleanNoRangeCheck(i);
                hash = 31 * hash + Boolean.hashCode(value);
            }
        }

        return hash;
    }

    // endregion

    // region equals

    public static <T> boolean equals(final ObjectList<T> lhs,
                                     final ObjectList<T> rhs,
                                     final BiPredicate<? super T, ? super T> elementEqualsFunction) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!elementEqualsFunction.test(lhs.getObjectNoRangeCheck(i), rhs.getObjectNoRangeCheck(i))) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean equals(final ObjectList<T> lhs, final ObjectList<T> rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(lhs.getObjectNoRangeCheck(i), rhs.getObjectNoRangeCheck(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final LongList lhs, final LongList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getLongNoRangeCheck(i) != rhs.getLongNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final IntegerList lhs, final IntegerList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getIntegerNoRangeCheck(i) != rhs.getIntegerNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final ShortList lhs, final ShortList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getShortNoRangeCheck(i) != rhs.getShortNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final ByteList lhs, final ByteList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getByteNoRangeCheck(i) != rhs.getByteNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final CharacterList lhs, final CharacterList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getCharacterNoRangeCheck(i) != rhs.getCharacterNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    public static boolean equals(final BooleanArrayList lhs, final BooleanArrayList rhs) {
        if (lhs == rhs) {
            return true;
        }

        if (lhs == null || rhs == null) {
            return false;
        }

        final int size = lhs.size();
        if (size != rhs.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (lhs.getBooleanNoRangeCheck(i) != rhs.getBooleanNoRangeCheck(i)) {
                return false;
            }
        }

        return true;
    }

    // endregion

    // region copy

    @SuppressWarnings("unchecked")
    public static <T> ObjectArrayList<T> copy(final ObjectList<T> list,
                                              final Function<? super T, ? extends T> elementCopyFunction) {
        if (list == null) {
            return null;
        }

        final int size = list.size();
        final ObjectArrayList<T> copy = new ObjectArrayList<>(size);

        for (int i = 0; i < size; i++) {
            final T element = list.getObjectNoRangeCheck(i);
            final T elementCopy = (element == null) ? null : elementCopyFunction.apply(element);

            copy.add(elementCopy);
        }

        return copy;
    }

    public static LongArrayList copy(final LongList list) {
        return (list == null) ? null : new LongArrayList(list);
    }

    public static IntegerArrayList copy(final IntegerList list) {
        return (list == null) ? null : new IntegerArrayList(list);
    }

    public static ShortArrayList copy(final ShortList list) {
        return (list == null) ? null : new ShortArrayList(list);
    }

    public static ByteArrayList copy(final ByteList list) {
        return (list == null) ? null : new ByteArrayList(list);
    }

    public static CharacterArrayList copy(final CharacterList list) {
        return (list == null) ? null : new CharacterArrayList(list);
    }

    public static BooleanArrayList copy(final BooleanList list) {
        return (list == null) ? null : new BooleanArrayList(list);
    }

    // endregion

}