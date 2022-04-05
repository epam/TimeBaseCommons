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
 * This class may represent both exchange-local top of the book (BBO) as well as National Best Bid Offer (NBBO).
 * You can use method {getIsNational()} to filter out NBBO messages.
 * This is always a one side quote, unlike old BestBidOfferMessage which is two-side (with nullable properties).
 */
public interface L1EntryInterface extends L1EntryInfo, BasePriceEntryInterface {
  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @param value - Is National
   */
  void setIsNational(boolean value);

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   */
  void nullifyIsNational();

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
  L1EntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  L1EntryInterface reset();

  @Override
  L1EntryInterface copyFrom(RecordInfo template);
}
