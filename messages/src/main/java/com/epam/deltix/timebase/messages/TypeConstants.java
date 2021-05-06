package com.epam.deltix.timebase.messages;

import com.epam.deltix.dfp.Decimal;
import com.epam.deltix.dfp.Decimal64Utils;
import java.lang.String;

/**
 */
public class TypeConstants {
  public static final String CLASS_NAME = TypeConstants.class.getName();

  /**
   */
  public static final byte BOOLEAN_NULL = -1;

  /**
   */
  public static final byte BOOLEAN_FALSE = 0;

  /**
   */
  public static final byte BOOLEAN_TRUE = 1;

  /**
   */
  public static final byte INT8_NULL = Byte.MIN_VALUE;

  /**
   */
  public static final short INT16_NULL = Short.MIN_VALUE;

  /**
   */
  public static final int INT32_NULL = Integer.MIN_VALUE;

  /**
   */
  public static final long INT48_NULL = -140737488355328L;

  /**
   */
  public static final long INT64_NULL = Long.MIN_VALUE;

  /**
   */
  public static final int PINTERVAL_NULL = 0;

  /**
   */
  public static final long EXCHANGE_NULL = TypeConstants.INT64_NULL;

  /**
   */
  public static final long EXCHANGE_MAX_LEN = 10L;

  /**
   */
  public static final float IEEE32_NULL = Float.NaN;

  /**
   */
  public static final double IEEE64_NULL = Double.NaN;

  /**
   */
  @Decimal
  public static final long DECIMAL_NULL = Decimal64Utils.NULL;

  /**
   */
  public static final int TIME_OF_DAY_NULL = -1;

  /**
   */
  public static final long DURATION_NULL = Integer.MIN_VALUE;

  /**
   */
  public static final long TIMESTAMP_UNKNOWN = Long.MIN_VALUE;

  /**
   */
  public static final long TIMESTAMP_NULL = Long.MIN_VALUE;

  /**
   */
  public static final int TIMESTAMP_MAX_LEN = 10;

  /**
   */
  public static final int BINARY_UNLIMITED_SIZE = Integer.MIN_VALUE;

  /**
   */
  public static final int BINARY_MIN_COMPRESSION = 0;

  /**
   */
  public static final int BINARY_MAX_COMPRESSION = 9;

  /**
   */
  public static final int ENUM_NULL = -1;

  /**
   */
  public static final long ENUM_NULL_CODE = -1L;

  /**
   */
  public static final long ALPHANUMERIC_NULL = Long.MIN_VALUE;
}
