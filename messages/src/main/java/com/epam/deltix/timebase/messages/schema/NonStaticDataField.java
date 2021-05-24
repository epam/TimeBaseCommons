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
import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;

/**
 * Schema definition for non-static data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.NonStaticDataField",
    title = "NonStaticDataField"
)
public class NonStaticDataField extends DataField implements NonStaticDataFieldInterface {
  public static final String CLASS_NAME = NonStaticDataField.class.getName();

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   */
  protected byte isPrimaryKey = TypeConstants.BOOLEAN_NULL;

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   */
  protected CharSequence relativeTo = null;

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @return Is Primary Key
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public boolean isPrimaryKey() {
    return isPrimaryKey == 1;
  }

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @param value - Is Primary Key
   */
  public void setIsPrimaryKey(boolean value) {
    this.isPrimaryKey = (byte)(value ? 1 : 0);
  }

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   * @return true if Is Primary Key is not null
   */
  public boolean hasIsPrimaryKey() {
    return isPrimaryKey != com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if field is defined as a primary key.
   * The propery is applicable for unique streams only.
   */
  public void nullifyIsPrimaryKey() {
    this.isPrimaryKey = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @return Relative To
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getRelativeTo() {
    return relativeTo;
  }

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @param value - Relative To
   */
  public void setRelativeTo(CharSequence value) {
    this.relativeTo = value;
  }

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   * @return true if Relative To is not null
   */
  public boolean hasRelativeTo() {
    return relativeTo != null;
  }

  /**
   * If specified, the field value will be encoded as a difference with a field defined in a RelativeTo property.
   */
  public void nullifyRelativeTo() {
    this.relativeTo = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected NonStaticDataField createInstance() {
    return new NonStaticDataField();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public NonStaticDataField nullify() {
    super.nullify();
    nullifyIsPrimaryKey();
    nullifyRelativeTo();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public NonStaticDataField reset() {
    super.reset();
    isPrimaryKey = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
    relativeTo = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public NonStaticDataField clone() {
    NonStaticDataField t = createInstance();
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
    if (!(obj instanceof NonStaticDataFieldInfo)) return false;
    NonStaticDataFieldInfo other =(NonStaticDataFieldInfo)obj;
    if (hasIsPrimaryKey() != other.hasIsPrimaryKey()) return false;
    if (hasIsPrimaryKey() && isPrimaryKey() != other.isPrimaryKey()) return false;
    if (hasRelativeTo() != other.hasRelativeTo()) return false;
    if (hasRelativeTo()) {
      if (getRelativeTo().length() != other.getRelativeTo().length()) return false; else {
        CharSequence s1 = getRelativeTo();
        CharSequence s2 = other.getRelativeTo();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
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
    if (hasIsPrimaryKey()) {
      hash = hash * 31 + (isPrimaryKey() ? 1231 : 1237);
    }
    if (hasRelativeTo()) {
      hash = hash * 31 + getRelativeTo().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public NonStaticDataField copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof NonStaticDataFieldInfo) {
      NonStaticDataFieldInfo t = (NonStaticDataFieldInfo)template;
      if (t.hasIsPrimaryKey()) {
        setIsPrimaryKey(t.isPrimaryKey());
      } else {
        nullifyIsPrimaryKey();
      }
      if (t.hasRelativeTo()) {
        if (hasRelativeTo() && getRelativeTo() instanceof StringBuilder) {
          ((StringBuilder)getRelativeTo()).setLength(0);
        } else {
          setRelativeTo(new StringBuilder());
        }
        ((StringBuilder)getRelativeTo()).append(t.getRelativeTo());
      } else {
        nullifyRelativeTo();
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
    str.append("{ \"$type\":  \"NonStaticDataField\"");
    if (hasIsPrimaryKey()) {
      str.append(", \"isPrimaryKey\": ").append(isPrimaryKey());
    }
    if (hasRelativeTo()) {
      str.append(", \"relativeTo\": \"").append(getRelativeTo()).append("\"");
    }
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