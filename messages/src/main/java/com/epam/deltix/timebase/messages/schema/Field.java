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

import com.epam.deltix.timebase.messages.*;

/**
 * This is a base class for schema data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.Field",
    title = "Field"
)
public abstract class Field extends Descriptor implements RecordInterface {
  public static final String CLASS_NAME = Field.class.getName();

  /**
   * Data type of a field.
   */
  protected FieldType type = null;

  /**
   * Data type of a field.
   * @return Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            ArrayFieldType.class, BinaryFieldType.class, BooleanFieldType.class, CharFieldType.class, ClassFieldType.class, DateTimeFieldType.class, EnumFieldType.class, FloatFieldType.class, IntegerFieldType.class, TimeOfDayFieldType.class, VarcharFieldType.class}

  )
  public FieldType getType() {
    return type;
  }

  /**
   * Data type of a field.
   * @param value - Type
   */
  public void setType(FieldType value) {
    this.type = value;
  }

  /**
   * Data type of a field.
   * @return true if Type is not null
   */
  public boolean hasType() {
    return type != null;
  }

  /**
   * Data type of a field.
   */
  public void nullifyType() {
    this.type = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected Field createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public Field nullify() {
    super.nullify();
    nullifyType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public Field reset() {
    super.reset();
    type = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public Field clone() {
    Field t = createInstance();
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
    if (!(obj instanceof Field)) return false;
    Field other =(Field)obj;
    if (hasType() != other.hasType()) return false;
    if (hasType() && !(getType().equals(other.getType()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasType()) {
      hash = hash * 31 + getType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public Field copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof Field) {
      Field t = (Field)template;
      if (t.hasType()) {
        t.setType((FieldType) getType().clone());
      } else {
        nullifyType();
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
    str.append("{ \"$type\":  \"Field\"");
    if (hasType()) {
      str.append(", \"type\": ");
      getType().toString(str);
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
