package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Schema definition for a class.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.TypeDescriptor",
    title = "TypeDescriptor"
)
public class TypeDescriptor extends UniqueDescriptor implements RecordInterface {
  public static final String CLASS_NAME = TypeDescriptor.class.getName();

  /**
   * Defines parent TypeDescriptor if exist.
   */
  protected TypeDescriptor parent = null;

  /**
   * Defines if current TypeDescriptor is abstract.
   */
  protected byte isAbstract = TypeConstants.BOOLEAN_NULL;

  /**
   * Defines if current TypeDescriptor is content class.
   */
  protected byte isContentClass = TypeConstants.BOOLEAN_NULL;

  /**
   * List of fields of a class.
   */
  protected ObjectArrayList<Field> fields = null;

  /**
   * Defines parent TypeDescriptor if exist.
   * @return Parent
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            TypeDescriptor.class}

  )
  public TypeDescriptor getParent() {
    return parent;
  }

  /**
   * Defines parent TypeDescriptor if exist.
   * @param value - Parent
   */
  public void setParent(TypeDescriptor value) {
    this.parent = value;
  }

  /**
   * Defines parent TypeDescriptor if exist.
   * @return true if Parent is not null
   */
  public boolean hasParent() {
    return parent != null;
  }

  /**
   * Defines parent TypeDescriptor if exist.
   */
  public void nullifyParent() {
    this.parent = null;
  }

  /**
   * Defines if current TypeDescriptor is abstract.
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
   * Defines if current TypeDescriptor is abstract.
   * @param value - Is Abstract
   */
  public void setIsAbstract(boolean value) {
    this.isAbstract = (byte)(value ? 1 : 0);
  }

  /**
   * Defines if current TypeDescriptor is abstract.
   * @return true if Is Abstract is not null
   */
  public boolean hasIsAbstract() {
    return isAbstract != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current TypeDescriptor is abstract.
   */
  public void nullifyIsAbstract() {
    this.isAbstract = TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current TypeDescriptor is content class.
   * @return Is Content Class
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public boolean isContentClass() {
    return isContentClass == 1;
  }

  /**
   * Defines if current TypeDescriptor is content class.
   * @param value - Is Content Class
   */
  public void setIsContentClass(boolean value) {
    this.isContentClass = (byte)(value ? 1 : 0);
  }

  /**
   * Defines if current TypeDescriptor is content class.
   * @return true if Is Content Class is not null
   */
  public boolean hasIsContentClass() {
    return isContentClass != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Defines if current TypeDescriptor is content class.
   */
  public void nullifyIsContentClass() {
    this.isContentClass = TypeConstants.BOOLEAN_NULL;
  }

  /**
   * List of fields of a class.
   * @return Fields
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            StaticField.class, NonStaticField.class}

  )
  public ObjectArrayList<Field> getFields() {
    return fields;
  }

  /**
   * List of fields of a class.
   * @param value - Fields
   */
  public void setFields(ObjectArrayList<Field> value) {
    this.fields = value;
  }

  /**
   * List of fields of a class.
   * @return true if Fields is not null
   */
  public boolean hasFields() {
    return fields != null;
  }

  /**
   * List of fields of a class.
   */
  public void nullifyFields() {
    this.fields = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected TypeDescriptor createInstance() {
    return new TypeDescriptor();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public TypeDescriptor nullify() {
    super.nullify();
    nullifyParent();
    nullifyIsAbstract();
    nullifyIsContentClass();
    nullifyFields();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public TypeDescriptor reset() {
    super.reset();
    parent = null;
    isAbstract = TypeConstants.BOOLEAN_NULL;
    isContentClass = TypeConstants.BOOLEAN_NULL;
    fields = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public TypeDescriptor clone() {
    TypeDescriptor t = createInstance();
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
    if (!(obj instanceof TypeDescriptor)) return false;
    TypeDescriptor other =(TypeDescriptor)obj;
    if (hasParent() != other.hasParent()) return false;
    if (hasParent() && !(getParent().equals(other.getParent()))) return false;
    if (hasIsAbstract() != other.hasIsAbstract()) return false;
    if (hasIsAbstract() && isAbstract() != other.isAbstract()) return false;
    if (hasIsContentClass() != other.hasIsContentClass()) return false;
    if (hasIsContentClass() && isContentClass() != other.isContentClass()) return false;
    if (hasFields() != other.hasFields()) return false;
    if (hasFields()) {
      if (getFields().size() != other.getFields().size()) return false;
      else for (int j = 0; j < getFields().size(); ++j) {
        if ((getFields().get(j) != null) != (other.getFields().get(j) != null)) return false;
        if (getFields().get(j) != null && !getFields().get(j).equals(other.getFields().get(j))) return false;
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
    if (hasFields()) {
      for (int j = 0; j < getFields().size(); ++j) {
        hash ^= getFields().get(j).hashCode();
      }
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public TypeDescriptor copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof TypeDescriptor) {
      TypeDescriptor t = (TypeDescriptor)template;
      if (t.hasParent()) {
        t.setParent((TypeDescriptor) getParent().clone());
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
      if (t.hasFields()) {
        if (!hasFields()) {
          setFields(new ObjectArrayList<Field>(t.getFields().size()));
        } else {
          getFields().clear();
        }
        for (int i = 0; i < t.getFields().size(); ++i) ((ObjectArrayList<Field>)getFields()).add((Field)t.getFields().get(i).clone());
      } else {
        nullifyFields();
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
    str.append("{ \"$type\":  \"TypeDescriptor\"");
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
    if (hasFields()) {
      str.append(", \"fields\": [");
      if (getFields().size() > 0) {
        if (getFields().get(0) == null) {
          str.append("null");
        } else {
          getFields().get(0).toString(str);
        }
      }
      for (int i = 1; i < getFields().size(); ++i) {
        str.append(", ");
        if (getFields().get(i) == null) {
          str.append("null");
        } else {
          getFields().get(i).toString(str);
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
