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

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Represents arbitrary exchange event.
 */
public interface StatisticsEntryInterface extends StatisticsEntryInfo, BaseEntryInterface {
  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @param value - Type
   */
  void setType(StatisticsType value);

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   */
  void nullifyType();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @param value - Value
   */
  void setValue(long value);

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   */
  void nullifyValue();

  /**
   * Original event type, vendor specific.
   * @param value - OriginalType
   */
  void setOriginalType(CharSequence value);

  /**
   * Original event type, vendor specific.
   */
  void nullifyOriginalType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  StatisticsEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  StatisticsEntryInterface reset();

  @Override
  StatisticsEntryInterface copyFrom(RecordInfo template);
}
