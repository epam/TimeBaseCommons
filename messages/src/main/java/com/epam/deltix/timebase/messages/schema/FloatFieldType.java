package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;

/**
 * Schema definition of float data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.FloatFieldType",
    title = "FloatFieldType"
)
public class FloatFieldType extends IntegralFieldType implements RecordInterface {
  public static final String CLASS_NAME = FloatFieldType.class.getName();

  /**
   * Scale.
   */
  protected short scale = TypeConstants.INT16_NULL;

  public FloatFieldType() {
    super();
    baseName = "FLOAT";
  }

  /**
   * Scale.
   * @return Scale
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public short getScale() {
    return scale;
  }

  /**
   * Scale.
   * @param value - Scale
   */
  public void setScale(short value) {
    this.scale = value;
  }

  /**
   * Scale.
   * @return true if Scale is not null
   */
  public boolean hasScale() {
    return scale != TypeConstants.INT16_NULL;
  }

  /**
   * Scale.
   */
  public void nullifyScale() {
    this.scale = TypeConstants.INT16_NULL;
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
  protected FloatFieldType createInstance() {
    return new FloatFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public FloatFieldType nullify() {
    super.nullify();
    nullifyScale();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public FloatFieldType reset() {
    super.reset();
    scale = TypeConstants.INT16_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public FloatFieldType clone() {
    FloatFieldType t = createInstance();
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
    if (!(obj instanceof FloatFieldType)) return false;
    FloatFieldType other =(FloatFieldType)obj;
    if (hasScale() != other.hasScale()) return false;
    if (hasScale() && getScale() != other.getScale()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasScale()) {
      hash = hash * 31 + ((int)getScale());
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public FloatFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof FloatFieldType) {
      FloatFieldType t = (FloatFieldType)template;
      if (t.hasScale()) {
        setScale(t.getScale());
      } else {
        nullifyScale();
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
    str.append("{ \"$type\":  \"FloatFieldType\"");
    if (hasScale()) {
      str.append(", \"scale\": ").append(getScale());
    }
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
    if (hasBaseName()) {
      str.append(", \"baseName\": \"").append(getBaseName()).append("\"");
    }
    str.append("}");
    return str;
  }
}
