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
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Schema definition of float data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.FloatDataType",
    title = "FloatDataType"
)
public class FloatDataType extends IntegralDataType implements FloatDataTypeInterface {
  public static final String CLASS_NAME = FloatDataType.class.getName();

  /**
   * Scale.
   */
  protected short scale = TypeConstants.INT16_NULL;

  /**
   * Scale.
   * @return Scale
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public short getScale() {
    return scale;
  }

  /**
   * Scale.
   * @param value - Scale
   */
  public void setScale(short value) {
    this.scale = value;
  }

  /**
   * Scale.
   * @return true if Scale is not null
   */
  public boolean hasScale() {
    return scale != com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Scale.
   */
  public void nullifyScale() {
    this.scale = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected FloatDataType createInstance() {
    return new FloatDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public FloatDataType nullify() {
    super.nullify();
    nullifyScale();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public FloatDataType reset() {
    super.reset();
    scale = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public FloatDataType clone() {
    FloatDataType t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    boolean superEquals = super.equals(obj);
    if (!superEquals) return false;
    if (!(obj instanceof FloatDataTypeInfo)) return false;
    FloatDataTypeInfo other =(FloatDataTypeInfo)obj;
    if (hasScale() != other.hasScale()) return false;
    if (hasScale() && getScale() != other.getScale()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasScale()) {
      hash = hash * 31 + ((int)getScale());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public FloatDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof FloatDataTypeInfo) {
      FloatDataTypeInfo t = (FloatDataTypeInfo)template;
      if (t.hasScale()) {
        setScale(t.getScale());
      } else {
        nullifyScale();
      }
    }
    return this;
  }

  /**
   * @return a string representation of this class object.
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    return toString(str).toString();
  }

  /**
   * @return a string representation of this class object.
   */
  @Override
  public StringBuilder toString(StringBuilder str) {
    str.append("{ \"$type\":  \"FloatDataType\"");
    if (hasScale()) {
      str.append(", \"scale\": ").append(getScale());
    }
    if (hasMinValue()) {
      str.append(", \"minValue\": \"").append(getMinValue()).append("\"");
    }
    if (hasMaxValue()) {
      str.append(", \"maxValue\": \"").append(getMaxValue()).append("\"");
    }
    if (hasEncoding()) {
      str.append(", \"encoding\": \"").append(getEncoding()).append("\"");
    }
    if (hasIsNullable()) {
      str.append(", \"isNullable\": ").append(isNullable());
    }
    str.append("}");
    return str;
  }
}