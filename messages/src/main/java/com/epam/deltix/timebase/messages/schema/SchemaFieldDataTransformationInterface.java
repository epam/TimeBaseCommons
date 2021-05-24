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
 * Class which defines a transformation that is applied to a data field.
 */
public interface SchemaFieldDataTransformationInterface extends SchemaFieldDataTransformationInfo, RecordInterface {
  /**
   * Defines the transformation that was applied to the field.
   * @param value - Transformation Type
   */
  void setTransformationType(SchemaFieldDataTransformationType value);

  /**
   * Defines the transformation that was applied to the field.
   */
  void nullifyTransformationType();

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @param value - Default Value
   */
  void setDefaultValue(CharSequence value);

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   */
  void nullifyDefaultValue();

  /**
   * Method nullifies all instance properties
   */
  @Override
  SchemaFieldDataTransformationInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  SchemaFieldDataTransformationInterface reset();

  @Override
  SchemaFieldDataTransformationInterface copyFrom(RecordInfo template);
}