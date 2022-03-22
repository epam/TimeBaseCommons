package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 * Schema definition of time-of-day data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.TimeOfDayFieldType",
    title = "TimeOfDayFieldType"
)
public class TimeOfDayFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = TimeOfDayFieldType.class.getName();

  public TimeOfDayFieldType() {
    super();
    baseName = "TIME_OF_DAY";
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
  protected TimeOfDayFieldType createInstance() {
    return new TimeOfDayFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public TimeOfDayFieldType nullify() {
    super.nullify();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public TimeOfDayFieldType reset() {
    super.reset();
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public TimeOfDayFieldType clone() {
    TimeOfDayFieldType t = createInstance();
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
    if (!(obj instanceof TimeOfDayFieldType)) return false;
    TimeOfDayFieldType other =(TimeOfDayFieldType)obj;
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
  public TimeOfDayFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof TimeOfDayFieldType) {
      TimeOfDayFieldType t = (TimeOfDayFieldType)template;
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
    str.append("{ \"$type\":  \"TimeOfDayFieldType\"");
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
