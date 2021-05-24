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
import java.lang.CharSequence;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.UnsupportedOperationException;
import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;

/**
 * This is a base class for schema data types.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.DataType",
    title = "DataType"
)
public abstract class DataType implements DataTypeInterface {
  public static final String CLASS_NAME = DataType.class.getName();

  /**
   * Binary representation (encoding) of a field in database and protocol.
   */
  protected CharSequence encoding = null;

  /**
   * True, if schema design allows the field to be nullable.
   */
  protected byte isNullable = TypeConstants.BOOLEAN_NULL;

  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @return Encoding
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getEncoding() {
    return encoding;
  }

  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @param value - Encoding
   */
  public void setEncoding(CharSequence value) {
    this.encoding = value;
  }

  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @return true if Encoding is not null
   */
  public boolean hasEncoding() {
    return encoding != null;
  }

  /**
   * Binary representation (encoding) of a field in database and protocol.
   */
  public void nullifyEncoding() {
    this.encoding = null;
  }

  /**
   * True, if schema design allows the field to be nullable.
   * @return Is Nullable
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public boolean isNullable() {
    return isNullable == 1;
  }

  /**
   * True, if schema design allows the field to be nullable.
   * @param value - Is Nullable
   */
  public void setIsNullable(boolean value) {
    this.isNullable = (byte)(value ? 1 : 0);
  }

  /**
   * True, if schema design allows the field to be nullable.
   * @return true if Is Nullable is not null
   */
  public boolean hasIsNullable() {
    return isNullable != com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if schema design allows the field to be nullable.
   */
  public void nullifyIsNullable() {
    this.isNullable = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected DataType createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  public DataType nullify() {
    nullifyEncoding();
    nullifyIsNullable();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public DataType reset() {
    encoding = null;
    isNullable = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public DataType clone() {
    DataType t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof DataTypeInfo)) return false;
    DataTypeInfo other =(DataTypeInfo)obj;
    if (hasEncoding() != other.hasEncoding()) return false;
    if (hasEncoding()) {
      if (getEncoding().length() != other.getEncoding().length()) return false; else {
        CharSequence s1 = getEncoding();
        CharSequence s2 = other.getEncoding();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasIsNullable() != other.hasIsNullable()) return false;
    if (hasIsNullable() && isNullable() != other.isNullable()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasEncoding()) {
      hash = hash * 31 + getEncoding().hashCode();
    }
    if (hasIsNullable()) {
      hash = hash * 31 + (isNullable() ? 1231 : 1237);
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public DataType copyFrom(RecordInfo template) {
    if (template instanceof DataTypeInfo) {
      DataTypeInfo t = (DataTypeInfo)template;
      if (t.hasEncoding()) {
        if (hasEncoding() && getEncoding() instanceof StringBuilder) {
          ((StringBuilder)getEncoding()).setLength(0);
        } else {
          setEncoding(new StringBuilder());
        }
        ((StringBuilder)getEncoding()).append(t.getEncoding());
      } else {
        nullifyEncoding();
      }
      if (t.hasIsNullable()) {
        setIsNullable(t.isNullable());
      } else {
        nullifyIsNullable();
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
    str.append("{ \"$type\":  \"DataType\"");
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