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
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
public interface L3EntryNewInterface extends L3EntryNewInfo, BasePriceEntryInterface {
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
   * Insert type. Add front or Add back.
   * @param value - Insert Type
   */
  void setInsertType(InsertType value);

  /**
   * Insert type. Add front or Add back.
   */
  void nullifyInsertType();

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @param value - Insert Before Quote Id
   */
  void setInsertBeforeQuoteId(CharSequence value);

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   */
  void nullifyInsertBeforeQuoteId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  L3EntryNewInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  L3EntryNewInterface reset();

  @Override
  L3EntryNewInterface copyFrom(RecordInfo template);
}
