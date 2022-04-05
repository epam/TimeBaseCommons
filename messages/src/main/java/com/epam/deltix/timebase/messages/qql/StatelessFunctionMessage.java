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
import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.timebase.messages.schema.FieldType;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Message that describes QQL stateless function
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.qql.StatelessFunctionMessage",
    title = "StatelessFunctionMessage"
)
public class StatelessFunctionMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = StatelessFunctionMessage.class.getName();

  /**
   * Function identifier
   */
  protected CharSequence id = null;

  /**
   */
  protected ObjectArrayList<Argument> arguments = null;

  /**
   */
  protected FieldType returnType = null;

  /**
   * Function identifier
   * @return Id
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getId() {
    return id;
  }

  /**
   * Function identifier
   * @param value - Id
   */
  public void setId(CharSequence value) {
    this.id = value;
  }

  /**
   * Function identifier
   * @return true if Id is not null
   */
  public boolean hasId() {
    return id != null;
  }

  /**
   * Function identifier
   */
  public void nullifyId() {
    this.id = null;
  }

  /**
   * @return Arguments
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            Argument.class}

  )
  public ObjectArrayList<Argument> getArguments() {
    return arguments;
  }

  /**
   * @param value - Arguments
   */
  public void setArguments(ObjectArrayList<Argument> value) {
    this.arguments = value;
  }

  /**
   * @return true if Arguments is not null
   */
  public boolean hasArguments() {
    return arguments != null;
  }

  /**
   */
  public void nullifyArguments() {
    this.arguments = null;
  }

  /**
   * @return Return Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            FieldType.class}

  )
  public FieldType getReturnType() {
    return returnType;
  }

  /**
   * @param value - Return Type
   */
  public void setReturnType(FieldType value) {
    this.returnType = value;
  }

  /**
   * @return true if Return Type is not null
   */
  public boolean hasReturnType() {
    return returnType != null;
  }

  /**
   */
  public void nullifyReturnType() {
    this.returnType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected StatelessFunctionMessage createInstance() {
    return new StatelessFunctionMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public StatelessFunctionMessage nullify() {
    super.nullify();
    nullifyId();
    nullifyArguments();
    nullifyReturnType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public StatelessFunctionMessage reset() {
    super.reset();
    id = null;
    arguments = null;
    returnType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public StatelessFunctionMessage clone() {
    StatelessFunctionMessage t = createInstance();
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
    if (!(obj instanceof StatelessFunctionMessage)) return false;
    StatelessFunctionMessage other =(StatelessFunctionMessage)obj;
    if (hasId() != other.hasId()) return false;
    if (hasId()) {
      if (getId().length() != other.getId().length()) return false; else {
        CharSequence s1 = getId();
        CharSequence s2 = other.getId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasArguments() != other.hasArguments()) return false;
    if (hasArguments()) {
      if (getArguments().size() != other.getArguments().size()) return false;
      else for (int j = 0; j < getArguments().size(); ++j) {
        if ((getArguments().get(j) != null) != (other.getArguments().get(j) != null)) return false;
        if (getArguments().get(j) != null && !getArguments().get(j).equals(other.getArguments().get(j))) return false;
      }
    }
    if (hasReturnType() != other.hasReturnType()) return false;
    if (hasReturnType() && !(getReturnType().equals(other.getReturnType()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasId()) {
      hash = hash * 31 + getId().hashCode();
    }
    if (hasArguments()) {
      for (int j = 0; j < getArguments().size(); ++j) {
        hash ^= getArguments().get(j).hashCode();
      }
    }
    if (hasReturnType()) {
      hash = hash * 31 + getReturnType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public StatelessFunctionMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof StatelessFunctionMessage) {
      StatelessFunctionMessage t = (StatelessFunctionMessage)template;
      if (t.hasId()) {
        if (hasId() && getId() instanceof StringBuilder) {
          ((StringBuilder)getId()).setLength(0);
        } else {
          setId(new StringBuilder());
        }
        ((StringBuilder)getId()).append(t.getId());
      } else {
        nullifyId();
      }
      if (t.hasArguments()) {
        if (!hasArguments()) {
          setArguments(new ObjectArrayList<Argument>(t.getArguments().size()));
        } else {
          getArguments().clear();
        }
        for (int i = 0; i < t.getArguments().size(); ++i) ((ObjectArrayList<Argument>)getArguments()).add((Argument)t.getArguments().get(i).clone());
      } else {
        nullifyArguments();
      }
      if (t.hasReturnType()) {
        t.setReturnType((FieldType) getReturnType().clone());
      } else {
        nullifyReturnType();
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
    str.append("{ \"$type\":  \"StatelessFunctionMessage\"");
    if (hasId()) {
      str.append(", \"id\": \"").append(getId()).append("\"");
    }
    if (hasArguments()) {
      str.append(", \"arguments\": [");
      if (getArguments().size() > 0) {
        if (getArguments().get(0) == null) {
          str.append("null");
        } else {
          getArguments().get(0).toString(str);
        }
      }
      for (int i = 1; i < getArguments().size(); ++i) {
        str.append(", ");
        if (getArguments().get(i) == null) {
          str.append("null");
        } else {
          getArguments().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasReturnType()) {
      str.append(", \"returnType\": ");
      getReturnType().toString(str);
    }
    if (hasTimeStampMs()) {
      str.append(", \"timeStampMs\": \"").append(getTimeStampMs()).append("\"");
    }
    if (hasSymbol()) {
      str.append(", \"symbol\": \"").append(getSymbol()).append("\"");
    }
    str.append("}");
    return str;
  }
}
