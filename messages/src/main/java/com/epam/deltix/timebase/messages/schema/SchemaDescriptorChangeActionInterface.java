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
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Class which defines a change to schema descriptor.
 */
public interface SchemaDescriptorChangeActionInterface extends SchemaDescriptorChangeActionInfo, RecordInterface {
  /**
   * Previous descriptor state.
   * @param value - Previous State
   */
  void setPreviousState(ClassDescriptorInfo value);

  /**
   * Previous descriptor state.
   */
  void nullifyPreviousState();

  /**
   * New descriptor state.
   * @param value - New State
   */
  void setNewState(ClassDescriptorInfo value);

  /**
   * New descriptor state.
   */
  void nullifyNewState();

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @param value - Change Types
   */
  void setChangeTypes(SchemaDescriptorChangeType value);

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   */
  void nullifyChangeTypes();

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @param value - Descriptor Transformation
   */
  void setDescriptorTransformation(SchemaDescriptorTransformationInfo value);

  /**
   * Defines the data transformation that was applied to the descriptor.
   */
  void nullifyDescriptorTransformation();

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @param value - Field Change Actions
   */
  void setFieldChangeActions(ObjectArrayList<SchemaFieldChangeActionInfo> value);

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   */
  void nullifyFieldChangeActions();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaDescriptorChangeActionInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaDescriptorChangeActionInterface reset();

  @Override
  SchemaDescriptorChangeActionInterface copyFrom(RecordInfo template);
}