package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import java.lang.Override;

/**
 * Schema definition of enum data type.
 */
public interface EnumDataTypeInterface extends EnumDataTypeInfo, DataTypeInterface {
  /**
   * Definition for enumeration class.
   * @param value - Type Descriptor
   */
  void setTypeDescriptor(ClassDescriptorRefInfo value);

  /**
   * Definition for enumeration class.
   */
  void nullifyTypeDescriptor();

  /**
   * Method nullifies all instance properties
   */
  @Override
  EnumDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  EnumDataTypeInterface reset();

  @Override
  EnumDataTypeInterface copyFrom(RecordInfo template);
}
