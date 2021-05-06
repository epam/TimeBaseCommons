package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Schema definition of binary data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.BinaryDataType",
    title = "BinaryDataType"
)
public class BinaryDataType extends DataType implements BinaryDataTypeInterface {
  public static final String CLASS_NAME = BinaryDataType.class.getName();

  /**
   * Maximum field length in bytes.
   */
  protected int maxSize = TypeConstants.INT32_NULL;

  /**
   * Compression level for binary data.
   */
  protected short compressionLevel = TypeConstants.INT16_NULL;

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
    return maxSize != com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
  }

  /**
   * Maximum field length in bytes.
   */
  public void nullifyMaxSize() {
    this.maxSize = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
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
    return compressionLevel != com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Compression level for binary data.
   */
  public void nullifyCompressionLevel() {
    this.compressionLevel = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected BinaryDataType createInstance() {
    return new BinaryDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public BinaryDataType nullify() {
    super.nullify();
    nullifyMaxSize();
    nullifyCompressionLevel();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public BinaryDataType reset() {
    super.reset();
    maxSize = com.epam.deltix.timebase.messages.TypeConstants.INT32_NULL;
    compressionLevel = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public BinaryDataType clone() {
    BinaryDataType t = createInstance();
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
    if (!(obj instanceof BinaryDataTypeInfo)) return false;
    BinaryDataTypeInfo other =(BinaryDataTypeInfo)obj;
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
  public BinaryDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof BinaryDataTypeInfo) {
      BinaryDataTypeInfo t = (BinaryDataTypeInfo)template;
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
    str.append("{ \"$type\":  \"BinaryDataType\"");
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
    str.append("}");
    return str;
  }
}
