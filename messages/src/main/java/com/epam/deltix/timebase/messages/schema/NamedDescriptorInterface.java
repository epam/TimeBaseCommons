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
 * This is a base class for schema nodes.
 */
public interface NamedDescriptorInterface extends NamedDescriptorInfo, RecordInterface {
  /**
   * Node name.
   * @param value - Name
   */
  void setName(CharSequence value);

  /**
   * Node name.
   */
  void nullifyName();

  /**
   * Node title.
   * @param value - Title
   */
  void setTitle(CharSequence value);

  /**
   * Node title.
   */
  void nullifyTitle();

  /**
   * Node description.
   * @param value - Description
   */
  void setDescription(CharSequence value);

  /**
   * Node description.
   */
  void nullifyDescription();

  /**
   * Method nullifies all instance properties
   */
  @Override
  NamedDescriptorInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  NamedDescriptorInterface reset();

  @Override
  NamedDescriptorInterface copyFrom(RecordInfo template);
}