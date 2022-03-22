package com.epam.deltix.timebase.messages.universal;

/**
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
public interface L3EntryUpdateInfo extends BasePriceEntryInfo {
  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @return Action
   */
  QuoteUpdateAction getAction();

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @return true if Action is not null
   */
  boolean hasAction();

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
   * Method copies state to a given instance
   */
  @Override
  L3EntryUpdateInfo clone();
}
