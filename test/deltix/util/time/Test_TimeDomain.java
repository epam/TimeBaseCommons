package deltix.util.time;

/*  ##UTILS## */

import org.junit.*;
import static org.junit.Assert.*;

public class Test_TimeDomain {
    private TimeDomain     td;
    
    @Before
    public void     setUp () {
        td = new TimeDomain ();

        td.add (10, 20);
        td.add (15, 25);

        td.add (35, 45);

        td.add (35, 45);// repeat

        td.add (35, 46);

        td.add (34, 46);

        System.out.println (td);
    }
    
    @Test
    public void     spotCheck () {
        assertTrue (td.includes (34));
        assertTrue (td.includes (10));
        assertTrue (td.includes (19));
        assertFalse (td.includes (25));
    }    
    
    @Test
    public void     testXForm () {
        td.buildIndex ();

        for (int ii = 0; ii < 50; ii++) {
            long    o = td.globalToOpen (ii, false);

            if (o == TimeDomain.TIME_EXCLUDED) {
                System.out.println (ii + " -> X");
                continue;
            }

            System.out.println (ii + " -> " + o);

            long    t = td.openToGlobal (o);

            assertEquals (ii, t);
        }
    }
}
