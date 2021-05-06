package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaDataType;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Schema definition of array data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.ArrayDataType",
    title = "ArrayDataType"
)
public class ArrayDataType extends DataType implements ArrayDataTypeInterface {
  public static final String CLASS_NAME = ArrayDataType.class.getName();

  /**
   * Array element data type.
   */
  protected DataTypeInfo elementType = null;

  /**
   * Array element data type.
   * @return Element Type
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            BinaryDataType.class, BooleanDataType.class, CharDataType.class, ClassDataType.class, DateTimeDataType.class, EnumDataType.class, FloatDataType.class, IntegerDataType.class, TimeOfDayDataType.class, VarcharDataType.class}

  )
  public DataTypeInfo getElementType() {
    return elementType;
  }

  /**
   * Array element data type.
   * @param value - Element Type
   */
  public void setElementType(DataTypeInfo value) {
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
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected ArrayDataType createInstance() {
    return new ArrayDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public ArrayDataType nullify() {
    super.nullify();
    nullifyElementType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public ArrayDataType reset() {
    super.reset();
    elementType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public ArrayDataType clone() {
    ArrayDataType t = createInstance();
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
    if (!(obj instanceof ArrayDataTypeInfo)) return false;
    ArrayDataTypeInfo other =(ArrayDataTypeInfo)obj;
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
  public ArrayDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof ArrayDataTypeInfo) {
      ArrayDataTypeInfo t = (ArrayDataTypeInfo)template;
      if (t.hasElementType()) {
        if (hasElementType() && getElementType() instanceof RecordInterface) {
          ((RecordInterface)getElementType()).copyFrom(t.getElementType());
        } else {
          setElementType((DataTypeInfo)t.getElementType().clone());
        }
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
    str.append("{ \"$type\":  \"ArrayDataType\"");
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
    str.append("}");
    return str;
  }
}
