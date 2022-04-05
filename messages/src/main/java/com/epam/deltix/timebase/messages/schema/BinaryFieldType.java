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

/**
 * Schema definition of binary data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.BinaryFieldType",
    title = "BinaryFieldType"
)
public class BinaryFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = BinaryFieldType.class.getName();

  /**
   * Maximum field length in bytes.
   */
  protected int maxSize = TypeConstants.INT32_NULL;

  /**
   * Compression level for binary data.
   */
  protected short compressionLevel = TypeConstants.INT16_NULL;

  public BinaryFieldType() {
    super();
    baseName = "BINARY";
  }

  /**
   * Maximum field length in bytes.
   * @return Max Size
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * Maximum field length in bytes.
   * @param value - Max Size
   */
  public void setMaxSize(int value) {
    this.maxSize = value;
  }

  /**
   * Maximum field length in bytes.
   * @return true if Max Size is not null
   */
  public boolean hasMaxSize() {
    return maxSize != TypeConstants.INT32_NULL;
  }

  /**
   * Maximum field length in bytes.
   */
  public void nullifyMaxSize() {
    this.maxSize = TypeConstants.INT32_NULL;
  }

  /**
   * Compression level for binary data.
   * @return Compression Level
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      minimum = "0",
      maximum = "9"
  )
  public short getCompressionLevel() {
    return compressionLevel;
  }

  /**
   * Compression level for binary data.
   * @param value - Compression Level
   */
  public void setCompressionLevel(short value) {
    this.compressionLevel = value;
  }

  /**
   * Compression level for binary data.
   * @return true if Compression Level is not null
   */
  public boolean hasCompressionLevel() {
    return compressionLevel != TypeConstants.INT16_NULL;
  }

  /**
   * Compression level for binary data.
   */
  public void nullifyCompressionLevel() {
    this.compressionLevel = TypeConstants.INT16_NULL;
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
  protected BinaryFieldType createInstance() {
    return new BinaryFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public BinaryFieldType nullify() {
    super.nullify();
    nullifyMaxSize();
    nullifyCompressionLevel();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public BinaryFieldType reset() {
    super.reset();
    maxSize = TypeConstants.INT32_NULL;
    compressionLevel = TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public BinaryFieldType clone() {
    BinaryFieldType t = createInstance();
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
    if (!(obj instanceof BinaryFieldType)) return false;
    BinaryFieldType other =(BinaryFieldType)obj;
    if (hasMaxSize() != other.hasMaxSize()) return false;
    if (hasMaxSize() && getMaxSize() != other.getMaxSize()) return false;
    if (hasCompressionLevel() != other.hasCompressionLevel()) return false;
    if (hasCompressionLevel() && getCompressionLevel() != other.getCompressionLevel()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasMaxSize()) {
      hash = hash * 31 + (getMaxSize());
    }
    if (hasCompressionLevel()) {
      hash = hash * 31 + ((int)getCompressionLevel());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public BinaryFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof BinaryFieldType) {
      BinaryFieldType t = (BinaryFieldType)template;
      if (t.hasMaxSize()) {
        setMaxSize(t.getMaxSize());
      } else {
        nullifyMaxSize();
      }
      if (t.hasCompressionLevel()) {
        setCompressionLevel(t.getCompressionLevel());
      } else {
        nullifyCompressionLevel();
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
    str.append("{ \"$type\":  \"BinaryFieldType\"");
    if (hasMaxSize()) {
      str.append(", \"maxSize\": ").append(getMaxSize());
    }
    if (hasCompressionLevel()) {
      str.append(", \"compressionLevel\": ").append(getCompressionLevel());
    }
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
