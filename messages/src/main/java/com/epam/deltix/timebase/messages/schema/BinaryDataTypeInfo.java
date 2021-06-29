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

import java.lang.Override;

/**
 * Schema definition of binary data type.
 */
public interface BinaryDataTypeInfo extends DataTypeInfo {
  /**
   * Maximum field length in bytes.
   * @return Max Size
   */
  int getMaxSize();

  /**
   * Maximum field length in bytes.
   * @return true if Max Size is not null
   */
  boolean hasMaxSize();

  /**
   * Compression level for binary data.
   * @return Compression Level
   */
  short getCompressionLevel();

  /**
   * Compression level for binary data.
   * @return true if Compression Level is not null
   */
  boolean hasCompressionLevel();

  /**
   * Method copies state to a given instance
   */
  @Override
  BinaryDataTypeInfo clone();
}