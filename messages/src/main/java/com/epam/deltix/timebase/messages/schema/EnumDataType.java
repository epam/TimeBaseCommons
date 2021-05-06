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
 * Schema definition of enum data type.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.EnumDataType",
    title = "EnumDataType"
)
public class EnumDataType extends DataType implements EnumDataTypeInterface {
  public static final String CLASS_NAME = EnumDataType.class.getName();

  /**
   * Definition for enumeration class.
   */
  protected ClassDescriptorRefInfo typeDescriptor = null;

  /**
   * Definition for enumeration class.
   * @return Type Descriptor
   */
  @SchemaElement
  @SchemaType(
      isNullable = false,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            ClassDescriptorRef.class}

  )
  public ClassDescriptorRefInfo getTypeDescriptor() {
    return typeDescriptor;
  }

  /**
   * Definition for enumeration class.
   * @param value - Type Descriptor
   */
  public void setTypeDescriptor(ClassDescriptorRefInfo value) {
    this.typeDescriptor = value;
  }

  /**
   * Definition for enumeration class.
   * @return true if Type Descriptor is not null
   */
  public boolean hasTypeDescriptor() {
    return typeDescriptor != null;
  }

  /**
   * Definition for enumeration class.
   */
  public void nullifyTypeDescriptor() {
    this.typeDescriptor = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected EnumDataType createInstance() {
    return new EnumDataType();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public EnumDataType nullify() {
    super.nullify();
    nullifyTypeDescriptor();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public EnumDataType reset() {
    super.reset();
    typeDescriptor = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public EnumDataType clone() {
    EnumDataType t = createInstance();
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
    if (!(obj instanceof EnumDataTypeInfo)) return false;
    EnumDataTypeInfo other =(EnumDataTypeInfo)obj;
    if (hasTypeDescriptor() != other.hasTypeDescriptor()) return false;
    if (hasTypeDescriptor() && !(getTypeDescriptor().equals(other.getTypeDescriptor()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasTypeDescriptor()) {
      hash = hash * 31 + getTypeDescriptor().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public EnumDataType copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof EnumDataTypeInfo) {
      EnumDataTypeInfo t = (EnumDataTypeInfo)template;
      if (t.hasTypeDescriptor()) {
        if (hasTypeDescriptor() && getTypeDescriptor() instanceof RecordInterface) {
          ((RecordInterface)getTypeDescriptor()).copyFrom(t.getTypeDescriptor());
        } else {
          setTypeDescriptor((ClassDescriptorRefInfo)t.getTypeDescriptor().clone());
        }
      } else {
        nullifyTypeDescriptor();
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
    str.append("{ \"$type\":  \"EnumDataType\"");
    if (hasTypeDescriptor()) {
      str.append(", \"typeDescriptor\": ");
      getTypeDescriptor().toString(str);
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
