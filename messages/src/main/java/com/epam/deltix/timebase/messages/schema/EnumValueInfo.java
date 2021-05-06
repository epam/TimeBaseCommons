package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for a single enumeration value.
 */
public interface EnumValueInfo extends RecordInfo {
  /**
   * Enumeration symbol.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Enumeration symbol.
   * @return true if Symbol is not null
   */
  boolean hasSymbol();

  /**
   * Enumeration value.
   * @return Value
   */
  short getValue();

  /**
   * Enumeration value.
   * @return true if Value is not null
   */
  boolean hasValue();

  /**
   * Method copies state to a given instance
   */
  @Override
  EnumValueInfo clone();
}
