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
 * Package type needs to distinguish between incremental changes and different types of snapshot.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.PackageType",
    title = "Package Type"
)
public enum PackageType {
  /**
   * Vendor snapshot.
   */
  @SchemaElement(
      name = "VENDOR_SNAPSHOT"
  )
  VENDOR_SNAPSHOT(0),

  /**
   * Periodical snapshot.
   */
  @SchemaElement(
      name = "PERIODICAL_SNAPSHOT"
  )
  PERIODICAL_SNAPSHOT(1),

  /**
   * Incremental update.
   */
  @SchemaElement(
      name = "INCREMENTAL_UPDATE"
  )
  INCREMENTAL_UPDATE(2);

  private final int value;

  PackageType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static PackageType valueOf(int number) {
    switch (number) {
      case 0: return VENDOR_SNAPSHOT;
      case 1: return PERIODICAL_SNAPSHOT;
      case 2: return INCREMENTAL_UPDATE;
      default: return null;
    }
  }

  public static PackageType strictValueOf(int number) {
    final PackageType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'PackageType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
