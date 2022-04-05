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
 * Reports incremental L2-updates: insert, delete or update of one line in Order Book either on ask or bid side.
 * It also can encode L2-snapshot entry. Note L2 is level oriented depth-of-the-book format and should be used
 * whenever price or integer index is used to locate book changes. It does support individual quotes book of
 * arbitrary depth. But if incremental changes key is a quoteId L3Entry should be used instead.
 */
public interface L2EntryUpdateInterface extends L2EntryUpdateInfo, BasePriceEntryInterface {
  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   * @param value - Level Index
   */
  void setLevel(short value);

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   */
  void nullifyLevel();

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, marketMakerCode, depth</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @param value - Action
   */
  void setAction(BookUpdateAction value);

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, marketMakerCode, depth</b> fields constitute the composite key
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
  L2EntryUpdateInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  L2EntryUpdateInterface reset();

  @Override
  L2EntryUpdateInterface copyFrom(RecordInfo template);
}
