package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Schema definition for static data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.StaticField",
    title = "StaticField"
)
public class StaticField extends Field implements RecordInterface {
  public static final String CLASS_NAME = StaticField.class.getName();

  /**
   * Value constant for a field.
   */
  protected CharSequence staticValue = null;

  /**
   * Value constant for a field.
   * @return Static Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getStaticValue() {
    return staticValue;
  }

  /**
   * Value constant for a field.
   * @param value - Static Value
   */
  public void setStaticValue(CharSequence value) {
    this.staticValue = value;
  }

  /**
   * Value constant for a field.
   * @return true if Static Value is not null
   */
  public boolean hasStaticValue() {
    return staticValue != null;
  }

  /**
   * Value constant for a field.
   */
  public void nullifyStaticValue() {
    this.staticValue = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected StaticField createInstance() {
    return new StaticField();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public StaticField nullify() {
    super.nullify();
    nullifyStaticValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public StaticField reset() {
    super.reset();
    staticValue = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public StaticField clone() {
    StaticField t = createInstance();
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
    if (!(obj instanceof StaticField)) return false;
    StaticField other =(StaticField)obj;
    if (hasStaticValue() != other.hasStaticValue()) return false;
    if (hasStaticValue()) {
      if (getStaticValue().length() != other.getStaticValue().length()) return false; else {
        CharSequence s1 = getStaticValue();
        CharSequence s2 = other.getStaticValue();
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
    if (hasStaticValue()) {
      hash = hash * 31 + getStaticValue().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public StaticField copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof StaticField) {
      StaticField t = (StaticField)template;
      if (t.hasStaticValue()) {
        if (hasStaticValue() && getStaticValue() instanceof StringBuilder) {
          ((StringBuilder)getStaticValue()).setLength(0);
        } else {
          setStaticValue(new StringBuilder());
        }
        ((StringBuilder)getStaticValue()).append(t.getStaticValue());
      } else {
        nullifyStaticValue();
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
    str.append("{ \"$type\":  \"StaticField\"");
    if (hasStaticValue()) {
      str.append(", \"staticValue\": \"").append(getStaticValue()).append("\"");
    }
    if (hasType()) {
      str.append(", \"type\": ");
      getType().toString(str);
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
