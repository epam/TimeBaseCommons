package com.epam.deltix.util.lang;

import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Andy
 *         Date: 6/4/12 11:15 AM
 */
public class Test_Util {

    @Test
    public void testArrayAddObjectArgument () {
        String[] input = {"a", "b", "c"};
        String[] output = Util.arrayadd(input, "d");
        assertEquals("[a, b, c, d]", Arrays.toString(output));
    }
}
