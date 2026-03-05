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
 * Insert type needs to distinguish between AddFront and AddBack.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.InsertType",
    title = "Insert Type"
)
public enum InsertType {
  /**
   * Add back.
   */
  @SchemaElement(
      name = "ADD_BACK"
  )
  ADD_BACK(0),

  /**
   * Add front.
   */
  @SchemaElement(
      name = "ADD_FRONT"
  )
  ADD_FRONT(1),

  /**
   * Add by time.
   */
  @SchemaElement(
      name = "ADD_BEFORE"
  )
  ADD_BEFORE(2);

  private final int value;

  InsertType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static InsertType valueOf(int number) {
    switch (number) {
      case 0: return ADD_BACK;
      case 1: return ADD_FRONT;
      case 2: return ADD_BEFORE;
      default: return null;
    }
  }

  public static InsertType strictValueOf(int number) {
    final InsertType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'InsertType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
