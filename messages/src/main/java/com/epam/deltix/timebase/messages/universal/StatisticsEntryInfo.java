package com.epam.deltix.timebase.messages.universal;

/**
 * Represents arbitrary exchange event.
 */
public interface StatisticsEntryInfo extends BaseEntryInfo {
  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return Type
   */
  StatisticsType getType();

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return true if Type is not null
   */
  boolean hasType();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return Value
   */
  long getValue();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return true if Value is not null
   */
  boolean hasValue();

  /**
   * Original event type, vendor specific.
   * @return OriginalType
   */
  CharSequence getOriginalType();

  /**
   * Original event type, vendor specific.
   * @return true if OriginalType is not null
   */
  boolean hasOriginalType();

  /**
   * Method copies state to a given instance
   */
  @Override
  StatisticsEntryInfo clone();
}
