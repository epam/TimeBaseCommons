package com.epam.deltix.timebase.messages.universal;

/**
 * This entry used for empty snapshot indication. It is necessary because PackageHeader has no field exchangeId.
 * You should use this entry only for empty snapshot (both sides are empty) case.
 */
public interface BookResetEntryInfo extends BaseEntryInfo {
  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
  QuoteSide getSide();

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return true if Side is not null
   */
  boolean hasSide();

  /**
   * Data Model Type to identify what book we should reset.
   * @return Model Type
   */
  DataModelType getModelType();

  /**
   * Data Model Type to identify what book we should reset.
   * @return true if Model Type is not null
   */
  boolean hasModelType();

  /**
   * Method copies state to a given instance
   */
  @Override
  BookResetEntryInfo clone();
}
