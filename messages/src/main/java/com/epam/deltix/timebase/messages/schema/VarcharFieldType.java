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
 * Schema definition of varchar data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.VarcharFieldType",
    title = "VarcharFieldType"
)
public class VarcharFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = VarcharFieldType.class.getName();

  /**
   * True, if field allows line breaks.
   */
  protected byte isMultiline = TypeConstants.BOOLEAN_NULL;

  /**
   * Encoding type.
   */
  protected int encodingType = TypeConstants.INT32_NULL;

  /**
   * Length.
   */
  protected int length = TypeConstants.INT32_NULL;

  public VarcharFieldType() {
    super();
    baseName = "VARCHAR";
  }

  /**
   * True, if field allows line breaks.
   * @return Is Multiline
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public boolean isMultiline() {
    return isMultiline == 1;
  }

  /**
   * True, if field allows line breaks.
   * @param value - Is Multiline
   */
  public void setIsMultiline(boolean value) {
    this.isMultiline = (byte)(value ? 1 : 0);
  }

  /**
   * True, if field allows line breaks.
   * @return true if Is Multiline is not null
   */
  public boolean hasIsMultiline() {
    return isMultiline != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if field allows line breaks.
   */
  public void nullifyIsMultiline() {
    this.isMultiline = TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Encoding type.
   * @return Encoding Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public int getEncodingType() {
    return encodingType;
  }

  /**
   * Encoding type.
   * @param value - Encoding Type
   */
  public void setEncodingType(int value) {
    this.encodingType = value;
  }

  /**
   * Encoding type.
   * @return true if Encoding Type is not null
   */
  public boolean hasEncodingType() {
    return encodingType != TypeConstants.INT32_NULL;
  }

  /**
   * Encoding type.
   */
  public void nullifyEncodingType() {
    this.encodingType = TypeConstants.INT32_NULL;
  }

  /**
   * Length.
   * @return Length
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public int getLength() {
    return length;
  }

  /**
   * Length.
   * @param value - Length
   */
  public void setLength(int value) {
    this.length = value;
  }

  /**
   * Length.
   * @return true if Length is not null
   */
  public boolean hasLength() {
    return length != TypeConstants.INT32_NULL;
  }

  /**
   * Length.
   */
  public void nullifyLength() {
    this.length = TypeConstants.INT32_NULL;
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
  protected VarcharFieldType createInstance() {
    return new VarcharFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public VarcharFieldType nullify() {
    super.nullify();
    nullifyIsMultiline();
    nullifyEncodingType();
    nullifyLength();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public VarcharFieldType reset() {
    super.reset();
    isMultiline = TypeConstants.BOOLEAN_NULL;
    encodingType = TypeConstants.INT32_NULL;
    length = TypeConstants.INT32_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public VarcharFieldType clone() {
    VarcharFieldType t = createInstance();
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
    if (!(obj instanceof VarcharFieldType)) return false;
    VarcharFieldType other =(VarcharFieldType)obj;
    if (hasIsMultiline() != other.hasIsMultiline()) return false;
    if (hasIsMultiline() && isMultiline() != other.isMultiline()) return false;
    if (hasEncodingType() != other.hasEncodingType()) return false;
    if (hasEncodingType() && getEncodingType() != other.getEncodingType()) return false;
    if (hasLength() != other.hasLength()) return false;
    if (hasLength() && getLength() != other.getLength()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasIsMultiline()) {
      hash = hash * 31 + (isMultiline() ? 1231 : 1237);
    }
    if (hasEncodingType()) {
      hash = hash * 31 + (getEncodingType());
    }
    if (hasLength()) {
      hash = hash * 31 + (getLength());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public VarcharFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof VarcharFieldType) {
      VarcharFieldType t = (VarcharFieldType)template;
      if (t.hasIsMultiline()) {
        setIsMultiline(t.isMultiline());
      } else {
        nullifyIsMultiline();
      }
      if (t.hasEncodingType()) {
        setEncodingType(t.getEncodingType());
      } else {
        nullifyEncodingType();
      }
      if (t.hasLength()) {
        setLength(t.getLength());
      } else {
        nullifyLength();
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
    str.append("{ \"$type\":  \"VarcharFieldType\"");
    if (hasIsMultiline()) {
      str.append(", \"isMultiline\": ").append(isMultiline());
    }
    if (hasEncodingType()) {
      str.append(", \"encodingType\": ").append(getEncodingType());
    }
    if (hasLength()) {
      str.append(", \"length\": ").append(getLength());
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
