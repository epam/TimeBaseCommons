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
import java.lang.Override;

/**
 * Class which defines a change to schema field.
 */
public interface SchemaFieldChangeActionInterface extends SchemaFieldChangeActionInfo, RecordInterface {
  /**
   * Previous data field state.
   * @param value - Previous State
   */
  void setPreviousState(DataFieldInfo value);

  /**
   * Previous data field state.
   */
  void nullifyPreviousState();

  /**
   * New descriptor state.
   * @param value - New State
   */
  void setNewState(DataFieldInfo value);

  /**
   * New descriptor state.
   */
  void nullifyNewState();

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @param value - Change Types
   */
  void setChangeTypes(SchemaFieldChangeType value);

  /**
   * Bitmask that defines the changes that were applied to the field.
   */
  void nullifyChangeTypes();

  /**
   * Defines the data transformation that was applied to the field.
   * @param value - Data Transformation
   */
  void setDataTransformation(SchemaFieldDataTransformationInfo value);

  /**
   * Defines the data transformation that was applied to the field.
   */
  void nullifyDataTransformation();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaFieldChangeActionInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaFieldChangeActionInterface reset();

  @Override
  SchemaFieldChangeActionInterface copyFrom(RecordInfo template);
}