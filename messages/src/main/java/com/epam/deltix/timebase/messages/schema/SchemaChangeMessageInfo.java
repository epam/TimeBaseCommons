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

import com.epam.deltix.timebase.messages.MessageInfo;
import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Message tha defines a change in stream schema.
 */
public interface SchemaChangeMessageInfo extends MessageInfo {
  /**
   * Previous schema state.
   * @return Previous State
   */
  ObjectList<ClassDescriptorInfo> getPreviousState();

  /**
   * Previous schema state.
   * @return true if Previous State is not null
   */
  boolean hasPreviousState();

  /**
   * New schema state.
   * @return New State
   */
  ObjectList<ClassDescriptorInfo> getNewState();

  /**
   * New schema state.
   * @return true if New State is not null
   */
  boolean hasNewState();

  /**
   * New schema state.
   * @return Descriptor Change Actions
   */
  ObjectList<SchemaDescriptorChangeActionInfo> getDescriptorChangeActions();

  /**
   * New schema state.
   * @return true if Descriptor Change Actions is not null
   */
  boolean hasDescriptorChangeActions();

  /**
   * Message version
   * @return Version
   */
  long getVersion();

  /**
   * Message version
   * @return true if Version is not null
   */
  boolean hasVersion();

  /**
   * Method copies state to a given instance
   */
  @Override
  SchemaChangeMessageInfo clone();
}