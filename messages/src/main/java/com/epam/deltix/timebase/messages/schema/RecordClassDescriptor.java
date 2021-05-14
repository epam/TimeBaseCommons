package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaArrayType;
import com.epam.deltix.timebase.messages.SchemaDataType;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import com.epam.deltix.timebase.messages.TypeConstants;
import com.epam.deltix.util.collections.generated.ObjectArrayList;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Schema definition for a class.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.RecordClassDescriptor",
    title = "RecordClassDescriptor"
)
public class RecordClassDescriptor extends ClassDescriptor implements RecordClassDescriptorInterface {
  public static final String CLASS_NAME = RecordClassDescriptor.class.getName();

  /**
   * Defines parent RecordClassDescriptor if exist.
   */
  protected RecordClassDescriptorInfo parent = null;

  /**
   * Defines if current RecordClassDescriptor is abstract.
   */
  protected byte isAbstract = TypeConstants.BOOLEAN_NULL;

  /**
   * Defines if current RecordClassDescriptor is content class.
   */
  protected byte isContentClass = TypeConstants.BOOLEAN_NULL;

  /**
   * List of fields of a class.
   */
  protected ObjectArrayList<DataFieldInfo> dataFields = null;

  /**
   * Defines parent RecordClassDescriptor if exist.
   * @return Parent
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            RecordClassDescriptor.class}

  )
  public RecordClassDescriptorInfo getParent() {
    return parent;
  }

  /**
   * Defines parent RecordClassDescriptor if exist.
   * @param value - Parent
   */
  public void setParent(RecordClassDescriptorInfo value) {
    this.parent = value;
  }

  /**
   * Defines parent RecordClassDescriptor if exist.
   * @return true if Parent is not null
   */
  public boolean hasParent() {
    return parent != null;
  }

  /**
   * Defines parent RecordClassDescriptor if exist.
   */
  public void nullifyParent() {
    this.parent = null;
  }

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @return Is Abstract
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public boolean isAbstract() {
    return isAbstract == 1;
  }

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @param value - Is Abstract
   */
  public void setIsAbstract(boolean value) {
    this.isAbstract = (byte)(value ? 1 : 0);
  }

