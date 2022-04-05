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
 * Side of quote. Bid and Ask .
 */
public enum QuoteSide {
  /**
   * Bid for quote.
   */
  @SchemaElement(
      name = "BID"
  )
  BID(0),

  /**
   * Ask for quote.
   */
  @SchemaElement(
      name = "ASK"
  )
  ASK(1);

  private final int value;

  QuoteSide(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static QuoteSide valueOf(int number) {
    switch (number) {
      case 0: return BID;
      case 1: return ASK;
      default: return null;
    }
  }

  public static QuoteSide strictValueOf(int number) {
    final QuoteSide value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'QuoteSide' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
