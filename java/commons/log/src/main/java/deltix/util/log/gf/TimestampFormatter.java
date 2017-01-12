package deltix.util.log.gf;

class TimestampFormatter {

    public static void appendTimestamp(long timestamp, StringBuilder builder) {
        DateFormatter.appendDate(timestamp, builder);
        builder.append('T');
        TimeFormatter.appendTime(timestamp, builder);
    }

}
