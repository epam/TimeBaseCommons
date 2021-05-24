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

import com.epam.deltix.util.collections.generated.ObjectList;
import java.lang.Override;

/**
 * Schema definition for a class.
 */
public interface RecordClassDescriptorInfo extends ClassDescriptorInfo {
  /**
   * Defines parent RecordClassDescriptor if exist.
   * @return Parent
   */
  RecordClassDescriptorInfo getParent();

  /**
   * Defines parent RecordClassDescriptor if exist.
   * @return true if Parent is not null
   */
  boolean hasParent();

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @return Is Abstract
   */
  boolean isAbstract();

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @return true if Is Abstract is not null
   */
  boolean hasIsAbstract();

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @return Is Content Class
   */
  boolean isContentClass();

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @return true if Is Content Class is not null
   */
  boolean hasIsContentClass();

  /**
   * List of fields of a class.
   * @return Data Fields
   */
  ObjectList<DataFieldInfo> getDataFields();

  /**
   * List of fields of a class.
   * @return true if Data Fields is not null
   */
  boolean hasDataFields();

  /**
   * Method copies state to a given instance
   */
  @Override
  RecordClassDescriptorInfo clone();
}