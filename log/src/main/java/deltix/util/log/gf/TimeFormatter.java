package deltix.util.log.gf;

import static deltix.util.log.gf.DateFormatter.DAY_MS;


class TimeFormatter {

    protected static final int SECOND_MILLIS = 1000;
    protected static final int MINUTE_MILLIS = SECOND_MILLIS * 60;
    protected static final int HOUR_MILLIS = MINUTE_MILLIS * 60;

    public static void appendTime(long timestamp, StringBuilder builder) {
        int days = (int) (timestamp / DAY_MS);

        int millis = (int) (timestamp - days * DAY_MS);

        if (millis < 0) {
            millis += DAY_MS;
        }

        int hour = millis / HOUR_MILLIS;
        millis -= hour * HOUR_MILLIS;

        int minute = millis / MINUTE_MILLIS;
        millis -= minute * MINUTE_MILLIS;

        int second = millis / SECOND_MILLIS;
        millis -= second * SECOND_MILLIS;

        IntFormatter.format2DigitUInt(hour, builder);
        builder.append(':');

        IntFormatter.format2DigitUInt(minute, builder);
        builder.append(':');

        IntFormatter.format2DigitUInt(second, builder);
        builder.append('.');

        IntFormatter.format3DigitUInt(millis, builder);
    }

}
