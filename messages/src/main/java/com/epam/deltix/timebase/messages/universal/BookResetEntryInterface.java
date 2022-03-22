package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * This entry used for empty snapshot indication. It is necessary because PackageHeader has no field exchangeId.
 * You should use this entry only for empty snapshot (both sides are empty) case.
 */
public interface BookResetEntryInterface extends BookResetEntryInfo, BaseEntryInterface {
  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @param value - Side
   */
  void setSide(QuoteSide value);

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  void nullifySide();

  /**
   * Data Model Type to identify what book we should reset.
   * @param value - Model Type
   */
  void setModelType(DataModelType value);

  /**
   * Data Model Type to identify what book we should reset.
   */
  void nullifyModelType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BookResetEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BookResetEntryInterface reset();

  @Override
  BookResetEntryInterface copyFrom(RecordInfo template);
}
