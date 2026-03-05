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
 * Schema definition of enum data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.EnumFieldType",
    title = "EnumFieldType"
)
public class EnumFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = EnumFieldType.class.getName();

  /**
   * Definition for enumeration class.
   */
  protected DescriptorRef typeDescriptor = null;

  public EnumFieldType() {
    super();
    baseName = "ENUM";
  }

  /**
   * Definition for enumeration class.
   * @return Type Descriptor
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            DescriptorRef.class}

  )
  public DescriptorRef getTypeDescriptor() {
    return typeDescriptor;
  }

  /**
   * Definition for enumeration class.
   * @param value - Type Descriptor
   */
  public void setTypeDescriptor(DescriptorRef value) {
    this.typeDescriptor = value;
  }

  /**
   * Definition for enumeration class.
   * @return true if Type Descriptor is not null
   */
  public boolean hasTypeDescriptor() {
    return typeDescriptor != null;
  }

  /**
   * Definition for enumeration class.
   */
  public void nullifyTypeDescriptor() {
    this.typeDescriptor = null;
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
  protected EnumFieldType createInstance() {
    return new EnumFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public EnumFieldType nullify() {
    super.nullify();
    nullifyTypeDescriptor();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public EnumFieldType reset() {
    super.reset();
    typeDescriptor = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public EnumFieldType clone() {
    EnumFieldType t = createInstance();
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
    if (!(obj instanceof EnumFieldType)) return false;
    EnumFieldType other =(EnumFieldType)obj;
    if (hasTypeDescriptor() != other.hasTypeDescriptor()) return false;
    if (hasTypeDescriptor() && !(getTypeDescriptor().equals(other.getTypeDescriptor()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasTypeDescriptor()) {
      hash = hash * 31 + getTypeDescriptor().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public EnumFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof EnumFieldType) {
      EnumFieldType t = (EnumFieldType)template;
      if (t.hasTypeDescriptor()) {
        t.setTypeDescriptor((DescriptorRef) getTypeDescriptor().clone());
      } else {
        nullifyTypeDescriptor();
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
    str.append("{ \"$type\":  \"EnumFieldType\"");
    if (hasTypeDescriptor()) {
      str.append(", \"typeDescriptor\": ");
      getTypeDescriptor().toString(str);
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
