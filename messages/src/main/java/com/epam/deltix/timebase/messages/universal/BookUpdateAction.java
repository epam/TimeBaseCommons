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
 * Directs how to update an Order Book for L2 format.
 */
@SchemaElement(
    name = "deltix.timebase.api.messages.BookUpdateAction",
    title = "Book Update Action"
)
public enum BookUpdateAction {
  /**
   * Quote is inserted (automatically shifts existing levels down). INSERT action is sent if there is a new price level.
   * Client systems should then shift price levels down, and delete any price levels past the defined depth of the book.
   */
  @SchemaElement(
      name = "INSERT"
  )
  INSERT(0),

  /**
   * Update of an existing quote. The UPDATE action is sent to update characteristics of a price level without changing the price itself,
   * or impacting any other prices on the book. The change data block is sent to update the order count and / or quantity for a price level.
   * The UPDATE is not sent when the price changes at a given price level.
   */
  @SchemaElement(
      name = "UPDATE"
  )
  UPDATE(1),

  /**
   * Quote at given price level is deleted. Client systems should shift prices below the data block up to the price level vacated by the deleted price level.
   * If available, an add data block will be sent to fill in the last price level.
   */
  @SchemaElement(
      name = "DELETE"
  )
  DELETE(2);

  private final int value;

  BookUpdateAction(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static BookUpdateAction valueOf(int number) {
    switch (number) {
      case 0: return INSERT;
      case 1: return UPDATE;
      case 2: return DELETE;
      default: return null;
    }
  }

  public static BookUpdateAction strictValueOf(int number) {
    final BookUpdateAction value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'BookUpdateAction' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
