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
        
        in.setBytes (out);
        
        for (long v : testValues) {
            long        actual = in.readLong48 ();
            assertEquals (v, actual);
        }
    }
}
