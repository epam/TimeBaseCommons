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
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Schema definition of char data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.CharFieldType",
    title = "CharFieldType"
)
public class CharFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = CharFieldType.class.getName();

  public CharFieldType() {
    super();
    baseName = "CHAR";
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
  protected CharFieldType createInstance() {
    return new CharFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public CharFieldType nullify() {
    super.nullify();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public CharFieldType reset() {
    super.reset();
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public CharFieldType clone() {
    CharFieldType t = createInstance();
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
    if (!(obj instanceof CharFieldType)) return false;
    CharFieldType other =(CharFieldType)obj;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public CharFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof CharFieldType) {
      CharFieldType t = (CharFieldType)template;
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
    str.append("{ \"$type\":  \"CharFieldType\"");
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
