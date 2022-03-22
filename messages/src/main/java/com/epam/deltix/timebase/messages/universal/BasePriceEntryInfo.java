package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.dfp.Decimal;

/**
 * This is base class for price entry.
 */
public interface BasePriceEntryInfo extends BaseEntryInfo {
  /**
   * Ask, Bid or Trade price.
   * @return Price
   */
  @Decimal
  long getPrice();

  /**
   * Ask, Bid or Trade price.
   * @return true if Price is not null
   */
  boolean hasPrice();

  /**
   * Ask, Bid or Trade quantity.
   * @return Size
   */
  long getSize();

  /**
   * Ask, Bid or Trade quantity.
   * @return true if Size is not null
   */
  boolean hasSize();

  /**
   * Numbers of orders.
   * @return Number Of Orders
   */
  long getNumberOfOrders();

  /**
   * Numbers of orders.
   * @return true if Number Of Orders is not null
   */
  boolean hasNumberOfOrders();

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @return Quote ID
   */
  CharSequence getQuoteId();

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @return true if Quote ID is not null
   */
  boolean hasQuoteId();

  /**
   * Id of participant (or broker ID).
   * @return Participant
   */
  CharSequence getParticipantId();

  /**
   * Id of participant (or broker ID).
   * @return true if Participant is not null
   */
  boolean hasParticipantId();

  /**
   * Method copies state to a given instance
   */
  @Override
  BasePriceEntryInfo clone();
}
