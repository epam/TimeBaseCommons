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
public interface MarketMessageInfo extends MessageInfo {
  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return Original Timestamp
   */
  long getOriginalTimestamp();

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return true if Original Timestamp is not null
   */
  boolean hasOriginalTimestamp();

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return Currency
   */
  long getCurrency();

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return true if Currency is not null
   */
  boolean hasCurrency();

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return Sequence Number
   */
  long getSequenceNumber();

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return true if Sequence Number is not null
   */
  boolean hasSequenceNumber();

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return Source Id
   */
  long getSourceId();

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return true if Source Id is not null
   */
  boolean hasSourceId();

  /**
   * Method copies state to a given instance
   */
  @Override
  MarketMessageInfo clone();
}
