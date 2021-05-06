package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema data types.
 */
public interface DataTypeInfo extends RecordInfo {
  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @return Encoding
   */
  CharSequence getEncoding();

  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @return true if Encoding is not null
   */
  boolean hasEncoding();

  /**
   * True, if schema design allows the field to be nullable.
   * @return Is Nullable
   */
  boolean isNullable();

  /**
   * True, if schema design allows the field to be nullable.
   * @return true if Is Nullable is not null
   */
  boolean hasIsNullable();

  /**
   * Method copies state to a given instance
   */
  @Override
  DataTypeInfo clone();
}
