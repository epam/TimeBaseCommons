package deltix.util.memory;

import org.junit.*;
import static org.junit.Assert.*;

public class Test_MemoryDataInputOutput {
    private MemoryDataOutput        out;
    private MemoryDataInput         in;
    
        
    @Before
    public void     setUp () {
        out = new MemoryDataOutput (1); 
        in = new MemoryDataInput ();
    }
    
    @Test
    public void     testPackedUnsignedLong () {
        out.reset ();
        
        final long []                 testValues = {
            0, 1, 0x1F, 0x20, 0x1FFF, 0x2000, 0x1F000000000000L, 0x1F00000000000000L
        };
        
        for (long v : testValues)
            out.writePackedUnsignedLong (v);
        
        assertEquals (out.getSize (), 1+1+1+2+2+3+7+8);
        
        in.setBytes (out);
        
        for (long v : testValues) {
            long        actual = in.readPackedUnsignedLong ();
            assertEquals (v, actual);
        }
    }
    
    @Test
    public void     testPackedUnsignedInt () {
        out.reset ();
        
        final int []                 testValues = {
            0, 1, 0x3F, 0x40, 0x3FFF, 0x4000
        };
        
        for (int v : testValues) 
            out.writePackedUnsignedInt (v);
        
        assertEquals (out.getSize (), 1+1+1+2+2+3);
        
        in.setBytes (out);
        
        for (int v : testValues) {
            int        actual = in.readPackedUnsignedInt ();
            assertEquals (v, actual);
        }
    }
    
    @Test
    public void     testLong48 () {
        out.reset ();
        
        final long []                 testValues = {
            1216850074278L, -1216850074278L,
            -62170156800000L, 95614819200000L            
        };
        
        for (long v : testValues)
            out.writeLong48 (v);
        
        assertEquals (out.getSize (), 6 * 4);
        
        in.setBytes (out);
        
        for (long v : testValues) {
            long        actual = in.readLong48 ();
            assertEquals (v, actual);
        }
    }
    
    @Test
    public void     testScaledDouble () {
        final double []                 testValues = {
            0, 0.7, -0.2, 2.28, -997.82, 
            666.234876, -234876747.6678,
            0.23476890879672543765, //out of exp range
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY
        };
        
        out.reset ();
        
        for (double v : testValues) 
            out.writeScaledDouble (v);
        
        in.setBytes (out);
        
        for (double v : testValues) {
            double        actual = in.readScaledDouble ();
            
            if (Double.isNaN (v))
                assertTrue ("!Double.isNaN (" + actual + ")", Double.isNaN (actual));
            else
                assertEquals (v, actual);
        }
        
        out.reset ();
        
        for (double v : testValues) 
            out.writeScaledDouble (v, 3);
        
        in.setBytes (out);
        
        for (double v : testValues) {
            double          read = in.readScaledDouble ();
            
            if (Double.isNaN (v))
                assertTrue ("!Double.isNaN (" + read + ")", Double.isNaN (read));
            else {
                long        actual = Math.round (read * 1000);
                long        expected = Math.round (v * 1000);

                assertEquals (expected, actual);
            }
        }
    }
}
