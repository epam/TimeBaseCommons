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
package com.epam.deltix.timebase.messages;

/**
 */
public enum SchemaDataType {
  /**
   */
  @SchemaElement(
      name = "DEFAULT"
  )
  DEFAULT(0),

  /**
   */
  @SchemaElement(
      name = "CHAR"
  )
  CHAR(1),

  /**
   */
  @SchemaElement(
      name = "VARCHAR"
  )
  VARCHAR(2),

  /**
   */
  @SchemaElement(
      name = "BINARY"
  )
  BINARY(3),

  /**
   */
  @SchemaElement(
      name = "BOOLEAN"
  )
  BOOLEAN(4),

  /**
   */
  @SchemaElement(
      name = "INTEGER"
  )
  INTEGER(5),

  /**
   */
  @SchemaElement(
      name = "FLOAT"
  )
  FLOAT(6),

  /**
   */
  @SchemaElement(
      name = "TIMESTAMP"
  )
  TIMESTAMP(7),

  /**
   */
  @SchemaElement(
      name = "TIME_OF_DAY"
  )
  TIME_OF_DAY(8),

  /**
   */
  @SchemaElement(
      name = "OBJECT"
  )
  OBJECT(9),

  /**
   */
  @SchemaElement(
      name = "ENUM"
  )
  ENUM(10);

  private final int value;

  SchemaDataType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaDataType valueOf(int number) {
    switch (number) {
      case 0: return DEFAULT;
      case 1: return CHAR;
      case 2: return VARCHAR;
      case 3: return BINARY;
      case 4: return BOOLEAN;
      case 5: return INTEGER;
      case 6: return FLOAT;
      case 7: return TIMESTAMP;
      case 8: return TIME_OF_DAY;
      case 9: return OBJECT;
      case 10: return ENUM;
      default: return null;
    }
  }

  public static SchemaDataType strictValueOf(int number) {
    final SchemaDataType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaDataType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
