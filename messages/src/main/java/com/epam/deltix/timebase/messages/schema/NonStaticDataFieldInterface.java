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
import java.lang.CharSequence;
import java.lang.Override;

/**
 * Schema definition for non-static data field.
 */
public interface NonStaticDataFieldInterface extends NonStaticDataFieldInfo, DataFieldInterface {
  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @param value - Is Primary Key
   */
  void setIsPrimaryKey(boolean value);

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   */
  void nullifyIsPrimaryKey();

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @param value - Relative To
   */
  void setRelativeTo(CharSequence value);

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   */
  void nullifyRelativeTo();

  /**
   * Method nullifies all instance properties
   */
  @Override
  NonStaticDataFieldInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  NonStaticDataFieldInterface reset();

  @Override
  NonStaticDataFieldInterface copyFrom(RecordInfo template);
}