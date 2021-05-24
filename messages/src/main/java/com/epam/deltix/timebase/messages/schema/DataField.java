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
import com.epam.deltix.timebase.messages.SchemaDataType;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.UnsupportedOperationException;

/**
 * This is a base class for schema data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.DataField",
    title = "DataField"
)
public abstract class DataField extends NamedDescriptor implements DataFieldInterface {
  public static final String CLASS_NAME = DataField.class.getName();

  /**
   * Data type of a field.
   */
  protected DataTypeInfo dataType = null;

  /**
   * Data type of a field.
   * @return Data Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            ArrayDataType.class, BinaryDataType.class, BooleanDataType.class, CharDataType.class, ClassDataType.class, DateTimeDataType.class, EnumDataType.class, FloatDataType.class, IntegerDataType.class, TimeOfDayDataType.class, VarcharDataType.class}

  )
  public DataTypeInfo getDataType() {
    return dataType;
  }

  /**
   * Data type of a field.
   * @param value - Data Type
   */
  public void setDataType(DataTypeInfo value) {
    this.dataType = value;
  }

  /**
   * Data type of a field.
   * @return true if Data Type is not null
   */
  public boolean hasDataType() {
    return dataType != null;
  }

  /**
   * Data type of a field.
   */
  public void nullifyDataType() {
    this.dataType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected DataField createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public DataField nullify() {
    super.nullify();
    nullifyDataType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public DataField reset() {
    super.reset();
    dataType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public DataField clone() {
    DataField t = createInstance();
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
    if (!(obj instanceof DataFieldInfo)) return false;
    DataFieldInfo other =(DataFieldInfo)obj;
    if (hasDataType() != other.hasDataType()) return false;
    if (hasDataType() && !(getDataType().equals(other.getDataType()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasDataType()) {
      hash = hash * 31 + getDataType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public DataField copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof DataFieldInfo) {
      DataFieldInfo t = (DataFieldInfo)template;
      if (t.hasDataType()) {
        if (hasDataType() && getDataType() instanceof RecordInterface) {
          ((RecordInterface)getDataType()).copyFrom(t.getDataType());
        } else {
          setDataType((DataTypeInfo)t.getDataType().clone());
        }
      } else {
        nullifyDataType();
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
    str.append("{ \"$type\":  \"DataField\"");
    if (hasDataType()) {
      str.append(", \"dataType\": ");
      getDataType().toString(str);
    }
    if (hasName()) {
      str.append(", \"name\": \"").append(getName()).append("\"");
    }
    if (hasTitle()) {
      str.append(", \"title\": \"").append(getTitle()).append("\"");
    }
    if (hasDescription()) {
      str.append(", \"description\": \"").append(getDescription()).append("\"");
    }
    str.append("}");
    return str;
  }
}