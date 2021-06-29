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
import java.lang.Override;

/**
 * Schema definition of binary data type.
 */
public interface BinaryDataTypeInterface extends BinaryDataTypeInfo, DataTypeInterface {
  /**
   * Maximum field length in bytes.
   * @param value - Max Size
   */
  void setMaxSize(int value);

  /**
   * Maximum field length in bytes.
   */
  void nullifyMaxSize();

  /**
   * Compression level for binary data.
   * @param value - Compression Level
   */
  void setCompressionLevel(short value);

  /**
   * Compression level for binary data.
   */
  void nullifyCompressionLevel();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BinaryDataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BinaryDataTypeInterface reset();

  @Override
  BinaryDataTypeInterface copyFrom(RecordInfo template);
}