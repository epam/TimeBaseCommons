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
 * Type of transformation applied to the descriptor.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorTransformationType",
    title = "SchemaDescriptorTransformationType"
)
public enum SchemaDescriptorTransformationType {
  /**
   * Drop record operation.
   */
  @SchemaElement(
      name = "DROP_RECORD"
  )
  DROP_RECORD(0);

  private final int value;

  SchemaDescriptorTransformationType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static SchemaDescriptorTransformationType valueOf(int number) {
    switch (number) {
      case 0: return DROP_RECORD;
      default: return null;
    }
  }

  public static SchemaDescriptorTransformationType strictValueOf(int number) {
    final SchemaDescriptorTransformationType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'SchemaDescriptorTransformationType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
