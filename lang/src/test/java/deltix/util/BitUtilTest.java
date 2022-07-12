package deltix.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexei Osipov
 */
public class BitUtilTest {
    @Test
    public void nextPowerOfTwo() throws Exception {
        assertEquals(64, BitUtil.nextPowerOfTwo(63));
        assertEquals(128, BitUtil.nextPowerOfTwo(100));
        assertEquals(128, BitUtil.nextPowerOfTwo(128));
    }

}