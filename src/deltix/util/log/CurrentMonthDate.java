package deltix.util.log;

import deltix.util.lang.Util;

import java.util.TimerTask;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

/**
 * Used by TerseFormatter to log date. Usage:
 * <pre>
 * CurrentMonthDate cmd = CurrentMonthDate.getInstance();
 * String date = cmd.getDayMonth(timestamp);
 * </pre>
 * @author Andy
*/
public class CurrentMonthDate extends TimerTask {
    private final static long MILLISECONDS_IN_DAY 	= TimeUnit.DAYS.toMillis(1);
    private final static Logger LOGGER = Logger.getLogger (CurrentMonthDate.class.getName());

    private final String [] MONTH_CODES = new SimpleDateFormat().getDateFormatSymbols().getShortMonths();
    private final Calendar c = Calendar.getInstance();

    private volatile String currentMonthDay;
    private volatile long goodUntil;

    private static final CurrentMonthDate INSTANCE = create();

    private CurrentMonthDate() {
        roll (System.currentTimeMillis());
    }

    private static CurrentMonthDate create () {
        final long now = System.currentTimeMillis();
        CurrentMonthDate result = new CurrentMonthDate();
        Util.GLOBAL_TIMER.scheduleAtFixedRate(result, result.goodUntil - now, MILLISECONDS_IN_DAY);
        return result;
    }


    public static CurrentMonthDate getInstance () {
        return INSTANCE;
    }

    /** @return Date and Month of given timestamp.
     * Method is slow when it is called at exactly midnight, or if timestamp represent previous day or some moment in the future. */
    public String getDayMonth (long timestamp) {

        final long goodUntilCopy = goodUntil; // volatile
        if (timestamp < goodUntilCopy && timestamp > goodUntilCopy - MILLISECONDS_IN_DAY)
            return currentMonthDay;
        else
            return slowFormat (timestamp);
    }

    private synchronized String slowFormat (long timestamp) {
        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ' ' + MONTH_CODES[c.get(Calendar.MONTH)];
    }

    private synchronized void roll (long timestamp) {
        assert timestamp >= goodUntil;

        c.setTimeInMillis(timestamp);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        final long nextMidnight = c.getTimeInMillis() + MILLISECONDS_IN_DAY;

        currentMonthDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ' ' + MONTH_CODES[c.get(Calendar.MONTH)];
        goodUntil = nextMidnight;
    }



    @Override
    public void run() {
        try {
            // runs at midnight - optional taks that rolls currentMonthDay forward to avoid doing it during logging (if possible)
            roll (System.currentTimeMillis());
        } catch (Throwable e) {
            LOGGER.log (Level.SEVERE, e.getMessage(), e);
        }
    }
}
