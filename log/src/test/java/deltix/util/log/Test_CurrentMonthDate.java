package deltix.util.log;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;
import java.util.Calendar;

import deltix.util.JUnitCategories.Utils;
import org.junit.experimental.categories.Category;

/**
 * @author Andy
 *         Date: Feb 19, 2010 11:13:56 AM
 */
@Category(Utils.class)
public class Test_CurrentMonthDate {
    private final static long MILLISECONDS_IN_DAY 	= TimeUnit.DAYS.toMillis(1);

    CurrentMonthDate cmd = CurrentMonthDate.getInstance();

    @Test
    public void testMidday () {
        long time = 1266596280059L;
        assertEquals ("19 Feb", cmd.getDayMonth(time-1));
        assertEquals ("19 Feb", cmd.getDayMonth(time));
        assertEquals ("19 Feb", cmd.getDayMonth(time+1));

        assertEquals ("18 Feb", cmd.getDayMonth(time - MILLISECONDS_IN_DAY));
        assertEquals ("20 Feb", cmd.getDayMonth(time + MILLISECONDS_IN_DAY));

        assertEquals ("31 Jan", cmd.getDayMonth(time - 19*MILLISECONDS_IN_DAY));
        assertEquals ("1 Mar", cmd.getDayMonth(time + 10*MILLISECONDS_IN_DAY));
    }

    @Test
    public void testMidnight () {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();

        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long lastLocalMidnight = c.getTimeInMillis();

        
        assertEquals (cmd.getDayMonth(now), cmd.getDayMonth(lastLocalMidnight));
        assertEquals (cmd.getDayMonth(now + MILLISECONDS_IN_DAY), cmd.getDayMonth(lastLocalMidnight + MILLISECONDS_IN_DAY));
        assertEquals (cmd.getDayMonth(now - MILLISECONDS_IN_DAY), cmd.getDayMonth(lastLocalMidnight - MILLISECONDS_IN_DAY));


    }
}
