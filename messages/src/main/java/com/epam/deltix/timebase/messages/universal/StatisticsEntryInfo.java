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

/**
 * Represents arbitrary exchange event.
 */
public interface StatisticsEntryInfo extends BaseEntryInfo {
  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return Type
   */
  StatisticsType getType();

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return true if Type is not null
   */
  boolean hasType();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return Value
   */
  long getValue();

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return true if Value is not null
   */
  boolean hasValue();

  /**
   * Original event type, vendor specific.
   * @return OriginalType
   */
  CharSequence getOriginalType();

  /**
   * Original event type, vendor specific.
   * @return true if OriginalType is not null
   */
  boolean hasOriginalType();

  /**
   * Method copies state to a given instance
   */
  @Override
  StatisticsEntryInfo clone();
}
