package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * This is a base class for schema data field.
 */
public interface DataFieldInterface extends DataFieldInfo, NamedDescriptorInterface {
  /**
   * Data type of a field.
   * @param value - Data Type
   */
  void setDataType(DataTypeInfo value);

  /**
   * Data type of a field.
   */
  void nullifyDataType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  DataFieldInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  DataFieldInterface reset();

  @Override
  DataFieldInterface copyFrom(RecordInfo template);
}
