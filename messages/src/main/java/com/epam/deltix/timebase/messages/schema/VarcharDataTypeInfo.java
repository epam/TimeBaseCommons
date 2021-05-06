package com.epam.deltix.timebase.messages.schema;

import java.lang.Override;

/**
 * Schema definition of varchar data type.
 */
public interface VarcharDataTypeInfo extends DataTypeInfo {
  /**
   * True, if field allows line breaks.
   * @return Is Multiline
   */
  boolean isMultiline();

  /**
   * True, if field allows line breaks.
   * @return true if Is Multiline is not null
   */
  boolean hasIsMultiline();

  /**
   * Encoding type.
   * @return Encoding Type
   */
  int getEncodingType();

  /**
   * Encoding type.
   * @return true if Encoding Type is not null
   */
  boolean hasEncodingType();

  /**
   * Length.
   * @return Length
   */
  int getLength();

  /**
   * Length.
   * @return true if Length is not null
   */
  boolean hasLength();

  /**
   * Method copies state to a given instance
   */
  @Override
  VarcharDataTypeInfo clone();
}
