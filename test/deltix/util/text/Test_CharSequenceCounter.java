package deltix.util.text;

import org.junit.Assert;
import org.junit.Test;

/*  ##UTILS## */
public class Test_CharSequenceCounter {

    @Test
    public void test () {
        CharSequenceCounter counter = new CharSequenceCounter();
        for (int i = 0; i < 100000; i++) {
            String number = String.format("%012d", i);
            Assert.assertEquals(number, counter.toString());
            counter.increment();
        }
    }
}
