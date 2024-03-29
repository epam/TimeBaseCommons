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
 */
public enum ErrorLevel {
  /**
   */
  @SchemaElement(
      name = "DEBUG"
  )
  DEBUG(0),

  /**
   * Maps to QuantOffice EventSeverity.Information
   */
  @SchemaElement(
      name = "INFO"
  )
  INFO(1),

  /**
   * Maps to QuantOffice EventSeverity.Warning
   */
  @SchemaElement(
      name = "ALERT"
  )
  ALERT(2),

  /**
   * Application/User code error
   * Maps to QuantOffice EventSeverity.Error, will be visible as Trading Console alert
   */
  @SchemaElement(
      name = "USER_ERROR"
  )
  USER_ERROR(3),

  /**
   * System error (Error in UHF/QuantOffice runtime)
   */
  @SchemaElement(
      name = "SYSTEM_ERROR"
  )
  SYSTEM_ERROR(4),

  /**
   */
  @SchemaElement(
      name = "HEARTBEAT"
  )
  HEARTBEAT(5);

  private final int value;

  ErrorLevel(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static ErrorLevel valueOf(int number) {
    switch (number) {
      case 0: return DEBUG;
      case 1: return INFO;
      case 2: return ALERT;
      case 3: return USER_ERROR;
      case 4: return SYSTEM_ERROR;
      case 5: return HEARTBEAT;
      default: return null;
    }
  }

  public static ErrorLevel strictValueOf(int number) {
    final ErrorLevel value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'ErrorLevel' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
