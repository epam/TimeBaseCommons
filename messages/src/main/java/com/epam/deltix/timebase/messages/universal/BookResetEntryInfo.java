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

/**
 * This entry used for empty snapshot indication. It is necessary because PackageHeader has no field exchangeId.
 * You should use this entry only for empty snapshot (both sides are empty) case.
 */
public interface BookResetEntryInfo extends BaseEntryInfo {
  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
  QuoteSide getSide();

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return true if Side is not null
   */
  boolean hasSide();

  /**
   * Data Model Type to identify what book we should reset.
   * @return Model Type
   */
  DataModelType getModelType();

  /**
   * Data Model Type to identify what book we should reset.
   * @return true if Model Type is not null
   */
  boolean hasModelType();

  /**
   * Method copies state to a given instance
   */
  @Override
  BookResetEntryInfo clone();
}
