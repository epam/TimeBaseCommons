package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.dfp.Decimal;
import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * This is base class for price entry.
 */
public interface BasePriceEntryInterface extends BasePriceEntryInfo, BaseEntryInterface {
  /**
   * Ask, Bid or Trade price.
   * @param value - Price
   */
  void setPrice(@Decimal long value);

  /**
   * Ask, Bid or Trade price.
   */
  void nullifyPrice();

  /**
   * Ask, Bid or Trade quantity.
   * @param value - Size
   */
  void setSize(long value);

  /**
   * Ask, Bid or Trade quantity.
   */
  void nullifySize();

  /**
   * Numbers of orders.
   * @param value - Number Of Orders
   */
  void setNumberOfOrders(long value);

  /**
   * Numbers of orders.
   */
  void nullifyNumberOfOrders();

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @param value - Quote ID
   */
  void setQuoteId(CharSequence value);

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   */
  void nullifyQuoteId();

  /**
   * Id of participant (or broker ID).
   * @param value - Participant
   */
  void setParticipantId(CharSequence value);

  /**
   * Id of participant (or broker ID).
   */
  void nullifyParticipantId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BasePriceEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BasePriceEntryInterface reset();

  @Override
  BasePriceEntryInterface copyFrom(RecordInfo template);
}
