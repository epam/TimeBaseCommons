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

import com.epam.deltix.timebase.messages.MarketMessageInterface;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Represents market data package.
 */
public interface PackageHeaderInterface extends PackageHeaderInfo, MarketMessageInterface {
  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @param value - Entries
   */
  void setEntries(ObjectArrayList<BaseEntryInfo> value);

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   */
  void nullifyEntries();

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @param value - Package Type
   */
  void setPackageType(PackageType value);

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   */
  void nullifyPackageType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  PackageHeaderInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  PackageHeaderInterface reset();

  @Override
  PackageHeaderInterface copyFrom(RecordInfo template);
}
