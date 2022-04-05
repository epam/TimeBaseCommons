/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Class which defines a change to schema descriptor.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaDescriptorChangeAction",
    title = "SchemaDescriptorChangeAction"
)
public class SchemaDescriptorChangeAction implements RecordInterface {
  public static final String CLASS_NAME = SchemaDescriptorChangeAction.class.getName();

  /**
   * Previous descriptor state.
   */
  protected UniqueDescriptor previousState = null;

  /**
   * New descriptor state.
   */
  protected UniqueDescriptor newState = null;

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   */
  protected SchemaDescriptorChangeType changeTypes = null;

  /**
   * Defines the data transformation that was applied to the descriptor.
   */
  protected SchemaDescriptorTransformation descriptorTransformation = null;

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   */
  protected ObjectArrayList<SchemaFieldChangeAction> fieldChangeActions = null;

  /**
   * Previous descriptor state.
   * @return Previous State
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            TypeDescriptor.class, EnumDescriptor.class}

  )
  public UniqueDescriptor getPreviousState() {
    return previousState;
  }

  /**
   * Previous descriptor state.
   * @param value - Previous State
   */
  public void setPreviousState(UniqueDescriptor value) {
    this.previousState = value;
  }

  /**
   * Previous descriptor state.
   * @return true if Previous State is not null
   */
  public boolean hasPreviousState() {
    return previousState != null;
  }

  /**
   * Previous descriptor state.
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
            TypeDescriptor.class, EnumDescriptor.class}

  )
  public UniqueDescriptor getNewState() {
    return newState;
  }

  /**
   * New descriptor state.
   * @param value - New State
   */
  public void setNewState(UniqueDescriptor value) {
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
   * Bitmask that defines the changes that were applied to the descriptor.
   * @return Change Types
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public SchemaDescriptorChangeType getChangeTypes() {
    return changeTypes;
  }

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @param value - Change Types
   */
  public void setChangeTypes(SchemaDescriptorChangeType value) {
    this.changeTypes = value;
  }

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   * @return true if Change Types is not null
   */
  public boolean hasChangeTypes() {
    return changeTypes != null;
  }

  /**
   * Bitmask that defines the changes that were applied to the descriptor.
   */
  public void nullifyChangeTypes() {
    this.changeTypes = null;
  }

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @return Descriptor Transformation
   */
  @SchemaElement
  @SchemaType(
      isNullable = true,
      dataType = SchemaDataType.OBJECT,
      nestedTypes =  {
            SchemaDescriptorTransformation.class}

  )
  public SchemaDescriptorTransformation getDescriptorTransformation() {
    return descriptorTransformation;
  }

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @param value - Descriptor Transformation
   */
  public void setDescriptorTransformation(SchemaDescriptorTransformation value) {
    this.descriptorTransformation = value;
  }

  /**
   * Defines the data transformation that was applied to the descriptor.
   * @return true if Descriptor Transformation is not null
   */
  public boolean hasDescriptorTransformation() {
    return descriptorTransformation != null;
  }

  /**
   * Defines the data transformation that was applied to the descriptor.
   */
  public void nullifyDescriptorTransformation() {
    this.descriptorTransformation = null;
  }

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @return Field Change Actions
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = true,
      isElementNullable = false,
      elementTypes =  {
            SchemaFieldChangeAction.class}

  )
  public ObjectArrayList<SchemaFieldChangeAction> getFieldChangeActions() {
    return fieldChangeActions;
  }

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @param value - Field Change Actions
   */
  public void setFieldChangeActions(ObjectArrayList<SchemaFieldChangeAction> value) {
    this.fieldChangeActions = value;
  }

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   * @return true if Field Change Actions is not null
   */
  public boolean hasFieldChangeActions() {
    return fieldChangeActions != null;
  }

