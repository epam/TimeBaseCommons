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
 * Schema definition for a class.
 */
public interface RecordClassDescriptorInterface extends RecordClassDescriptorInfo, ClassDescriptorInterface {
  /**
   * Defines parent RecordClassDescriptor if exist.
   * @param value - Parent
   */
  void setParent(RecordClassDescriptorInfo value);

  /**
   * Defines parent RecordClassDescriptor if exist.
   */
  void nullifyParent();

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @param value - Is Abstract
   */
  void setIsAbstract(boolean value);

  /**
   * Defines if current RecordClassDescriptor is abstract.
   */
  void nullifyIsAbstract();

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @param value - Is Content Class
   */
  void setIsContentClass(boolean value);

  /**
   * Defines if current RecordClassDescriptor is content class.
   */
  void nullifyIsContentClass();

  /**
   * List of fields of a class.
   * @param value - Data Fields
   */
  void setDataFields(ObjectArrayList<DataFieldInfo> value);

  /**
   * List of fields of a class.
   */
  void nullifyDataFields();

  /**
   * Method nullifies all instance properties
   */
  @Override
  RecordClassDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  RecordClassDescriptorInterface reset();

  @Override
  RecordClassDescriptorInterface copyFrom(RecordInfo template);
}