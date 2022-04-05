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
package com.epam.deltix.timebase.messages.qql;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Describes QQL function init argument
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.qql.InitArgument",
    title = "InitArgument"
)
public class InitArgument extends Argument implements RecordInterface {
  public static final String CLASS_NAME = InitArgument.class.getName();

  /**
   */
  protected CharSequence defaultValue = null;

  /**
   * @return Default Value
   */
  @SchemaElement
  @SchemaType
  public CharSequence getDefaultValue() {
    return defaultValue;
  }

  /**
   * @param value - Default Value
   */
  public void setDefaultValue(CharSequence value) {
    this.defaultValue = value;
  }

  /**
   * @return true if Default Value is not null
   */
  public boolean hasDefaultValue() {
    return defaultValue != null;
  }

  /**
   */
  public void nullifyDefaultValue() {
    this.defaultValue = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected InitArgument createInstance() {
    return new InitArgument();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public InitArgument nullify() {
    super.nullify();
    nullifyDefaultValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public InitArgument reset() {
    super.reset();
    defaultValue = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public InitArgument clone() {
    InitArgument t = createInstance();
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
    if (!(obj instanceof InitArgument)) return false;
    InitArgument other =(InitArgument)obj;
    if (hasDefaultValue() != other.hasDefaultValue()) return false;
    if (hasDefaultValue()) {
      if (getDefaultValue().length() != other.getDefaultValue().length()) return false; else {
        CharSequence s1 = getDefaultValue();
        CharSequence s2 = other.getDefaultValue();
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
    if (hasDefaultValue()) {
      hash = hash * 31 + getDefaultValue().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public InitArgument copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof InitArgument) {
      InitArgument t = (InitArgument)template;
      if (t.hasDefaultValue()) {
        if (hasDefaultValue() && getDefaultValue() instanceof StringBuilder) {
          ((StringBuilder)getDefaultValue()).setLength(0);
        } else {
          setDefaultValue(new StringBuilder());
        }
        ((StringBuilder)getDefaultValue()).append(t.getDefaultValue());
      } else {
        nullifyDefaultValue();
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
    str.append("{ \"$type\":  \"InitArgument\"");
    if (hasDefaultValue()) {
      str.append(", \"defaultValue\": \"").append(getDefaultValue()).append("\"");
    }
    if (hasName()) {
      str.append(", \"name\": \"").append(getName()).append("\"");
    }
    if (hasDataType()) {
      str.append(", \"dataType\": ");
      getDataType().toString(str);
    }
    str.append("}");
    return str;
  }
}
