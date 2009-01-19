package deltix.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class Test_CharSequenceMaps {
    private StringBuilder           sb = new StringBuilder ();

    private CharSequence            wrap (String text) {
        sb.setLength (0);
        sb.append (text);
        return (sb);
    }

    @Test
    public void         testIntegerMap () {
        CharSequenceToIntegerMap    map = new CharSequenceToIntegerMap ();

        assertTrue (map.put (wrap ("MOON"), 456));
        assertTrue (map.put (wrap ("SUN"), 235));
        assertFalse (map.put (wrap ("MOON"), -9998));

        assertEquals (map.size (), 2);

        assertEquals (map.get (wrap ("MOON"), -1), -9998);
        assertEquals (map.get (wrap ("SUN"), -1), 235);

        assertEquals (map.remove (wrap ("MOON"), -1), -9998);
        assertEquals (map.size (), 1);
    }

    @Test
    public void         testObjectMap () {
        Object                      a = new Object ();
        Object                      b = new Object ();
        Object                      c = new Object ();

        CharSequenceToObjectMap <Object>    map = new CharSequenceToObjectMap <Object> ();

        assertEquals (null, map.put (wrap ("MOON"), a));
        assertEquals (null, map.put (wrap ("SUN"), b));
        assertEquals (a, map.put (wrap ("MOON"), c));
        assertEquals (map.size (), 2);
        assertEquals (c, map.get (wrap ("MOON")));
        assertEquals (b, map.get (wrap ("SUN")));
        assertEquals (c, map.remove (wrap ("MOON")));
        assertEquals (map.size (), 1);
    }
}
