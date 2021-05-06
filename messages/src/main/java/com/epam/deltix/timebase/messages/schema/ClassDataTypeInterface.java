package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Schema definition of class data type.
 */
public interface ClassDataTypeInterface extends ClassDataTypeInfo, DataTypeInterface {
  /**
   * Definitions for nested schema classes.
   * @param value - Type Descriptors
   */
  void setTypeDescriptors(ObjectArrayList<ClassDescriptorRefInfo> value);

  /**
   * Definitions for nested schema classes.
   */
  void nullifyTypeDescriptors();

  /**
   * Method nullifies all instance properties
   */
  @Override
  ClassDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  ClassDataTypeInterface reset();

  @Override
  ClassDataTypeInterface copyFrom(RecordInfo template);
}
