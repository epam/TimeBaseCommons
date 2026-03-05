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
package com.epam.deltix.timebase.messages;

/**
 * Most financial market-related messages subclass this abstract class.
 */
public interface MarketMessageInterface extends MarketMessageInfo, MessageInterface {
  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @param value - Original Timestamp
   */
  void setOriginalTimestamp(long value);

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   */
  void nullifyOriginalTimestamp();

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @param value - Currency
   */
  void setCurrency(long value);

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   */
  void nullifyCurrency();

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @param value - Sequence Number
   */
  void setSequenceNumber(long value);

  /**
   * Market specific identifier of the given event in a sequence of market events.
   */
  void nullifySequenceNumber();

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @param value - Source Id
   */
  void setSourceId(long value);

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   */
  void nullifySourceId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  MarketMessageInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  MarketMessageInterface reset();

  @Override
  MarketMessageInterface copyFrom(RecordInfo template);
}
