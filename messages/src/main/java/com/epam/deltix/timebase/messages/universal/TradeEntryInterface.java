/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Basic information about a market trade.
 */
public interface TradeEntryInterface extends TradeEntryInfo, BaseEntryInterface {
  /**
   * Ask, Bid or Trade price.
   * @param value - Price
   */
  void setPrice(long value);

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
   * Market specific trade condition.
   * @param value - Condition
   */
  void setCondition(CharSequence value);

  /**
   * Market specific trade condition.
   */
  void nullifyCondition();

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @param value - Trade Type
   */
  void setTradeType(TradeType value);

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   */
  void nullifyTradeType();

  /**
   * Seller number of orders involved in match.
   * @param value - Seller Number Of Orders
   */
  void setSellerNumberOfOrders(long value);

  /**
   * Seller number of orders involved in match.
   */
  void nullifySellerNumberOfOrders();

  /**
   * Buyer number of orders involved in match.
   * @param value - Buyer Number Of Orders
   */
  void setBuyerNumberOfOrders(long value);

  /**
   * Buyer number of orders involved in match.
   */
  void nullifyBuyerNumberOfOrders();

  /**
   * ID of seller order.
   * @param value - Seller Order ID
   */
  void setSellerOrderId(CharSequence value);

  /**
   * ID of seller order.
   */
  void nullifySellerOrderId();

  /**
   * ID of buyer order.
   * @param value - Buyer Order ID
   */
  void setBuyerOrderId(CharSequence value);

  /**
   * ID of buyer order.
   */
  void nullifyBuyerOrderId();

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @param value - Seller Participant ID
   */
  void setSellerParticipantId(CharSequence value);

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   */
  void nullifySellerParticipantId();

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @param value - Buyer Participant ID
   */
  void setBuyerParticipantId(CharSequence value);

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   */
  void nullifyBuyerParticipantId();

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @param value - Side
   */
  void setSide(AggressorSide value);

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   */
  void nullifySide();

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @param value - Match ID
   */
  void setMatchId(CharSequence value);

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   */
  void nullifyMatchId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  TradeEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  TradeEntryInterface reset();

  @Override
  TradeEntryInterface copyFrom(RecordInfo template);
}
