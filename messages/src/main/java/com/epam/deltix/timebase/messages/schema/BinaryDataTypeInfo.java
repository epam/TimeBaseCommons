package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of binary data type.
 */
public interface BinaryDataTypeInfo extends DataTypeInfo {
  /**
   * Maximum field length in bytes.
   * @return Max Size
   */
  int getMaxSize();

  /**
   * Maximum field length in bytes.
   * @return true if Max Size is not null
   */
  boolean hasMaxSize();

  /**
   * Compression level for binary data.
   * @return Compression Level
   */
  short getCompressionLevel();

  /**
   * Compression level for binary data.
   * @return true if Compression Level is not null
   */
  boolean hasCompressionLevel();

  /**
   * Method copies state to a given instance
   */
  @Override
  BinaryDataTypeInfo clone();
}
