package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Class which defines a transformation that is applied to the descriptor.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorTransformation",
    title = "SchemaDescriptorTransformation"
)
public class SchemaDescriptorTransformation implements RecordInterface {
  public static final String CLASS_NAME = SchemaDescriptorTransformation.class.getName();

  /**
   * Defines the transformation type that was applied to the descriptor.
   */
  protected SchemaDescriptorTransformationType transformationType = null;

  /**
   * Defines the transformation type that was applied to the descriptor.
   * @return Transformation Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public SchemaDescriptorTransformationType getTransformationType() {
    return transformationType;
  }

  /**
   * Defines the transformation type that was applied to the descriptor.
   * @param value - Transformation Type
   */
  public void setTransformationType(SchemaDescriptorTransformationType value) {
    this.transformationType = value;
  }

  /**
   * Defines the transformation type that was applied to the descriptor.
   * @return true if Transformation Type is not null
   */
  public boolean hasTransformationType() {
    return transformationType != null;
  }

  /**
   * Defines the transformation type that was applied to the descriptor.
   */
  public void nullifyTransformationType() {
    this.transformationType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected SchemaDescriptorTransformation createInstance() {
    return new SchemaDescriptorTransformation();
  }

  /**
   * Method nullifies all instance properties
   */
  public SchemaDescriptorTransformation nullify() {
    nullifyTransformationType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public SchemaDescriptorTransformation reset() {
    transformationType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public SchemaDescriptorTransformation clone() {
    SchemaDescriptorTransformation t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof SchemaDescriptorTransformation)) return false;
    SchemaDescriptorTransformation other =(SchemaDescriptorTransformation)obj;
    if (hasTransformationType() != other.hasTransformationType()) return false;
    if (hasTransformationType() && getTransformationType() != other.getTransformationType()) return false;
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
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public SchemaDescriptorTransformation copyFrom(RecordInfo template) {
    if (template instanceof SchemaDescriptorTransformation) {
      SchemaDescriptorTransformation t = (SchemaDescriptorTransformation)template;
      if (t.hasTransformationType()) {
        setTransformationType(t.getTransformationType());
      } else {
        nullifyTransformationType();
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
    str.append("{ \"$type\":  \"SchemaDescriptorTransformation\"");
    if (hasTransformationType()) {
      str.append(", \"transformationType\": \"").append(getTransformationType()).append("\"");
    }
    str.append("}");
    return str;
  }
}
