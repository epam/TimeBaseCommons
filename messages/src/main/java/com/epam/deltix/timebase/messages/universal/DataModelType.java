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

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Level of data model.
 */
public enum DataModelType {
  /**
   * Level One (best bid and best offer)
   */
  @SchemaElement(
      name = "LEVEL_ONE"
  )
  LEVEL_ONE(0),

  /**
   * Level Two. Market by level. More details than LEVEL_ONE.
   */
  @SchemaElement(
      name = "LEVEL_TWO"
  )
  LEVEL_TWO(1),

  /**
   * Level Three. Market by Order. The most detailed view of the market.
   */
  @SchemaElement(
      name = "LEVEL_THREE"
  )
  LEVEL_THREE(2),

  /**
   * Maximal level of details availabe in input market data.
   */
  @SchemaElement(
      name = "MAX"
  )
  MAX(3);

  private final int value;

  DataModelType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static DataModelType valueOf(int number) {
    switch (number) {
      case 0: return LEVEL_ONE;
      case 1: return LEVEL_TWO;
      case 2: return LEVEL_THREE;
      case 3: return MAX;
      default: return null;
    }
  }

  public static DataModelType strictValueOf(int number) {
    final DataModelType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'DataModelType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
