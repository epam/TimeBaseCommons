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
 * Schema definition of varchar data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.VarcharDataType",
    title = "VarcharDataType"
)
public class VarcharDataType extends DataType implements VarcharDataTypeInterface {
  public static final String CLASS_NAME = VarcharDataType.class.getName();

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
    return isMultiline != com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if field allows line breaks.
   */
  public void nullifyIsMultiline() {
    this.isMultiline = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
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
    return encodingType != com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
  }

  /**
   * Encoding type.
   */
  public void nullifyEncodingType() {
    this.encodingType = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
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
    return length != com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
  }

  /**
   * Length.
   */
  public void nullifyLength() {
    this.length = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected VarcharDataType createInstance() {
    return new VarcharDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public VarcharDataType nullify() {
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
  public VarcharDataType reset() {
    super.reset();
    isMultiline = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
    encodingType = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
    length = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public VarcharDataType clone() {
    VarcharDataType t = createInstance();
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
    if (!(obj instanceof VarcharDataTypeInfo)) return false;
    VarcharDataTypeInfo other =(VarcharDataTypeInfo)obj;
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
  public VarcharDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof VarcharDataTypeInfo) {
      VarcharDataTypeInfo t = (VarcharDataTypeInfo)template;
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
    str.append("{ \"$type\":  \"VarcharDataType\"");
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
    str.append("}");
    return str;
  }
}