package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of varchar data type.
 */
public interface VarcharDataTypeInterface extends VarcharDataTypeInfo, DataTypeInterface {
  /**
   * True, if field allows line breaks.
   * @param value - Is Multiline
   */
  void setIsMultiline(boolean value);

  /**
   * True, if field allows line breaks.
   */
  void nullifyIsMultiline();

  /**
   * Encoding type.
   * @param value - Encoding Type
   */
  void setEncodingType(int value);

  /**
   * Encoding type.
   */
  void nullifyEncodingType();

  /**
   * Length.
   * @param value - Length
   */
  void setLength(int value);

  /**
   * Length.
   */
  void nullifyLength();

  /**
   * Method nullifies all instance properties
   */
  @Override
  VarcharDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  VarcharDataTypeInterface reset();

  @Override
  VarcharDataTypeInterface copyFrom(RecordInfo template);
}
