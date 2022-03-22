package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Message tha defines a change in stream schema.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.SchemaChangeMessage",
    title = "SchemaChangeMessage"
)
public class SchemaChangeMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = SchemaChangeMessage.class.getName();

  /**
   * Previous schema state.
   */
  protected ObjectArrayList<UniqueDescriptor> previousState = null;

  /**
   * New schema state.
   */
  protected ObjectArrayList<UniqueDescriptor> newState = null;

  /**
   * New schema state.
   */
  protected ObjectArrayList<SchemaDescriptorChangeAction> descriptorChangeActions = null;

  /**
   * Message version
   */
  protected long version = TypeConstants.INT64_NULL;

  /**
   * Previous schema state.
   * @return Previous State
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = true,
      isElementNullable = false,
      elementTypes =  {
            TypeDescriptor.class, EnumDescriptor.class}

  )
  public ObjectArrayList<UniqueDescriptor> getPreviousState() {
    return previousState;
  }

  /**
   * Previous schema state.
   * @param value - Previous State
   */
  public void setPreviousState(ObjectArrayList<UniqueDescriptor> value) {
    this.previousState = value;
  }

  /**
   * Previous schema state.
   * @return true if Previous State is not null
   */
  public boolean hasPreviousState() {
    return previousState != null;
  }

  /**
   * Previous schema state.
   */
  public void nullifyPreviousState() {
    this.previousState = null;
  }

  /**
   * New schema state.
   * @return New State
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            TypeDescriptor.class, EnumDescriptor.class}

  )
  public ObjectArrayList<UniqueDescriptor> getNewState() {
    return newState;
  }

  /**
   * New schema state.
   * @param value - New State
   */
  public void setNewState(ObjectArrayList<UniqueDescriptor> value) {
    this.newState = value;
  }

  /**
   * New schema state.
   * @return true if New State is not null
   */
  public boolean hasNewState() {
    return newState != null;
  }

  /**
   * New schema state.
   */
  public void nullifyNewState() {
    this.newState = null;
  }

  /**
   * New schema state.
   * @return Descriptor Change Actions
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            SchemaDescriptorChangeAction.class}

  )
  public ObjectArrayList<SchemaDescriptorChangeAction> getDescriptorChangeActions() {
    return descriptorChangeActions;
  }

  /**
   * New schema state.
   * @param value - Descriptor Change Actions
   */
  public void setDescriptorChangeActions(ObjectArrayList<SchemaDescriptorChangeAction> value) {
    this.descriptorChangeActions = value;
  }

  /**
   * New schema state.
   * @return true if Descriptor Change Actions is not null
   */
  public boolean hasDescriptorChangeActions() {
    return descriptorChangeActions != null;
  }

  /**
   * New schema state.
   */
  public void nullifyDescriptorChangeActions() {
    this.descriptorChangeActions = null;
  }

  /**
   * Message version
   * @return Version
   */
  @SchemaElement
  public long getVersion() {
    return version;
  }

  /**
   * Message version
   * @param value - Version
   */
  public void setVersion(long value) {
    this.version = value;
  }

  /**
   * Message version
   * @return true if Version is not null
   */
  public boolean hasVersion() {
    return version != TypeConstants.INT64_NULL;
  }

  /**
   * Message version
   */
  public void nullifyVersion() {
    this.version = TypeConstants.INT64_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected SchemaChangeMessage createInstance() {
    return new SchemaChangeMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public SchemaChangeMessage nullify() {
    super.nullify();
    nullifyPreviousState();
    nullifyNewState();
    nullifyDescriptorChangeActions();
    nullifyVersion();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public SchemaChangeMessage reset() {
    super.reset();
    previousState = null;
    newState = null;
    descriptorChangeActions = null;
    version = TypeConstants.INT64_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public SchemaChangeMessage clone() {
    SchemaChangeMessage t = createInstance();
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
    if (!(obj instanceof SchemaChangeMessage)) return false;
    SchemaChangeMessage other =(SchemaChangeMessage)obj;
    if (hasPreviousState() != other.hasPreviousState()) return false;
    if (hasPreviousState()) {
      if (getPreviousState().size() != other.getPreviousState().size()) return false;
      else for (int j = 0; j < getPreviousState().size(); ++j) {
        if ((getPreviousState().get(j) != null) != (other.getPreviousState().get(j) != null)) return false;
        if (getPreviousState().get(j) != null && !getPreviousState().get(j).equals(other.getPreviousState().get(j))) return false;
      }
    }
    if (hasNewState() != other.hasNewState()) return false;
    if (hasNewState()) {
      if (getNewState().size() != other.getNewState().size()) return false;
      else for (int j = 0; j < getNewState().size(); ++j) {
        if ((getNewState().get(j) != null) != (other.getNewState().get(j) != null)) return false;
        if (getNewState().get(j) != null && !getNewState().get(j).equals(other.getNewState().get(j))) return false;
      }
    }
    if (hasDescriptorChangeActions() != other.hasDescriptorChangeActions()) return false;
    if (hasDescriptorChangeActions()) {
      if (getDescriptorChangeActions().size() != other.getDescriptorChangeActions().size()) return false;
      else for (int j = 0; j < getDescriptorChangeActions().size(); ++j) {
        if ((getDescriptorChangeActions().get(j) != null) != (other.getDescriptorChangeActions().get(j) != null)) return false;
        if (getDescriptorChangeActions().get(j) != null && !getDescriptorChangeActions().get(j).equals(other.getDescriptorChangeActions().get(j))) return false;
      }
    }
    if (hasVersion() != other.hasVersion()) return false;
    if (hasVersion() && getVersion() != other.getVersion()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasPreviousState()) {
      for (int j = 0; j < getPreviousState().size(); ++j) {
        hash ^= getPreviousState().get(j).hashCode();
      }
    }
    if (hasNewState()) {
      for (int j = 0; j < getNewState().size(); ++j) {
        hash ^= getNewState().get(j).hashCode();
      }
    }
    if (hasDescriptorChangeActions()) {
      for (int j = 0; j < getDescriptorChangeActions().size(); ++j) {
        hash ^= getDescriptorChangeActions().get(j).hashCode();
      }
    }
    if (hasVersion()) {
      hash = hash * 31 + ((int)(getVersion() ^ (getVersion() >>> 32)));
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public SchemaChangeMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof SchemaChangeMessage) {
      SchemaChangeMessage t = (SchemaChangeMessage)template;
      if (t.hasPreviousState()) {
        if (!hasPreviousState()) {
          setPreviousState(new ObjectArrayList<UniqueDescriptor>(t.getPreviousState().size()));
        } else {
          getPreviousState().clear();
        }
        for (int i = 0; i < t.getPreviousState().size(); ++i) ((ObjectArrayList<UniqueDescriptor>)getPreviousState()).add((UniqueDescriptor)t.getPreviousState().get(i).clone());
      } else {
        nullifyPreviousState();
      }
      if (t.hasNewState()) {
        if (!hasNewState()) {
          setNewState(new ObjectArrayList<UniqueDescriptor>(t.getNewState().size()));
        } else {
          getNewState().clear();
        }
        for (int i = 0; i < t.getNewState().size(); ++i) ((ObjectArrayList<UniqueDescriptor>)getNewState()).add((UniqueDescriptor)t.getNewState().get(i).clone());
      } else {
        nullifyNewState();
      }
      if (t.hasDescriptorChangeActions()) {
        if (!hasDescriptorChangeActions()) {
          setDescriptorChangeActions(new ObjectArrayList<SchemaDescriptorChangeAction>(t.getDescriptorChangeActions().size()));
        } else {
          getDescriptorChangeActions().clear();
        }
        for (int i = 0; i < t.getDescriptorChangeActions().size(); ++i) ((ObjectArrayList<SchemaDescriptorChangeAction>)getDescriptorChangeActions()).add((SchemaDescriptorChangeAction)t.getDescriptorChangeActions().get(i).clone());
      } else {
        nullifyDescriptorChangeActions();
      }
      if (t.hasVersion()) {
        setVersion(t.getVersion());
      } else {
        nullifyVersion();
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
    str.append("{ \"$type\":  \"SchemaChangeMessage\"");
    if (hasPreviousState()) {
      str.append(", \"previousState\": [");
      if (getPreviousState().size() > 0) {
        if (getPreviousState().get(0) == null) {
          str.append("null");
        } else {
          getPreviousState().get(0).toString(str);
        }
      }
      for (int i = 1; i < getPreviousState().size(); ++i) {
        str.append(", ");
        if (getPreviousState().get(i) == null) {
          str.append("null");
        } else {
          getPreviousState().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasNewState()) {
      str.append(", \"newState\": [");
      if (getNewState().size() > 0) {
        if (getNewState().get(0) == null) {
          str.append("null");
        } else {
          getNewState().get(0).toString(str);
        }
      }
      for (int i = 1; i < getNewState().size(); ++i) {
        str.append(", ");
        if (getNewState().get(i) == null) {
          str.append("null");
        } else {
          getNewState().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasDescriptorChangeActions()) {
      str.append(", \"descriptorChangeActions\": [");
      if (getDescriptorChangeActions().size() > 0) {
        if (getDescriptorChangeActions().get(0) == null) {
          str.append("null");
        } else {
          getDescriptorChangeActions().get(0).toString(str);
        }
      }
      for (int i = 1; i < getDescriptorChangeActions().size(); ++i) {
        str.append(", ");
        if (getDescriptorChangeActions().get(i) == null) {
          str.append("null");
        } else {
          getDescriptorChangeActions().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasVersion()) {
      str.append(", \"version\": ").append(getVersion());
    }
    if (hasTimeStampMs()) {
      str.append(", \"timeStampMs\": \"").append(getTimeStampMs()).append("\"");
    }
    if (hasSymbol()) {
      str.append(", \"symbol\": \"").append(getSymbol()).append("\"");
    }
    str.append("}");
    return str;
  }
}
