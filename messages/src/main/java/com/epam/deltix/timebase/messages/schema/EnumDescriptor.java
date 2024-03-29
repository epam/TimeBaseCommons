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
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Schema definition for enumeration class.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.EnumDescriptor",
    title = "EnumDescriptor"
)
public class EnumDescriptor extends UniqueDescriptor implements RecordInterface {
  public static final String CLASS_NAME = EnumDescriptor.class.getName();

  /**
   * List of values of enumeration.
   */
  protected ObjectArrayList<EnumConstant> values = null;

  /**
   * True, if enumeration represents a bitmask.
   */
  protected byte isBitmask = TypeConstants.BOOLEAN_NULL;

  /**
   * List of values of enumeration.
   * @return Values
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            EnumConstant.class}

  )
  public ObjectArrayList<EnumConstant> getValues() {
    return values;
  }

  /**
   * List of values of enumeration.
   * @param value - Values
   */
  public void setValues(ObjectArrayList<EnumConstant> value) {
    this.values = value;
  }

  /**
   * List of values of enumeration.
   * @return true if Values is not null
   */
  public boolean hasValues() {
    return values != null;
  }

  /**
   * List of values of enumeration.
   */
  public void nullifyValues() {
    this.values = null;
  }

  /**
   * True, if enumeration represents a bitmask.
   * @return Is Bitmask
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public boolean isBitmask() {
    return isBitmask == 1;
  }

  /**
   * True, if enumeration represents a bitmask.
   * @param value - Is Bitmask
   */
  public void setIsBitmask(boolean value) {
    this.isBitmask = (byte)(value ? 1 : 0);
  }

  /**
   * True, if enumeration represents a bitmask.
   * @return true if Is Bitmask is not null
   */
  public boolean hasIsBitmask() {
    return isBitmask != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if enumeration represents a bitmask.
   */
  public void nullifyIsBitmask() {
    this.isBitmask = TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected EnumDescriptor createInstance() {
    return new EnumDescriptor();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public EnumDescriptor nullify() {
    super.nullify();
    nullifyValues();
    nullifyIsBitmask();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public EnumDescriptor reset() {
    super.reset();
    values = null;
    isBitmask = TypeConstants.BOOLEAN_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public EnumDescriptor clone() {
    EnumDescriptor t = createInstance();
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
    if (!(obj instanceof EnumDescriptor)) return false;
    EnumDescriptor other =(EnumDescriptor)obj;
    if (hasValues() != other.hasValues()) return false;
    if (hasValues()) {
      if (getValues().size() != other.getValues().size()) return false;
      else for (int j = 0; j < getValues().size(); ++j) {
        if ((getValues().get(j) != null) != (other.getValues().get(j) != null)) return false;
        if (getValues().get(j) != null && !getValues().get(j).equals(other.getValues().get(j))) return false;
      }
    }
    if (hasIsBitmask() != other.hasIsBitmask()) return false;
    if (hasIsBitmask() && isBitmask() != other.isBitmask()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasValues()) {
      for (int j = 0; j < getValues().size(); ++j) {
        hash ^= getValues().get(j).hashCode();
      }
    }
    if (hasIsBitmask()) {
      hash = hash * 31 + (isBitmask() ? 1231 : 1237);
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public EnumDescriptor copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof EnumDescriptor) {
      EnumDescriptor t = (EnumDescriptor)template;
      if (t.hasValues()) {
        if (!hasValues()) {
          setValues(new ObjectArrayList<EnumConstant>(t.getValues().size()));
        } else {
          getValues().clear();
        }
        for (int i = 0; i < t.getValues().size(); ++i) ((ObjectArrayList<EnumConstant>)getValues()).add((EnumConstant)t.getValues().get(i).clone());
      } else {
        nullifyValues();
      }
      if (t.hasIsBitmask()) {
        setIsBitmask(t.isBitmask());
      } else {
        nullifyIsBitmask();
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
    str.append("{ \"$type\":  \"EnumDescriptor\"");
    if (hasValues()) {
      str.append(", \"values\": [");
      if (getValues().size() > 0) {
        if (getValues().get(0) == null) {
          str.append("null");
        } else {
          getValues().get(0).toString(str);
        }
      }
      for (int i = 1; i < getValues().size(); ++i) {
        str.append(", ");
        if (getValues().get(i) == null) {
          str.append("null");
        } else {
          getValues().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasIsBitmask()) {
      str.append(", \"isBitmask\": ").append(isBitmask());
    }
    if (hasGuid()) {
      str.append(", \"guid\": \"").append(getGuid()).append("\"");
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
