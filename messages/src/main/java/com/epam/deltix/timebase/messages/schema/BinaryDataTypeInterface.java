package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of binary data type.
 */
public interface BinaryDataTypeInterface extends BinaryDataTypeInfo, DataTypeInterface {
  /**
   * Maximum field length in bytes.
   * @param value - Max Size
   */
  void setMaxSize(int value);

  /**
   * Maximum field length in bytes.
   */
  void nullifyMaxSize();

  /**
   * Compression level for binary data.
   * @param value - Compression Level
   */
  void setCompressionLevel(short value);

  /**
   * Compression level for binary data.
   */
  void nullifyCompressionLevel();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BinaryDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BinaryDataTypeInterface reset();

  @Override
  BinaryDataTypeInterface copyFrom(RecordInfo template);
}
