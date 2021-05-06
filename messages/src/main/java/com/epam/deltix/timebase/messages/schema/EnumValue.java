package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.CharSequence;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;

/**
 * Schema definition for a single enumeration value.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.EnumValue",
    title = "EnumValue"
)
public class EnumValue implements EnumValueInterface {
  public static final String CLASS_NAME = EnumValue.class.getName();

  /**
   * Enumeration symbol.
   */
  protected CharSequence symbol = null;

  /**
   * Enumeration value.
   */
  protected short value = TypeConstants.INT16_NULL;

  /**
   * Enumeration symbol.
   * @return Symbol
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getSymbol() {
    return symbol;
  }

  /**
   * Enumeration symbol.
   * @param value - Symbol
   */
  public void setSymbol(CharSequence value) {
    this.symbol = value;
  }

  /**
   * Enumeration symbol.
   * @return true if Symbol is not null
   */
  public boolean hasSymbol() {
    return symbol != null;
  }

  /**
   * Enumeration symbol.
   */
  public void nullifySymbol() {
    this.symbol = null;
  }

  /**
   * Enumeration value.
   * @return Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public short getValue() {
    return value;
  }

  /**
   * Enumeration value.
   * @param value - Value
   */
  public void setValue(short value) {
    this.value = value;
  }

  /**
   * Enumeration value.
   * @return true if Value is not null
   */
  public boolean hasValue() {
    return value != com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Enumeration value.
   */
  public void nullifyValue() {
    this.value = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected EnumValue createInstance() {
    return new EnumValue();
  }

  /**
   * Method nullifies all instance properties
   */
  public EnumValue nullify() {
    nullifySymbol();
    nullifyValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public EnumValue reset() {
    symbol = null;
    value = com.epam.deltix.timebase.messages.TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public EnumValue clone() {
    EnumValue t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof EnumValueInfo)) return false;
    EnumValueInfo other =(EnumValueInfo)obj;
    if (hasSymbol() != other.hasSymbol()) return false;
    if (hasSymbol()) {
      if (getSymbol().length() != other.getSymbol().length()) return false; else {
        CharSequence s1 = getSymbol();
        CharSequence s2 = other.getSymbol();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasValue() != other.hasValue()) return false;
    if (hasValue() && getValue() != other.getValue()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasSymbol()) {
      hash = hash * 31 + getSymbol().hashCode();
    }
    if (hasValue()) {
      hash = hash * 31 + ((int)getValue());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public EnumValue copyFrom(RecordInfo template) {
    if (template instanceof EnumValueInfo) {
      EnumValueInfo t = (EnumValueInfo)template;
      if (t.hasSymbol()) {
        if (hasSymbol() && getSymbol() instanceof StringBuilder) {
          ((StringBuilder)getSymbol()).setLength(0);
        } else {
          setSymbol(new StringBuilder());
        }
        ((StringBuilder)getSymbol()).append(t.getSymbol());
      } else {
        nullifySymbol();
      }
      if (t.hasValue()) {
        setValue(t.getValue());
      } else {
        nullifyValue();
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
    str.append("{ \"$type\":  \"EnumValue\"");
    if (hasSymbol()) {
      str.append(", \"symbol\": \"").append(getSymbol()).append("\"");
    }
    if (hasValue()) {
      str.append(", \"value\": ").append(getValue());
    }
    str.append("}");
    return str;
  }
}
