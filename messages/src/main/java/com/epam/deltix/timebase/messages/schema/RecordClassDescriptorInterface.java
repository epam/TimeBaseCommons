package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Schema definition for a class.
 */
public interface RecordClassDescriptorInterface extends RecordClassDescriptorInfo, ClassDescriptorInterface {
  /**
   * Defines parent RecordClassDescriptor if exist.
   * @param value - Parent
   */
  void setParent(RecordClassDescriptorInfo value);

  /**
   * Defines parent RecordClassDescriptor if exist.
   */
  void nullifyParent();

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @param value - Is Abstract
   */
  void setIsAbstract(boolean value);

  /**
   * Defines if current RecordClassDescriptor is abstract.
   */
  void nullifyIsAbstract();

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @param value - Is Content Class
   */
  void setIsContentClass(boolean value);

  /**
   * Defines if current RecordClassDescriptor is content class.
   */
  void nullifyIsContentClass();

  /**
   * List of fields of a class.
   * @param value - Data Fields
   */
  void setDataFields(ObjectArrayList<DataFieldInfo> value);

  /**
   * List of fields of a class.
   */
  void nullifyDataFields();

  /**
   * Method nullifies all instance properties
   */
  @Override
  RecordClassDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  RecordClassDescriptorInterface reset();

  @Override
  RecordClassDescriptorInterface copyFrom(RecordInfo template);
}
