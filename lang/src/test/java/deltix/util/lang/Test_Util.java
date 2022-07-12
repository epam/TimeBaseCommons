package deltix.util.lang;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Andy
 *         Date: 6/4/12 11:15 AM
 */
@Tag("utils")
public class Test_Util {

    @Test
    public void testArrayAddObjectArgument () {
        String[] input = {"a", "b", "c"};
        String[] output = Util.arrayadd(input, "d");
        assertEquals("[a, b, c, d]", Arrays.toString(output));
    }
}
