package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for static data field.
 */
public interface StaticDataFieldInterface extends StaticDataFieldInfo, DataFieldInterface {
  /**
   * Value constant for a field.
   * @param value - Static Value
   */
  void setStaticValue(CharSequence value);

  /**
   * Value constant for a field.
   */
  void nullifyStaticValue();

  /**
   * Method nullifies all instance properties
   */
  @Override
  StaticDataFieldInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  StaticDataFieldInterface reset();

  @Override
  StaticDataFieldInterface copyFrom(RecordInfo template);
}
