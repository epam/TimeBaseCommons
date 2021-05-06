package com.epam.deltix.util.memory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Alexei Osipov
 */
public class MemoryDataOutputTest {
    @Test
    public void writeLongBytes() throws Exception {
        MemoryDataOutput mdo = new MemoryDataOutput();
        int result = mdo.writeLongBytes(0xFF_00_FFL);
        assertEquals(3, result);
        assertEquals(3, mdo.getSize());

        int result2 = mdo.writeLongBytes(0xCAFE_BABEL);
        assertEquals(4, result2);
        assertEquals(7, mdo.getSize());
    }

}