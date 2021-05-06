package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a single enumeration value.
 */
public interface EnumValueInterface extends EnumValueInfo, RecordInterface {
  /**
   * Enumeration symbol.
   * @param value - Symbol
   */
  void setSymbol(CharSequence value);

  /**
   * Enumeration symbol.
   */
  void nullifySymbol();

  /**
   * Enumeration value.
   * @param value - Value
   */
  void setValue(short value);

  /**
   * Enumeration value.
   */
  void nullifyValue();

  /**
   * Method nullifies all instance properties
   */
  @Override
  EnumValueInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  EnumValueInterface reset();

  @Override
  EnumValueInterface copyFrom(RecordInfo template);
}
