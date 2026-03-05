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

import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Explains the meaning of the given price and/or size.
 */
public enum TradeType {
  /**
   * Regular trade.
   */
  @SchemaElement(
      name = "REGULAR_TRADE"
  )
  REGULAR_TRADE(0),

  /**
   * Auction price.
   */
  @SchemaElement(
      name = "AUCTION_CLEARING_PRICE"
  )
  AUCTION_CLEARING_PRICE(1),

  /**
   * Trade correction.
   */
  @SchemaElement(
      name = "CORRECTION"
  )
  CORRECTION(2),

  /**
   * Trade cancellation.
   */
  @OldElementName("CANCELATION")
  @SchemaElement(
      name = "CANCELLATION"
  )
  CANCELLATION(3),

  /**
   * Not regular trade of unknown type.
   */
  @SchemaElement(
      name = "UNKNOWN"
  )
  UNKNOWN(20);

  private final int value;

  TradeType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static TradeType valueOf(int number) {
    switch (number) {
      case 0: return REGULAR_TRADE;
      case 1: return AUCTION_CLEARING_PRICE;
      case 2: return CORRECTION;
      case 3: return CANCELLATION;
      case 20: return UNKNOWN;
      default: return null;
    }
  }

  public static TradeType strictValueOf(int number) {
    final TradeType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'TradeType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
