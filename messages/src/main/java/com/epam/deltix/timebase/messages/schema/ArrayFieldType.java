package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;

/**
 * Schema definition of array data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.ArrayFieldType",
    title = "ArrayFieldType"
)
public class ArrayFieldType extends FieldType implements RecordInterface {
  public static final String CLASS_NAME = ArrayFieldType.class.getName();

  /**
   * Array element data type.
   */
  protected FieldType elementType = null;

  public ArrayFieldType() {
    super();
    baseName = "ARRAY";
  }

  /**
   * Array element data type.
   * @return Element Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            BinaryFieldType.class, BooleanFieldType.class, CharFieldType.class, ClassFieldType.class, DateTimeFieldType.class, EnumFieldType.class, FloatFieldType.class, IntegerFieldType.class, TimeOfDayFieldType.class, VarcharFieldType.class}

  )
  public FieldType getElementType() {
    return elementType;
  }

  /**
   * Array element data type.
   * @param value - Element Type
   */
  public void setElementType(FieldType value) {
    this.elementType = value;
  }

  /**
   * Array element data type.
   * @return true if Element Type is not null
   */
  public boolean hasElementType() {
    return elementType != null;
  }

  /**
   * Array element data type.
   */
  public void nullifyElementType() {
    this.elementType = null;
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
  protected ArrayFieldType createInstance() {
    return new ArrayFieldType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public ArrayFieldType nullify() {
    super.nullify();
    nullifyElementType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public ArrayFieldType reset() {
    super.reset();
    elementType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public ArrayFieldType clone() {
    ArrayFieldType t = createInstance();
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
    if (!(obj instanceof ArrayFieldType)) return false;
    ArrayFieldType other =(ArrayFieldType)obj;
    if (hasElementType() != other.hasElementType()) return false;
    if (hasElementType() && !(getElementType().equals(other.getElementType()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasElementType()) {
      hash = hash * 31 + getElementType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public ArrayFieldType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof ArrayFieldType) {
      ArrayFieldType t = (ArrayFieldType)template;
      if (t.hasElementType()) {
        t.setElementType((FieldType) getElementType().clone());
      } else {
        nullifyElementType();
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
    str.append("{ \"$type\":  \"ArrayFieldType\"");
    if (hasElementType()) {
      str.append(", \"elementType\": ");
      getElementType().toString(str);
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
