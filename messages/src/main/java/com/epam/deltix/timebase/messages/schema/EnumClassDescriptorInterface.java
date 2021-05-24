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

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Schema definition for enumeration class.
 */
public interface EnumClassDescriptorInterface extends EnumClassDescriptorInfo, ClassDescriptorInterface {
  /**
   * List of values of enumeration.
   * @param value - Values
   */
  void setValues(ObjectArrayList<EnumValueInfo> value);

  /**
   * List of values of enumeration.
   */
  void nullifyValues();

  /**
   * True, if enumeration represents a bitmask.
   * @param value - Is Bitmask
   */
  void setIsBitmask(boolean value);

  /**
   * True, if enumeration represents a bitmask.
   */
  void nullifyIsBitmask();

  /**
   * Method nullifies all instance properties
   */
  @Override
  EnumClassDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  EnumClassDescriptorInterface reset();

  @Override
  EnumClassDescriptorInterface copyFrom(RecordInfo template);
}