  /**
   * A list of change actions to data fields.
   * Populated only if ChangeTypes contains ALTER flag
   */
  public void nullifyFieldChangeActions() {
    this.fieldChangeActions = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected SchemaDescriptorChangeAction createInstance() {
    return new SchemaDescriptorChangeAction();
  }

  /**
   * Method nullifies all instance properties
   */
  public SchemaDescriptorChangeAction nullify() {
    nullifyPreviousState();
    nullifyNewState();
    nullifyChangeTypes();
    nullifyDescriptorTransformation();
    nullifyFieldChangeActions();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public SchemaDescriptorChangeAction reset() {
    previousState = null;
    newState = null;
    changeTypes = null;
    descriptorTransformation = null;
    fieldChangeActions = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public SchemaDescriptorChangeAction clone() {
    SchemaDescriptorChangeAction t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof SchemaDescriptorChangeAction)) return false;
    SchemaDescriptorChangeAction other =(SchemaDescriptorChangeAction)obj;
    if (hasPreviousState() != other.hasPreviousState()) return false;
    if (hasPreviousState() && !(getPreviousState().equals(other.getPreviousState()))) return false;
    if (hasNewState() != other.hasNewState()) return false;
    if (hasNewState() && !(getNewState().equals(other.getNewState()))) return false;
    if (hasChangeTypes() != other.hasChangeTypes()) return false;
    if (hasChangeTypes() && getChangeTypes() != other.getChangeTypes()) return false;
    if (hasDescriptorTransformation() != other.hasDescriptorTransformation()) return false;
    if (hasDescriptorTransformation() && !(getDescriptorTransformation().equals(other.getDescriptorTransformation()))) return false;
    if (hasFieldChangeActions() != other.hasFieldChangeActions()) return false;
    if (hasFieldChangeActions()) {
      if (getFieldChangeActions().size() != other.getFieldChangeActions().size()) return false;
      else for (int j = 0; j < getFieldChangeActions().size(); ++j) {
        if ((getFieldChangeActions().get(j) != null) != (other.getFieldChangeActions().get(j) != null)) return false;
        if (getFieldChangeActions().get(j) != null && !getFieldChangeActions().get(j).equals(other.getFieldChangeActions().get(j))) return false;
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
    if (hasPreviousState()) {
      hash = hash * 31 + getPreviousState().hashCode();
    }
    if (hasNewState()) {
      hash = hash * 31 + getNewState().hashCode();
    }
    if (hasChangeTypes()) {
      hash = hash * 31 + getChangeTypes().getNumber();
    }
    if (hasDescriptorTransformation()) {
      hash = hash * 31 + getDescriptorTransformation().hashCode();
    }
    if (hasFieldChangeActions()) {
      for (int j = 0; j < getFieldChangeActions().size(); ++j) {
        hash ^= getFieldChangeActions().get(j).hashCode();
      }
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public SchemaDescriptorChangeAction copyFrom(RecordInfo template) {
    if (template instanceof SchemaDescriptorChangeAction) {
      SchemaDescriptorChangeAction t = (SchemaDescriptorChangeAction)template;
      if (t.hasPreviousState()) {
        t.setPreviousState((UniqueDescriptor) getPreviousState().clone());
      } else {
        nullifyPreviousState();
      }
      if (t.hasNewState()) {
        t.setNewState((UniqueDescriptor) getNewState().clone());
      } else {
        nullifyNewState();
      }
      if (t.hasChangeTypes()) {
        setChangeTypes(t.getChangeTypes());
      } else {
        nullifyChangeTypes();
      }
      if (t.hasDescriptorTransformation()) {
        t.setDescriptorTransformation((SchemaDescriptorTransformation) getDescriptorTransformation().clone());
      } else {
        nullifyDescriptorTransformation();
      }
      if (t.hasFieldChangeActions()) {
        if (!hasFieldChangeActions()) {
          setFieldChangeActions(new ObjectArrayList<SchemaFieldChangeAction>(t.getFieldChangeActions().size()));
        } else {
          getFieldChangeActions().clear();
        }
        for (int i = 0; i < t.getFieldChangeActions().size(); ++i) ((ObjectArrayList<SchemaFieldChangeAction>)getFieldChangeActions()).add((SchemaFieldChangeAction)t.getFieldChangeActions().get(i).clone());
      } else {
        nullifyFieldChangeActions();
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
    str.append("{ \"$type\":  \"SchemaDescriptorChangeAction\"");
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
    if (hasDescriptorTransformation()) {
      str.append(", \"descriptorTransformation\": ");
      getDescriptorTransformation().toString(str);
    }
    if (hasFieldChangeActions()) {
      str.append(", \"fieldChangeActions\": [");
      if (getFieldChangeActions().size() > 0) {
        if (getFieldChangeActions().get(0) == null) {
          str.append("null");
        } else {
          getFieldChangeActions().get(0).toString(str);
        }
      }
      for (int i = 1; i < getFieldChangeActions().size(); ++i) {
        str.append(", ");
        if (getFieldChangeActions().get(i) == null) {
          str.append("null");
        } else {
          getFieldChangeActions().get(i).toString(str);
        }
      }
      str.append("]");
    }
    str.append("}");
    return str;
  }
}
