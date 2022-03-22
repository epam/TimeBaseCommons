package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
public interface L3EntryUpdateInterface extends L3EntryUpdateInfo, BasePriceEntryInterface {
  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @param value - Action
   */
  void setAction(QuoteUpdateAction value);

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   */
  void nullifyAction();

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
  L3EntryUpdateInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  L3EntryUpdateInterface reset();

  @Override
  L3EntryUpdateInterface copyFrom(RecordInfo template);
}