  /**
   * Defines if current RecordClassDescriptor is abstract.
   * @return true if Is Abstract is not null
   */
  public boolean hasIsAbstract() {
    return isAbstract != com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current RecordClassDescriptor is abstract.
   */
  public void nullifyIsAbstract() {
    this.isAbstract = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @return Is Content Class
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public boolean isContentClass() {
    return isContentClass == 1;
  }

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @param value - Is Content Class
   */
  public void setIsContentClass(boolean value) {
    this.isContentClass = (byte)(value ? 1 : 0);
  }

  /**
   * Defines if current RecordClassDescriptor is content class.
   * @return true if Is Content Class is not null
   */
  public boolean hasIsContentClass() {
    return isContentClass != com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current RecordClassDescriptor is content class.
   */
  public void nullifyIsContentClass() {
    this.isContentClass = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
  }

  /**
   * List of fields of a class.
   * @return Data Fields
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            StaticDataField.class, NonStaticDataField.class}

  )
  public ObjectArrayList<DataFieldInfo> getDataFields() {
    return dataFields;
  }

  /**
   * List of fields of a class.
   * @param value - Data Fields
   */
  public void setDataFields(ObjectArrayList<DataFieldInfo> value) {
    this.dataFields = value;
  }

  /**
   * List of fields of a class.
   * @return true if Data Fields is not null
   */
  public boolean hasDataFields() {
    return dataFields != null;
  }

  /**
   * List of fields of a class.
   */
  public void nullifyDataFields() {
    this.dataFields = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected RecordClassDescriptor createInstance() {
    return new RecordClassDescriptor();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public RecordClassDescriptor nullify() {
    super.nullify();
    nullifyParent();
    nullifyIsAbstract();
    nullifyIsContentClass();
    nullifyDataFields();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public RecordClassDescriptor reset() {
    super.reset();
    parent = null;
    isAbstract = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
    isContentClass = com.epam.deltix.timebase.messages.TypeConstants.BOOLEAN_NULL;
    dataFields = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public RecordClassDescriptor clone() {
    RecordClassDescriptor t = createInstance();
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
    if (!(obj instanceof RecordClassDescriptorInfo)) return false;
    RecordClassDescriptorInfo other =(RecordClassDescriptorInfo)obj;
    if (hasParent() != other.hasParent()) return false;
    if (hasParent() && !(getParent().equals(other.getParent()))) return false;
    if (hasIsAbstract() != other.hasIsAbstract()) return false;
    if (hasIsAbstract() && isAbstract() != other.isAbstract()) return false;
    if (hasIsContentClass() != other.hasIsContentClass()) return false;
    if (hasIsContentClass() && isContentClass() != other.isContentClass()) return false;
    if (hasDataFields() != other.hasDataFields()) return false;
    if (hasDataFields()) {
      if (getDataFields().size() != other.getDataFields().size()) return false;
      else for (int j = 0; j < getDataFields().size(); ++j) {
        if ((getDataFields().get(j) != null) != (other.getDataFields().get(j) != null)) return false;
        if (getDataFields().get(j) != null && !getDataFields().get(j).equals(other.getDataFields().get(j))) return false;
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
    if (hasParent()) {
      hash = hash * 31 + getParent().hashCode();
    }
    if (hasIsAbstract()) {
      hash = hash * 31 + (isAbstract() ? 1231 : 1237);
    }
    if (hasIsContentClass()) {
      hash = hash * 31 + (isContentClass() ? 1231 : 1237);
    }
    if (hasDataFields()) {
      for (int j = 0; j < getDataFields().size(); ++j) {
        hash ^= getDataFields().get(j).hashCode();
      }
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public RecordClassDescriptor copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof RecordClassDescriptorInfo) {
      RecordClassDescriptorInfo t = (RecordClassDescriptorInfo)template;
      if (t.hasParent()) {
        if (hasParent() && getParent() instanceof RecordInterface) {
          ((RecordInterface)getParent()).copyFrom(t.getParent());
        } else {
          setParent((RecordClassDescriptorInfo)t.getParent().clone());
        }
      } else {
        nullifyParent();
      }
      if (t.hasIsAbstract()) {
        setIsAbstract(t.isAbstract());
      } else {
        nullifyIsAbstract();
      }
      if (t.hasIsContentClass()) {
        setIsContentClass(t.isContentClass());
      } else {
        nullifyIsContentClass();
      }
      if (t.hasDataFields()) {
        if (!hasDataFields()) {
          setDataFields(new ObjectArrayList<DataFieldInfo>(t.getDataFields().size()));
        } else {
          getDataFields().clear();
        }
        for (int i = 0; i < t.getDataFields().size(); ++i) ((ObjectArrayList<DataFieldInfo>)getDataFields()).add((DataFieldInfo)t.getDataFields().get(i).clone());
      } else {
        nullifyDataFields();
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
    str.append("{ \"$type\":  \"RecordClassDescriptor\"");
    if (hasParent()) {
      str.append(", \"parent\": ");
      getParent().toString(str);
    }
    if (hasIsAbstract()) {
      str.append(", \"isAbstract\": ").append(isAbstract());
    }
    if (hasIsContentClass()) {
      str.append(", \"isContentClass\": ").append(isContentClass());
    }
    if (hasDataFields()) {
      str.append(", \"dataFields\": [");
      if (getDataFields().size() > 0) {
        if (getDataFields().get(0) == null) {
          str.append("null");
        } else {
          getDataFields().get(0).toString(str);
        }
      }
      for (int i = 1; i < getDataFields().size(); ++i) {
        str.append(", ");
        if (getDataFields().get(i) == null) {
          str.append("null");
        } else {
          getDataFields().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasGuid()) {
      str.append(", \"guid\": \"").append(getGuid()).append("\"");
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
