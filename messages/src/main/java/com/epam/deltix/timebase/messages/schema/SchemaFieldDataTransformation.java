package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Class which defines a transformation that is applied to a data field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaFieldDataTransformation",
    title = "SchemaFieldDataTransformation"
)
public class SchemaFieldDataTransformation implements RecordInterface {
  public static final String CLASS_NAME = SchemaFieldDataTransformation.class.getName();

  /**
   * Defines the transformation that was applied to the field.
   */
  protected SchemaFieldDataTransformationType transformationType = null;

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   */
  protected CharSequence defaultValue = null;

  /**
   * Defines the transformation that was applied to the field.
   * @return Transformation Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public SchemaFieldDataTransformationType getTransformationType() {
    return transformationType;
  }

  /**
   * Defines the transformation that was applied to the field.
   * @param value - Transformation Type
   */
  public void setTransformationType(SchemaFieldDataTransformationType value) {
    this.transformationType = value;
  }

  /**
   * Defines the transformation that was applied to the field.
   * @return true if Transformation Type is not null
   */
  public boolean hasTransformationType() {
    return transformationType != null;
  }

  /**
   * Defines the transformation that was applied to the field.
   */
  public void nullifyTransformationType() {
    this.transformationType = null;
  }

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @return Default Value
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getDefaultValue() {
    return defaultValue;
  }

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @param value - Default Value
   */
  public void setDefaultValue(CharSequence value) {
    this.defaultValue = value;
  }

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   * @return true if Default Value is not null
   */
  public boolean hasDefaultValue() {
    return defaultValue != null;
  }

  /**
   * Defines the default value if TransformationType equals SET_DEFAULT.
   */
  public void nullifyDefaultValue() {
    this.defaultValue = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected SchemaFieldDataTransformation createInstance() {
    return new SchemaFieldDataTransformation();
  }

  /**
   * Method nullifies all instance properties
   */
  public SchemaFieldDataTransformation nullify() {
    nullifyTransformationType();
    nullifyDefaultValue();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public SchemaFieldDataTransformation reset() {
    transformationType = null;
    defaultValue = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public SchemaFieldDataTransformation clone() {
    SchemaFieldDataTransformation t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof SchemaFieldDataTransformation)) return false;
    SchemaFieldDataTransformation other =(SchemaFieldDataTransformation)obj;
    if (hasTransformationType() != other.hasTransformationType()) return false;
    if (hasTransformationType() && getTransformationType() != other.getTransformationType()) return false;
    if (hasDefaultValue() != other.hasDefaultValue()) return false;
    if (hasDefaultValue()) {
      if (getDefaultValue().length() != other.getDefaultValue().length()) return false; else {
        CharSequence s1 = getDefaultValue();
        CharSequence s2 = other.getDefaultValue();
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
    int hash = 0;
    if (hasTransformationType()) {
      hash = hash * 31 + getTransformationType().getNumber();
    }
    if (hasDefaultValue()) {
      hash = hash * 31 + getDefaultValue().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public SchemaFieldDataTransformation copyFrom(RecordInfo template) {
    if (template instanceof SchemaFieldDataTransformation) {
      SchemaFieldDataTransformation t = (SchemaFieldDataTransformation)template;
      if (t.hasTransformationType()) {
        setTransformationType(t.getTransformationType());
      } else {
        nullifyTransformationType();
      }
      if (t.hasDefaultValue()) {
        if (hasDefaultValue() && getDefaultValue() instanceof StringBuilder) {
          ((StringBuilder)getDefaultValue()).setLength(0);
        } else {
          setDefaultValue(new StringBuilder());
        }
        ((StringBuilder)getDefaultValue()).append(t.getDefaultValue());
      } else {
        nullifyDefaultValue();
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
    str.append("{ \"$type\":  \"SchemaFieldDataTransformation\"");
    if (hasTransformationType()) {
      str.append(", \"transformationType\": \"").append(getTransformationType()).append("\"");
    }
    if (hasDefaultValue()) {
      str.append(", \"defaultValue\": \"").append(getDefaultValue()).append("\"");
    }
    str.append("}");
    return str;
  }
}
