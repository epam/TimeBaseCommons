package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for non-static data field.
 */
public interface NonStaticDataFieldInterface extends NonStaticDataFieldInfo, DataFieldInterface {
  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @param value - Is Primary Key
   */
  void setIsPrimaryKey(boolean value);

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   */
  void nullifyIsPrimaryKey();

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @param value - Relative To
   */
  void setRelativeTo(CharSequence value);

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   */
  void nullifyRelativeTo();

  /**
   * Method nullifies all instance properties
   */
  @Override
  NonStaticDataFieldInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  NonStaticDataFieldInterface reset();

  @Override
  NonStaticDataFieldInterface copyFrom(RecordInfo template);
}
