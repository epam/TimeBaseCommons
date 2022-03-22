package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Represents arbitrary exchange event.
 */
public interface StatisticsEntryInterface extends StatisticsEntryInfo, BaseEntryInterface {
  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @param value - Type
   */
  void setType(StatisticsType value);

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   */
  void nullifyType();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @param value - Value
   */
  void setValue(long value);

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   */
  void nullifyValue();

  /**
   * Original event type, vendor specific.
   * @param value - OriginalType
   */
  void setOriginalType(CharSequence value);

  /**
   * Original event type, vendor specific.
   */
  void nullifyOriginalType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  StatisticsEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  StatisticsEntryInterface reset();

  @Override
  StatisticsEntryInterface copyFrom(RecordInfo template);
}
