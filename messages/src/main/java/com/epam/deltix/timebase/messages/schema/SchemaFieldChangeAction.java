package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;

/**
 * Class which defines a change to schema field.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaFieldChangeAction",
    title = "SchemaFieldChangeAction"
)
public class SchemaFieldChangeAction implements RecordInterface {
  public static final String CLASS_NAME = SchemaFieldChangeAction.class.getName();

  /**
   * Previous data field state.
   */
  protected Field previousState = null;

  /**
   * New descriptor state.
   */
  protected Field newState = null;

  /**
   * Bitmask that defines the changes that were applied to the field.
   */
  protected SchemaFieldChangeType changeTypes = null;

  /**
   * Defines the data transformation that was applied to the field.
   */
  protected SchemaFieldDataTransformation dataTransformation = null;

  /**
   * Previous data field state.
   * @return Previous State
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            StaticField.class, NonStaticField.class}

  )
  public Field getPreviousState() {
    return previousState;
  }

  /**
   * Previous data field state.
   * @param value - Previous State
   */
  public void setPreviousState(Field value) {
    this.previousState = value;
  }

  /**
   * Previous data field state.
   * @return true if Previous State is not null
   */
  public boolean hasPreviousState() {
    return previousState != null;
  }

  /**
   * Previous data field state.
   */
  public void nullifyPreviousState() {
    this.previousState = null;
  }

  /**
   * New descriptor state.
   * @return New State
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            StaticField.class, NonStaticField.class}

  )
  public Field getNewState() {
    return newState;
  }

  /**
   * New descriptor state.
   * @param value - New State
   */
  public void setNewState(Field value) {
    this.newState = value;
  }

  /**
   * New descriptor state.
   * @return true if New State is not null
   */
  public boolean hasNewState() {
    return newState != null;
  }

  /**
   * New descriptor state.
   */
  public void nullifyNewState() {
    this.newState = null;
  }

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @return Change Types
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public SchemaFieldChangeType getChangeTypes() {
    return changeTypes;
  }

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @param value - Change Types
   */
  public void setChangeTypes(SchemaFieldChangeType value) {
    this.changeTypes = value;
  }

  /**
   * Bitmask that defines the changes that were applied to the field.
   * @return true if Change Types is not null
   */
  public boolean hasChangeTypes() {
    return changeTypes != null;
  }

  /**
   * Bitmask that defines the changes that were applied to the field.
   */
  public void nullifyChangeTypes() {
    this.changeTypes = null;
  }

  /**
   * Defines the data transformation that was applied to the field.
   * @return Data Transformation
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            SchemaFieldDataTransformation.class}

  )
  public SchemaFieldDataTransformation getDataTransformation() {
    return dataTransformation;
  }

  /**
   * Defines the data transformation that was applied to the field.
   * @param value - Data Transformation
   */
  public void setDataTransformation(SchemaFieldDataTransformation value) {
    this.dataTransformation = value;
  }

  /**
   * Defines the data transformation that was applied to the field.
   * @return true if Data Transformation is not null
   */
  public boolean hasDataTransformation() {
    return dataTransformation != null;
  }

  /**
   * Defines the data transformation that was applied to the field.
   */
  public void nullifyDataTransformation() {
    this.dataTransformation = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected SchemaFieldChangeAction createInstance() {
    return new SchemaFieldChangeAction();
  }

  /**
   * Method nullifies all instance properties
   */
  public SchemaFieldChangeAction nullify() {
    nullifyPreviousState();
    nullifyNewState();
    nullifyChangeTypes();
    nullifyDataTransformation();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public SchemaFieldChangeAction reset() {
    previousState = null;
    newState = null;
    changeTypes = null;
    dataTransformation = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public SchemaFieldChangeAction clone() {
    SchemaFieldChangeAction t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof SchemaFieldChangeAction)) return false;
    SchemaFieldChangeAction other =(SchemaFieldChangeAction)obj;
    if (hasPreviousState() != other.hasPreviousState()) return false;
    if (hasPreviousState() && !(getPreviousState().equals(other.getPreviousState()))) return false;
    if (hasNewState() != other.hasNewState()) return false;
    if (hasNewState() && !(getNewState().equals(other.getNewState()))) return false;
    if (hasChangeTypes() != other.hasChangeTypes()) return false;
    if (hasChangeTypes() && getChangeTypes() != other.getChangeTypes()) return false;
    if (hasDataTransformation() != other.hasDataTransformation()) return false;
    if (hasDataTransformation() && !(getDataTransformation().equals(other.getDataTransformation()))) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasPreviousState()) {
      hash = hash * 31 + getPreviousState().hashCode();
    }
    if (hasNewState()) {
      hash = hash * 31 + getNewState().hashCode();
    }
    if (hasChangeTypes()) {
      hash = hash * 31 + getChangeTypes().getNumber();
    }
    if (hasDataTransformation()) {
      hash = hash * 31 + getDataTransformation().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public SchemaFieldChangeAction copyFrom(RecordInfo template) {
    if (template instanceof SchemaFieldChangeAction) {
      SchemaFieldChangeAction t = (SchemaFieldChangeAction)template;
      if (t.hasPreviousState()) {
        t.setPreviousState((Field) getPreviousState().clone());
      } else {
        nullifyPreviousState();
      }
      if (t.hasNewState()) {
        t.setNewState((Field) getNewState().clone());
      } else {
        nullifyNewState();
      }
      if (t.hasChangeTypes()) {
        setChangeTypes(t.getChangeTypes());
      } else {
        nullifyChangeTypes();
      }
      if (t.hasDataTransformation()) {
        t.setDataTransformation((SchemaFieldDataTransformation) getDataTransformation().clone());
      } else {
        nullifyDataTransformation();
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
    str.append("{ \"$type\":  \"SchemaFieldChangeAction\"");
    if (hasPreviousState()) {
      str.append(", \"previousState\": ");
      getPreviousState().toString(str);
    }
    if (hasNewState()) {
      str.append(", \"newState\": ");
      getNewState().toString(str);
    }
    if (hasChangeTypes()) {
      str.append(", \"changeTypes\": \"").append(getChangeTypes()).append("\"");
    }
    if (hasDataTransformation()) {
      str.append(", \"dataTransformation\": ");
      getDataTransformation().toString(str);
    }
    str.append("}");
    return str;
  }
}
