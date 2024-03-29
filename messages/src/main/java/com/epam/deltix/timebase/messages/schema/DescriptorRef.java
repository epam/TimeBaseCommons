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
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Schema definition for a reference to class descriptor.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.DescriptorRef",
    title = "DescriptorRef"
)
public class DescriptorRef implements RecordInterface {
  public static final String CLASS_NAME = DescriptorRef.class.getName();

  /**
   * Node name.
   */
  protected CharSequence name = null;

  /**
   * Node name.
   * @return Name
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getName() {
    return name;
  }

  /**
   * Node name.
   * @param value - Name
   */
  public void setName(CharSequence value) {
    this.name = value;
  }

  /**
   * Node name.
   * @return true if Name is not null
   */
  public boolean hasName() {
    return name != null;
  }

  /**
   * Node name.
   */
  public void nullifyName() {
    this.name = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected DescriptorRef createInstance() {
    return new DescriptorRef();
  }

  /**
   * Method nullifies all instance properties
   */
  public DescriptorRef nullify() {
    nullifyName();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public DescriptorRef reset() {
    name = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public DescriptorRef clone() {
    DescriptorRef t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof DescriptorRef)) return false;
    DescriptorRef other =(DescriptorRef)obj;
    if (hasName() != other.hasName()) return false;
    if (hasName()) {
      if (getName().length() != other.getName().length()) return false; else {
        CharSequence s1 = getName();
        CharSequence s2 = other.getName();
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
    int hash = 0;
    if (hasName()) {
      hash = hash * 31 + getName().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public DescriptorRef copyFrom(RecordInfo template) {
    if (template instanceof DescriptorRef) {
      DescriptorRef t = (DescriptorRef)template;
      if (t.hasName()) {
        if (hasName() && getName() instanceof StringBuilder) {
          ((StringBuilder)getName()).setLength(0);
        } else {
          setName(new StringBuilder());
        }
        ((StringBuilder)getName()).append(t.getName());
      } else {
        nullifyName();
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
    str.append("{ \"$type\":  \"DescriptorRef\"");
    if (hasName()) {
      str.append(", \"name\": \"").append(getName()).append("\"");
    }
    str.append("}");
    return str;
  }
}
