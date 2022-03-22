package com.epam.deltix.timebase.messages.universal;

/**
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
public interface L3EntryNewInfo extends BasePriceEntryInfo {
  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
  QuoteSide getSide();

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return true if Side is not null
   */
  boolean hasSide();

  /**
   * Insert type. Add front or Add back.
   * @return Insert Type
   */
  InsertType getInsertType();

  /**
   * Insert type. Add front or Add back.
   * @return true if Insert Type is not null
   */
  boolean hasInsertType();

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @return Insert Before Quote Id
   */
  CharSequence getInsertBeforeQuoteId();

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @return true if Insert Before Quote Id is not null
   */
  boolean hasInsertBeforeQuoteId();

  /**
   * Method copies state to a given instance
   */
  @Override
  L3EntryNewInfo clone();
}
