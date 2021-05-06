package com.epam.deltix.timebase.messages.schema;

import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for non-static data field.
 */
public interface NonStaticDataFieldInfo extends DataFieldInfo {
  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @return Is Primary Key
   */
  boolean isPrimaryKey();

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @return true if Is Primary Key is not null
   */
  boolean hasIsPrimaryKey();

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @return Relative To
   */
  CharSequence getRelativeTo();

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @return true if Relative To is not null
   */
  boolean hasRelativeTo();

  /**
   * Method copies state to a given instance
   */
  @Override
  NonStaticDataFieldInfo clone();
}
