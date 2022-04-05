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
package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Security feed status.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.FeedStatus",
    title = "Security Feed Status"
)
public enum FeedStatus {
  /**
   * Feed for this security is available again.
   */
  @SchemaElement(
      name = "AVAILABLE"
  )
  AVAILABLE(0),

  /**
   * Feed for this security and exchange code is no longer available.
   */
  @SchemaElement(
      name = "NOT_AVAILABLE"
  )
  NOT_AVAILABLE(1);

  private final int value;

  FeedStatus(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static FeedStatus valueOf(int number) {
    switch (number) {
      case 0: return AVAILABLE;
      case 1: return NOT_AVAILABLE;
      default: return null;
    }
  }

  public static FeedStatus strictValueOf(int number) {
    final FeedStatus value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'FeedStatus' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
