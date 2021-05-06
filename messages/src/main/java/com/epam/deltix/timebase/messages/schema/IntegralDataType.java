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
 * This is a base class for numeric data types.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.IntegralDataType",
    title = "IntegralDataType"
)
public abstract class IntegralDataType extends DataType implements IntegralDataTypeInterface {
  public static final String CLASS_NAME = IntegralDataType.class.getName();

  /**
   * String definition for minimum constraint.
   */
  protected CharSequence minValue = null;

  /**
   * String definition for maximum constraint.
   */
  protected CharSequence maxValue = null;

  /**
   * String definition for minimum constraint.
   * @return Min Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getMinValue() {
    return minValue;
  }

  /**
   * String definition for minimum constraint.
   * @param value - Min Value
   */
  public void setMinValue(CharSequence value) {
    this.minValue = value;
  }

  /**
   * String definition for minimum constraint.
   * @return true if Min Value is not null
   */
  public boolean hasMinValue() {
    return minValue != null;
  }

  /**
   * String definition for minimum constraint.
   */
  public void nullifyMinValue() {
    this.minValue = null;
  }

  /**
   * String definition for maximum constraint.
   * @return Max Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getMaxValue() {
    return maxValue;
  }

  /**
   * String definition for maximum constraint.
   * @param value - Max Value
   */
  public void setMaxValue(CharSequence value) {
    this.maxValue = value;
  }

  /**
   * String definition for maximum constraint.
   * @return true if Max Value is not null
   */
  public boolean hasMaxValue() {
    return maxValue != null;
  }

  /**
   * String definition for maximum constraint.
   */
  public void nullifyMaxValue() {
    this.maxValue = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected IntegralDataType createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public IntegralDataType nullify() {
    super.nullify();
    nullifyMinValue();
    nullifyMaxValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public IntegralDataType reset() {
    super.reset();
    minValue = null;
    maxValue = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public IntegralDataType clone() {
    IntegralDataType t = createInstance();
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
    if (!(obj instanceof IntegralDataTypeInfo)) return false;
    IntegralDataTypeInfo other =(IntegralDataTypeInfo)obj;
    if (hasMinValue() != other.hasMinValue()) return false;
    if (hasMinValue()) {
      if (getMinValue().length() != other.getMinValue().length()) return false; else {
        CharSequence s1 = getMinValue();
        CharSequence s2 = other.getMinValue();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasMaxValue() != other.hasMaxValue()) return false;
    if (hasMaxValue()) {
      if (getMaxValue().length() != other.getMaxValue().length()) return false; else {
        CharSequence s1 = getMaxValue();
        CharSequence s2 = other.getMaxValue();
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
    if (hasMinValue()) {
      hash = hash * 31 + getMinValue().hashCode();
    }
    if (hasMaxValue()) {
      hash = hash * 31 + getMaxValue().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public IntegralDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof IntegralDataTypeInfo) {
      IntegralDataTypeInfo t = (IntegralDataTypeInfo)template;
      if (t.hasMinValue()) {
        if (hasMinValue() && getMinValue() instanceof StringBuilder) {
          ((StringBuilder)getMinValue()).setLength(0);
        } else {
          setMinValue(new StringBuilder());
        }
        ((StringBuilder)getMinValue()).append(t.getMinValue());
      } else {
        nullifyMinValue();
      }
      if (t.hasMaxValue()) {
        if (hasMaxValue() && getMaxValue() instanceof StringBuilder) {
          ((StringBuilder)getMaxValue()).setLength(0);
        } else {
          setMaxValue(new StringBuilder());
        }
        ((StringBuilder)getMaxValue()).append(t.getMaxValue());
      } else {
        nullifyMaxValue();
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
    str.append("{ \"$type\":  \"IntegralDataType\"");
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
