package deltix.util.lang;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author Andy
 *         Date: 12/17/13
 */
/*  ##UTILS## */
public class Test_StringUtils {

    @Test
    public void testCharSequenceIndexOf() {

        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "F"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "ABCDEF"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "ABDE"));

        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "A"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "AB"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABC"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABCD"));
        Assert.assertEquals(0, StringUtils.indexOf("ABCDE", "ABCDE"));

        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "B"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BC"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BCD"));
        Assert.assertEquals(1, StringUtils.indexOf("ABCDE", "BCDE"));
        Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", "BCDEF"));

        try {
            Assert.assertEquals(-1, StringUtils.indexOf("ABCDE", ""));
            Assert.fail ("Failed to detect empty string");
        } catch (StringIndexOutOfBoundsException expected) {
            //
        }
    }

}
