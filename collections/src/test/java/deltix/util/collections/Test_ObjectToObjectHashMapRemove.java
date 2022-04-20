package deltix.util.collections;

import deltix.util.collections.generated.ObjectToObjectHashMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Test_ObjectToObjectHashMapRemove {
    private final ObjectToObjectHashMap<String,String> map = new ObjectToObjectHashMap<>();

    @Test
    public void testRemoveByKey () {
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        assertContent("one, two, three");

        map.remove("2");

        assertContent("one, three");

        map.remove("X");

        assertContent("one, three");
    }

    @Test
    public void testRemoveByValue () {
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        assertFalse(map.remove((value) -> value.equals("unknown"))); // no such value
        assertContent("one, two, three");

        assertTrue(map.remove((value) -> value.equals("two")));
        assertContent("one, three");

        assertTrue(map.remove((value) -> value.equals("three")));
        assertContent("one");

        assertFalse(map.remove((value) -> value.equals("three"))); // again?
        assertContent("one");

        assertTrue(map.remove((value) -> value.equals("one")));
        assertContent("");

        assertFalse(map.remove((value) -> value.equals("one"))); // again?
        assertContent("");
    }

    @Test
    public void testRemoveByValueWithCookie () {
        BiPredicate<String,String> filter = String::equals;

        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        assertFalse(map.remove(filter, "unknown")); // no such value
        assertContent("one, two, three");

        assertTrue(map.remove(filter, "two"));
        assertContent("one, three");

        assertTrue(map.remove(filter, "three"));
        assertContent("one");

        assertFalse(map.remove(filter, "three")); // again?
        assertContent("one");

        assertTrue(map.remove(filter, "one"));
        assertContent("");

        assertFalse(map.remove(filter, "one")); // again?
        assertContent("");
    }

    @Test
    public void testRemoveAllByValue () {
        map.put("1", "one");
        map.put("2", "two");
        map.put("3", "three");

        assertFalse(map.removeAll((value) -> value.equals("unknown"))); // no such value
        assertContent("one, two, three");

        assertTrue(map.removeAll((value) -> value.equals("two")));
        assertContent("one, three");

        assertTrue(map.removeAll((value) -> value.equals("three")));
        assertContent("one");

        assertFalse(map.removeAll((value) -> value.equals("three"))); // again?
        assertContent("one");

        assertTrue(map.removeAll((value) -> value.equals("one")));
        assertContent("");

        assertFalse(map.removeAll((value) -> value.equals("one"))); // again?
        assertContent("");

        map.put("1", "one");
        map.put("2", "two");
        map.put("2a", "two");
        map.put("2b", "two");
        map.put("3", "three");
        assertContent("one, two, two, two, three");

        assertTrue(map.removeAll((value) -> value.equals("two")));
        assertContent("one, three");
    }

    private void assertContent(String expectedDump) {
        String actualDump = dump();
        Assert.assertEquals(expectedDump, actualDump);
    }

    private String dump() {
        StringBuilder result = new StringBuilder();

        ElementsEnumeration<String> keys = map.keys();
        SortedSet<String> sortedKeys = new TreeSet<>();
        while(keys.hasMoreElements())
            sortedKeys.add(keys.nextElement());

        for (String key : sortedKeys) {
            if (result.length() > 0)
                result.append(", ");

            result.append(map.get(key, null));
        }
        return result.toString();
    }
}
