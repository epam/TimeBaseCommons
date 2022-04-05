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
public interface MessageInterface extends MessageInfo, RecordInterface {
  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @param value - Time Stamp Ms
   */
  void setTimeStampMs(long value);

  /**
   *
   * Gets message time in milliseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   */
  void nullifyTimeStampMs();

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   * @param value - Nano Time
   */
  void setNanoTime(long value);

  /**
   *
   * Gets message time in nanoseconds that passed since January 1, 1970, 00:00:00 GMT
   *
   */
  void nullifyNanoTime();

  /**
   * Instrument name.
   * @param value - Symbol
   */
  void setSymbol(CharSequence value);

  /**
   * Instrument name.
   */
  void nullifySymbol();

  /**
   * Method nullifies all instance properties
   */
  @Override
  MessageInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  MessageInterface reset();

  @Override
  MessageInterface copyFrom(RecordInfo template);
}
