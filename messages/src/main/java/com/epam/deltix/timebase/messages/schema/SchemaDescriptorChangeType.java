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
package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.Bitmask;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Type of descriptor change.
 */
@Bitmask
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorChangeType",
    title = "SchemaDescriptorChangeType"
)
public enum SchemaDescriptorChangeType {
  /**
   * Add operation.
   */
  @SchemaElement(
      name = "ADD"
  )
  ADD(1),

  /**
   * Delete operation.
   */
  @SchemaElement(
      name = "DELETE"
  )
  DELETE(2),

  /**
   * Rename operation.
   */
  @SchemaElement(
      name = "RENAME"
  )
  RENAME(4),

  /**
   * Fields change operation.
   */
  @SchemaElement(
      name = "FIELDS_CHANGE"
  )
  FIELDS_CHANGE(8),

  /**
   * Content type change operation.
   */
  @SchemaElement(
      name = "CONTENT_TYPE_CHANGE"
  )
  CONTENT_TYPE_CHANGE(16);

  private final int value;

  SchemaDescriptorChangeType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaDescriptorChangeType valueOf(int number) {
    switch (number) {
      case 1: return ADD;
      case 2: return DELETE;
      case 4: return RENAME;
      case 8: return FIELDS_CHANGE;
      case 16: return CONTENT_TYPE_CHANGE;
      default: return null;
    }
  }

  public static SchemaDescriptorChangeType strictValueOf(int number) {
    final SchemaDescriptorChangeType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaDescriptorChangeType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}