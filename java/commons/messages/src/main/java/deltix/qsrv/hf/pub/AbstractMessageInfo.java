package deltix.qsrv.hf.pub;

/**
 *
 */
public interface AbstractMessageInfo extends RecordInfo{
  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   * @return Time
   */
  long getTimeStampMs();

  /**
   * Time in this field is measured in milliseconds that passed since January 1, 1970 UTC.
   * For inbound messages special constant {link TIMESTAMP_UNKNOWN} marks 'unknown' timestamp in which case TimeBase server stores message using current server time.
   * @return true if Timeis not null
   */
  boolean hasTimeStampMs();

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   * @return Nanoseconds Component
   */
  long getNanoTime();

  /**
   * Nanoseconds component of timestamp. Generally speaking value must be in range from [ 0 to 999999 ],
   * but system handles any value (nanoTime simply added to timestamp converted to nanoseconds).
   * @return true if Nanoseconds Componentis not null
   */
  boolean hasNanoTime();
}
