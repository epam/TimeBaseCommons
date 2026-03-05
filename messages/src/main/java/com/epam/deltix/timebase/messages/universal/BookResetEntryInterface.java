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
 * This entry used for empty snapshot indication. It is necessary because PackageHeader has no field exchangeId.
 * You should use this entry only for empty snapshot (both sides are empty) case.
 */
public interface BookResetEntryInterface extends BookResetEntryInfo, BaseEntryInterface {
  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @param value - Side
   */
  void setSide(QuoteSide value);

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  void nullifySide();

  /**
   * Data Model Type to identify what book we should reset.
   * @param value - Model Type
   */
  void setModelType(DataModelType value);

  /**
   * Data Model Type to identify what book we should reset.
   */
  void nullifyModelType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BookResetEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BookResetEntryInterface reset();

  @Override
  BookResetEntryInterface copyFrom(RecordInfo template);
}
