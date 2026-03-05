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
import com.epam.deltix.timebase.messages.RecordInterface;

/**
 * Base class for market data entry to be included in package (PackageHeader).
 */
public interface BaseEntryInterface extends BaseEntryInfo, RecordInterface {
  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @param value - Exchange Code
   */
  void setExchangeId(long value);

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   */
  void nullifyExchangeId();

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @param value - Is Implied
   */
  void setIsImplied(boolean value);

  /**
   * True, if quote (or trade) comes from an implied Order book.
   */
  void nullifyIsImplied();

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @param value - Contract ID
   */
  void setContractId(long value);

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   */
  void nullifyContractId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BaseEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BaseEntryInterface reset();

  @Override
  BaseEntryInterface copyFrom(RecordInfo template);
}
