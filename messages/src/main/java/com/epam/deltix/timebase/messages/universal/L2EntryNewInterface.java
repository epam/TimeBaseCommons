package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Reports incremental L2-updates: insert, delete or update of one line in Order Book either on ask or bid side.
 * It also can encode L2-snapshot entry. Note L2 is level oriented depth-of-the-book format and should be used
 * whenever price or integer index is used to locate book changes. It does support individual quotes book of
 * arbitrary depth. But if incremental changes key is a quoteId L3Entry should be used instead.
 */
public interface L2EntryNewInterface extends L2EntryNewInfo, BasePriceEntryInterface {
  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   * @param value - Level Index
   */
  void setLevel(short value);

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   */
  void nullifyLevel();

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @param value - Side
   */
  void setSide(QuoteSide value);

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  void nullifySide();

  /**
   * Method nullifies all instance properties
   */
  @Override
  L2EntryNewInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  L2EntryNewInterface reset();

  @Override
  L2EntryNewInterface copyFrom(RecordInfo template);
}
