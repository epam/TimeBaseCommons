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
 * Base class for all messages that could be written in Timebase.
 */
public interface MessageInfo extends RecordInfo {
  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return Time Stamp Ms
   */
  long getTimeStampMs();

  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return true if Time Stamp Ms is not null
   */
  boolean hasTimeStampMs();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return Nano Time
   */
  long getNanoTime();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @return true if Nano Time is not null
   */
  boolean hasNanoTime();

  /**
   * Instrument name.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Instrument name.
   * @return true if Symbol is not null
   */
  boolean hasSymbol();

  /**
   * Method copies state to a given instance
   */
  @Override
  MessageInfo clone();
}
