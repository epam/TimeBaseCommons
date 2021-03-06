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

import com.epam.deltix.timebase.messages.MessageInterface;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Override;

/**
 * Message tha defines a change in stream schema.
 */
public interface SchemaChangeMessageInterface extends SchemaChangeMessageInfo, MessageInterface {
  /**
   * Previous schema state.
   * @param value - Previous State
   */
  void setPreviousState(ObjectArrayList<ClassDescriptorInfo> value);

  /**
   * Previous schema state.
   */
  void nullifyPreviousState();

  /**
   * New schema state.
   * @param value - New State
   */
  void setNewState(ObjectArrayList<ClassDescriptorInfo> value);

  /**
   * New schema state.
   */
  void nullifyNewState();

  /**
   * New schema state.
   * @param value - Descriptor Change Actions
   */
  void setDescriptorChangeActions(ObjectArrayList<SchemaDescriptorChangeActionInfo> value);

  /**
   * New schema state.
   */
  void nullifyDescriptorChangeActions();

  /**
   * Message version
   * @param value - Version
   */
  void setVersion(long value);

  /**
   * Message version
   */
  void nullifyVersion();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaChangeMessageInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaChangeMessageInterface reset();

  @Override
  SchemaChangeMessageInterface copyFrom(RecordInfo template);
}