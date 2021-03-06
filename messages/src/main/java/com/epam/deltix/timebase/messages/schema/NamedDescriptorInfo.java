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
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema nodes.
 */
public interface NamedDescriptorInfo extends RecordInfo {
  /**
   * Node name.
   * @return Name
   */
  CharSequence getName();

  /**
   * Node name.
   * @return true if Name is not null
   */
  boolean hasName();

  /**
   * Node title.
   * @return Title
   */
  CharSequence getTitle();

  /**
   * Node title.
   * @return true if Title is not null
   */
  boolean hasTitle();

  /**
   * Node description.
   * @return Description
   */
  CharSequence getDescription();

  /**
   * Node description.
   * @return true if Description is not null
   */
  boolean hasDescription();

  /**
   * Method copies state to a given instance
   */
  @Override
  NamedDescriptorInfo clone();
}