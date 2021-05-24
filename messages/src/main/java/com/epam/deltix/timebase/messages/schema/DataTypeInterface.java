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
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema data types.
 */
public interface DataTypeInterface extends DataTypeInfo, RecordInterface {
  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @param value - Encoding
   */
  void setEncoding(CharSequence value);

  /**
   * Binary representation (encoding) of a field in database and protocol.
   */
  void nullifyEncoding();

  /**
   * True, if schema design allows the field to be nullable.
   * @param value - Is Nullable
   */
  void setIsNullable(boolean value);

  /**
   * True, if schema design allows the field to be nullable.
   */
  void nullifyIsNullable();

  /**
   * Method nullifies all instance properties
   */
  @Override
  DataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  DataTypeInterface reset();

  @Override
  DataTypeInterface copyFrom(RecordInfo template);
}