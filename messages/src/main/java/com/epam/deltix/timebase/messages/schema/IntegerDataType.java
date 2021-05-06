package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Schema definition of integer data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.IntegerDataType",
    title = "IntegerDataType"
)
public class IntegerDataType extends IntegralDataType implements IntegerDataTypeInterface {
  public static final String CLASS_NAME = IntegerDataType.class.getName();

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected IntegerDataType createInstance() {
    return new IntegerDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public IntegerDataType nullify() {
    super.nullify();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public IntegerDataType reset() {
    super.reset();
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public IntegerDataType clone() {
    IntegerDataType t = createInstance();
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
    if (!(obj instanceof IntegerDataTypeInfo)) return false;
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
  public IntegerDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
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
    str.append("{ \"$type\":  \"IntegerDataType\"");
    if (hasMinValue()) {
      str.append(", \"minValue\": \"").append(getMinValue()).append("\"");
    }
    if (hasMaxValue()) {
      str.append(", \"maxValue\": \"").append(getMaxValue()).append("\"");
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
