package com.epam.deltix.timebase.messages.universal;

/**
 * This class may represent both exchange-local top of the book (BBO) as well as National Best Bid Offer (NBBO).
 * You can use method {getIsNational()} to filter out NBBO messages.
 * This is always a one side quote, unlike old BestBidOfferMessage which is two-side (with nullable properties).
 */
public interface L1EntryInfo extends BasePriceEntryInfo {
  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @return Is National
   */
  boolean isNational();

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @return true if Is National is not null
   */
  boolean hasIsNational();

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
  L1EntryInfo clone();
}
