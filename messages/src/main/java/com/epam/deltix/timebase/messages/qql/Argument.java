package com.epam.deltix.timebase.messages.qql;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.timebase.messages.schema.FieldType;

/**
 * Describes QQL function argument
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.qql.Argument",
    title = "Argument"
)
public class Argument implements RecordInterface {
  public static final String CLASS_NAME = Argument.class.getName();

  /**
   * Argument name
   */
  protected CharSequence name = null;

  /**
   * Data type
   */
  protected FieldType dataType = null;

  /**
   * Argument name
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
   * Argument name
   * @param value - Name
   */
  public void setName(CharSequence value) {
    this.name = value;
  }

  /**
   * Argument name
   * @return true if Name is not null
   */
  public boolean hasName() {
    return name != null;
  }

  /**
   * Argument name
   */
  public void nullifyName() {
    this.name = null;
  }

  /**
   * Data type
   * @return Data Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            FieldType.class}

  )
  public FieldType getDataType() {
    return dataType;
  }

  /**
   * Data type
   * @param value - Data Type
   */
  public void setDataType(FieldType value) {
    this.dataType = value;
  }

  /**
   * Data type
   * @return true if Data Type is not null
   */
  public boolean hasDataType() {
    return dataType != null;
  }

  /**
   * Data type
   */
  public void nullifyDataType() {
    this.dataType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected Argument createInstance() {
    return new Argument();
  }

  /**
   * Method nullifies all instance properties
   */
  public Argument nullify() {
    nullifyName();
    nullifyDataType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public Argument reset() {
    name = null;
    dataType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public Argument clone() {
    Argument t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Argument)) return false;
    Argument other =(Argument)obj;
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
    if (hasDataType() != other.hasDataType()) return false;
    if (hasDataType() && !(getDataType().equals(other.getDataType()))) return false;
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
    if (hasDataType()) {
      hash = hash * 31 + getDataType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public Argument copyFrom(RecordInfo template) {
    if (template instanceof Argument) {
      Argument t = (Argument)template;
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
      if (t.hasDataType()) {
        t.setDataType((FieldType) getDataType().clone());
      } else {
        nullifyDataType();
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
    str.append("{ \"$type\":  \"Argument\"");
    if (hasName()) {
      str.append(", \"name\": \"").append(getName()).append("\"");
    }
    if (hasDataType()) {
      str.append(", \"dataType\": ");
      getDataType().toString(str);
    }
    str.append("}");
    return str;
  }
}
