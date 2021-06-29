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
 * This is a base class for schema nodes.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.NamedDescriptor",
    title = "NamedDescriptor"
)
public abstract class NamedDescriptor implements NamedDescriptorInterface {
  public static final String CLASS_NAME = NamedDescriptor.class.getName();

  /**
   * Node name.
   */
  protected CharSequence name = null;

  /**
   * Node title.
   */
  protected CharSequence title = null;

  /**
   * Node description.
   */
  protected CharSequence description = null;

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
   * Node title.
   * @return Title
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getTitle() {
    return title;
  }

  /**
   * Node title.
   * @param value - Title
   */
  public void setTitle(CharSequence value) {
    this.title = value;
  }

  /**
   * Node title.
   * @return true if Title is not null
   */
  public boolean hasTitle() {
    return title != null;
  }

  /**
   * Node title.
   */
  public void nullifyTitle() {
    this.title = null;
  }

  /**
   * Node description.
   * @return Description
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getDescription() {
    return description;
  }

  /**
   * Node description.
   * @param value - Description
   */
  public void setDescription(CharSequence value) {
    this.description = value;
  }

  /**
   * Node description.
   * @return true if Description is not null
   */
  public boolean hasDescription() {
    return description != null;
  }

  /**
   * Node description.
   */
  public void nullifyDescription() {
    this.description = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected NamedDescriptor createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  public NamedDescriptor nullify() {
    nullifyName();
    nullifyTitle();
    nullifyDescription();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public NamedDescriptor reset() {
    name = null;
    title = null;
    description = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public NamedDescriptor clone() {
    NamedDescriptor t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof NamedDescriptorInfo)) return false;
    NamedDescriptorInfo other =(NamedDescriptorInfo)obj;
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
    if (hasTitle() != other.hasTitle()) return false;
    if (hasTitle()) {
      if (getTitle().length() != other.getTitle().length()) return false; else {
        CharSequence s1 = getTitle();
        CharSequence s2 = other.getTitle();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasDescription() != other.hasDescription()) return false;
    if (hasDescription()) {
      if (getDescription().length() != other.getDescription().length()) return false; else {
        CharSequence s1 = getDescription();
        CharSequence s2 = other.getDescription();
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
    if (hasTitle()) {
      hash = hash * 31 + getTitle().hashCode();
    }
    if (hasDescription()) {
      hash = hash * 31 + getDescription().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public NamedDescriptor copyFrom(RecordInfo template) {
    if (template instanceof NamedDescriptorInfo) {
      NamedDescriptorInfo t = (NamedDescriptorInfo)template;
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
      if (t.hasTitle()) {
        if (hasTitle() && getTitle() instanceof StringBuilder) {
          ((StringBuilder)getTitle()).setLength(0);
        } else {
          setTitle(new StringBuilder());
        }
        ((StringBuilder)getTitle()).append(t.getTitle());
      } else {
        nullifyTitle();
      }
      if (t.hasDescription()) {
        if (hasDescription() && getDescription() instanceof StringBuilder) {
          ((StringBuilder)getDescription()).setLength(0);
        } else {
          setDescription(new StringBuilder());
        }
        ((StringBuilder)getDescription()).append(t.getDescription());
      } else {
        nullifyDescription();
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
    str.append("{ \"$type\":  \"NamedDescriptor\"");
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