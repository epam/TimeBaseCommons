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
import com.epam.deltix.timebase.messages.SchemaArrayType;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Schema definition of class data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.ClassFieldType",
    title = "ClassFieldType"
)
public class ClassFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = ClassFieldType.class.getName();

  /**
   * Definitions for nested schema classes.
   */
  protected ObjectArrayList<DescriptorRef> typeDescriptors = null;

  public ClassFieldType() {
    super();
    baseName = "OBJECT";
  }

  /**
   * Definitions for nested schema classes.
   * @return Type Descriptors
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            DescriptorRef.class}

  )
  public ObjectArrayList<DescriptorRef> getTypeDescriptors() {
    return typeDescriptors;
  }

  /**
   * Definitions for nested schema classes.
   * @param value - Type Descriptors
   */
  public void setTypeDescriptors(ObjectArrayList<DescriptorRef> value) {
    this.typeDescriptors = value;
  }

  /**
   * Definitions for nested schema classes.
   * @return true if Type Descriptors is not null
   */
  public boolean hasTypeDescriptors() {
    return typeDescriptors != null;
  }

  /**
   * Definitions for nested schema classes.
   */
  public void nullifyTypeDescriptors() {
    this.typeDescriptors = null;
  }

  /**
   * @return Base Name
   */
  @Override
  public CharSequence getBaseName() {
    return baseName;
  }

  /**
   * @param value - Base Name
   */
  @Override
  public void setBaseName(CharSequence value) {
    this.baseName = value;
  }

  /**
   * @return true if Base Name is not null
   */
  @Override
  public boolean hasBaseName() {
    return baseName != null;
  }

  /**
   */
  @Override
  public void nullifyBaseName() {
    this.baseName = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected ClassFieldType createInstance() {
    return new ClassFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public ClassFieldType nullify() {
    super.nullify();
    nullifyTypeDescriptors();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public ClassFieldType reset() {
    super.reset();
    typeDescriptors = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public ClassFieldType clone() {
    ClassFieldType t = createInstance();
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
    if (!(obj instanceof ClassFieldType)) return false;
    ClassFieldType other =(ClassFieldType)obj;
    if (hasTypeDescriptors() != other.hasTypeDescriptors()) return false;
    if (hasTypeDescriptors()) {
      if (getTypeDescriptors().size() != other.getTypeDescriptors().size()) return false;
      else for (int j = 0; j < getTypeDescriptors().size(); ++j) {
        if ((getTypeDescriptors().get(j) != null) != (other.getTypeDescriptors().get(j) != null)) return false;
        if (getTypeDescriptors().get(j) != null && !getTypeDescriptors().get(j).equals(other.getTypeDescriptors().get(j))) return false;
      }
    }
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasTypeDescriptors()) {
      for (int j = 0; j < getTypeDescriptors().size(); ++j) {
        hash ^= getTypeDescriptors().get(j).hashCode();
      }
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public ClassFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof ClassFieldType) {
      ClassFieldType t = (ClassFieldType)template;
      if (t.hasTypeDescriptors()) {
        if (!hasTypeDescriptors()) {
          setTypeDescriptors(new ObjectArrayList<DescriptorRef>(t.getTypeDescriptors().size()));
        } else {
          getTypeDescriptors().clear();
        }
        for (int i = 0; i < t.getTypeDescriptors().size(); ++i) ((ObjectArrayList<DescriptorRef>)getTypeDescriptors()).add((DescriptorRef)t.getTypeDescriptors().get(i).clone());
      } else {
        nullifyTypeDescriptors();
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
    str.append("{ \"$type\":  \"ClassFieldType\"");
    if (hasTypeDescriptors()) {
      str.append(", \"typeDescriptors\": [");
      if (getTypeDescriptors().size() > 0) {
        if (getTypeDescriptors().get(0) == null) {
          str.append("null");
        } else {
          getTypeDescriptors().get(0).toString(str);
        }
      }
      for (int i = 1; i < getTypeDescriptors().size(); ++i) {
        str.append(", ");
        if (getTypeDescriptors().get(i) == null) {
          str.append("null");
        } else {
          getTypeDescriptors().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasEncoding()) {
      str.append(", \"encoding\": \"").append(getEncoding()).append("\"");
    }
    if (hasIsNullable()) {
      str.append(", \"isNullable\": ").append(isNullable());
    }
    if (hasBaseName()) {
      str.append(", \"baseName\": \"").append(getBaseName()).append("\"");
    }
    str.append("}");
    return str;
  }
}
