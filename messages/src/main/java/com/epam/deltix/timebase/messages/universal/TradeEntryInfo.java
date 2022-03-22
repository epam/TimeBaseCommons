package com.epam.deltix.timebase.messages.universal;

/**
 * Basic information about a market trade.
 */
public interface TradeEntryInfo extends BaseEntryInfo {
  /**
   * Ask, Bid or Trade price.
   * @return Price
   */
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
   * Market specific trade condition.
   * @return Condition
   */
  CharSequence getCondition();

  /**
   * Market specific trade condition.
   * @return true if Condition is not null
   */
  boolean hasCondition();

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @return Trade Type
   */
  TradeType getTradeType();

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @return true if Trade Type is not null
   */
  boolean hasTradeType();

  /**
   * Seller number of orders involved in match.
   * @return Seller Number Of Orders
   */
  long getSellerNumberOfOrders();

  /**
   * Seller number of orders involved in match.
   * @return true if Seller Number Of Orders is not null
   */
  boolean hasSellerNumberOfOrders();

  /**
   * Buyer number of orders involved in match.
   * @return Buyer Number Of Orders
   */
  long getBuyerNumberOfOrders();

  /**
   * Buyer number of orders involved in match.
   * @return true if Buyer Number Of Orders is not null
   */
  boolean hasBuyerNumberOfOrders();

  /**
   * ID of seller order.
   * @return Seller Order ID
   */
  CharSequence getSellerOrderId();

  /**
   * ID of seller order.
   * @return true if Seller Order ID is not null
   */
  boolean hasSellerOrderId();

  /**
   * ID of buyer order.
   * @return Buyer Order ID
   */
  CharSequence getBuyerOrderId();

  /**
   * ID of buyer order.
   * @return true if Buyer Order ID is not null
   */
  boolean hasBuyerOrderId();

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @return Seller Participant ID
   */
  CharSequence getSellerParticipantId();

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @return true if Seller Participant ID is not null
   */
  boolean hasSellerParticipantId();

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @return Buyer Participant ID
   */
  CharSequence getBuyerParticipantId();

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @return true if Buyer Participant ID is not null
   */
  boolean hasBuyerParticipantId();

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @return Side
   */
  AggressorSide getSide();

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @return true if Side is not null
   */
  boolean hasSide();

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @return Match ID
   */
  CharSequence getMatchId();

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @return true if Match ID is not null
   */
  boolean hasMatchId();

  /**
   * Method copies state to a given instance
   */
  @Override
  TradeEntryInfo clone();
}
