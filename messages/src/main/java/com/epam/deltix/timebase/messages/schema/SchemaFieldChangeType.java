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

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Type of field change.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaFieldChangeType",
    title = "SchemaFieldChangeType"
)
public enum SchemaFieldChangeType {
  /**
   * Add field or enumeration value operation.
   */
  @SchemaElement(
      name = "ADD"
  )
  ADD(1),

  /**
   * Delete field or enumeration value operation.
   */
  @SchemaElement(
      name = "DELETE"
  )
  DELETE(2),

  /**
   * Rename field or enumeration value operation.
   */
  @SchemaElement(
      name = "RENAME"
  )
  RENAME(4),

  /**
   * Change field title operation.
   */
  @SchemaElement(
      name = "TITLE_CHANGE"
  )
  TITLE_CHANGE(8),

  /**
   * Change field description operation.
   */
  @SchemaElement(
      name = "DESCRIPTION_CHANGE"
  )
  DESCRIPTION_CHANGE(16),

  /**
   * Change field data type operation.
   */
  @SchemaElement(
      name = "DATA_TYPE_CHANGE"
  )
  DATA_TYPE_CHANGE(32),

  /**
   * Change field ordinal operation.
   */
  @SchemaElement(
      name = "ORDINAL_CHANGE"
  )
  ORDINAL_CHANGE(64),

  /**
   * Change field modifier (static and non-static) operation.
   */
  @SchemaElement(
      name = "MODIFIER_CHANGE"
  )
  MODIFIER_CHANGE(128),

  /**
   * Change field static value operation.
   */
  @SchemaElement(
      name = "STATIC_VALUE_CHANGE"
  )
  STATIC_VALUE_CHANGE(256),

  /**
   * Change fileld primary key attribute operation.
   */
  @SchemaElement(
      name = "PRIMARY_KEY_CHANGE"
  )
  PRIMARY_KEY_CHANGE(512),

  /**
   * Change field relation attribute operation.
   */
  @SchemaElement(
      name = "RELATION_CHANGE"
  )
  RELATION_CHANGE(1024);

  private final int value;

  SchemaFieldChangeType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaFieldChangeType valueOf(int number) {
    switch (number) {
      case 1: return ADD;
      case 2: return DELETE;
      case 4: return RENAME;
      case 8: return TITLE_CHANGE;
      case 16: return DESCRIPTION_CHANGE;
      case 32: return DATA_TYPE_CHANGE;
      case 64: return ORDINAL_CHANGE;
      case 128: return MODIFIER_CHANGE;
      case 256: return STATIC_VALUE_CHANGE;
      case 512: return PRIMARY_KEY_CHANGE;
      case 1024: return RELATION_CHANGE;
      default: return null;
    }
  }

  public static SchemaFieldChangeType strictValueOf(int number) {
    final SchemaFieldChangeType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaFieldChangeType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
