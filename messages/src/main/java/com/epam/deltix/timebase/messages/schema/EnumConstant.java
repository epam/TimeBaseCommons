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

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.*;

/**
 * Schema definition for a single enumeration value.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.EnumConstant",
    title = "EnumConstant"
)
public class EnumConstant implements RecordInterface {
  public static final String CLASS_NAME = EnumConstant.class.getName();

  /**
   * Enumeration symbol.
   */
  protected CharSequence symbol = null;

  /**
   * Enumeration value.
   */
  protected short value = TypeConstants.INT16_NULL;

  /**
   * Enumeration symbol.
   * @return Symbol
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getSymbol() {
    return symbol;
  }

  /**
   * Enumeration symbol.
   * @param value - Symbol
   */
  public void setSymbol(CharSequence value) {
    this.symbol = value;
  }

  /**
   * Enumeration symbol.
   * @return true if Symbol is not null
   */
  public boolean hasSymbol() {
    return symbol != null;
  }

  /**
   * Enumeration symbol.
   */
  public void nullifySymbol() {
    this.symbol = null;
  }

  /**
   * Enumeration value.
   * @return Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public short getValue() {
    return value;
  }

  /**
   * Enumeration value.
   * @param value - Value
   */
  public void setValue(short value) {
    this.value = value;
  }

  /**
   * Enumeration value.
   * @return true if Value is not null
   */
  public boolean hasValue() {
    return value != TypeConstants.INT16_NULL;
  }

  /**
   * Enumeration value.
   */
  public void nullifyValue() {
    this.value = TypeConstants.INT16_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected EnumConstant createInstance() {
    return new EnumConstant();
  }

  /**
   * Method nullifies all instance properties
   */
  public EnumConstant nullify() {
    nullifySymbol();
    nullifyValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public EnumConstant reset() {
    symbol = null;
    value = TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public EnumConstant clone() {
    EnumConstant t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof EnumConstant)) return false;
    EnumConstant other =(EnumConstant)obj;
    if (hasSymbol() != other.hasSymbol()) return false;
    if (hasSymbol()) {
      if (getSymbol().length() != other.getSymbol().length()) return false; else {
        CharSequence s1 = getSymbol();
        CharSequence s2 = other.getSymbol();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasValue() != other.hasValue()) return false;
    if (hasValue() && getValue() != other.getValue()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasSymbol()) {
      hash = hash * 31 + getSymbol().hashCode();
    }
    if (hasValue()) {
      hash = hash * 31 + ((int)getValue());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public EnumConstant copyFrom(RecordInfo template) {
    if (template instanceof EnumConstant) {
      EnumConstant t = (EnumConstant)template;
      if (t.hasSymbol()) {
        if (hasSymbol() && getSymbol() instanceof StringBuilder) {
          ((StringBuilder)getSymbol()).setLength(0);
        } else {
          setSymbol(new StringBuilder());
        }
        ((StringBuilder)getSymbol()).append(t.getSymbol());
      } else {
        nullifySymbol();
      }
      if (t.hasValue()) {
        setValue(t.getValue());
      } else {
        nullifyValue();
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
    str.append("{ \"$type\":  \"EnumConstant\"");
    if (hasSymbol()) {
      str.append(", \"symbol\": \"").append(getSymbol()).append("\"");
    }
    if (hasValue()) {
      str.append(", \"value\": ").append(getValue());
    }
    str.append("}");
    return str;
  }
}
