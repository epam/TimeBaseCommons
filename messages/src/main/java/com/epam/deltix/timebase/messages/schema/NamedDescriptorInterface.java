package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema nodes.
 */
public interface NamedDescriptorInterface extends NamedDescriptorInfo, RecordInterface {
  /**
   * Node name.
   * @param value - Name
   */
  void setName(CharSequence value);

  /**
   * Node name.
   */
  void nullifyName();

  /**
   * Node title.
   * @param value - Title
   */
  void setTitle(CharSequence value);

  /**
   * Node title.
   */
  void nullifyTitle();

  /**
   * Node description.
   * @param value - Description
   */
  void setDescription(CharSequence value);

  /**
   * Node description.
   */
  void nullifyDescription();

  /**
   * Method nullifies all instance properties
   */
  @Override
  NamedDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  NamedDescriptorInterface reset();

  @Override
  NamedDescriptorInterface copyFrom(RecordInfo template);
}
