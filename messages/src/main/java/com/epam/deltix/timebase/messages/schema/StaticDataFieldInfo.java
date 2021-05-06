package com.epam.deltix.timebase.messages.schema;

import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for static data field.
 */
public interface StaticDataFieldInfo extends DataFieldInfo {
  /**
   * Value constant for a field.
   * @return Static Value
   */
  CharSequence getStaticValue();

  /**
   * Value constant for a field.
   * @return true if Static Value is not null
   */
  boolean hasStaticValue();

  /**
   * Method copies state to a given instance
   */
  @Override
  StaticDataFieldInfo clone();
}
