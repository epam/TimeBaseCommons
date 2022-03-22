package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Message that describes TimeBase stream
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.StreamMessage",
    title = "StreamMessage"
)
public class StreamMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = StreamMessage.class.getName();

  /**
   * Stream key
   */
  protected CharSequence key = null;

  /**
   * Stream name
   */
  protected CharSequence name = null;

  /**
   * Stream description
   */
  protected CharSequence description = null;

  /**
   * Top types in stream schema
   */
  protected ObjectArrayList<TypeDescriptor> topTypes = null;

  /**
   * All types in stream schema
   */
  protected ObjectArrayList<UniqueDescriptor> allTypes = null;

  /**
   * Stream key
   * @return Key
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getKey() {
    return key;
  }

  /**
   * Stream key
   * @param value - Key
   */
  public void setKey(CharSequence value) {
    this.key = value;
  }

  /**
   * Stream key
   * @return true if Key is not null
   */
  public boolean hasKey() {
    return key != null;
  }

  /**
   * Stream key
   */
  public void nullifyKey() {
    this.key = null;
  }

  /**
   * Stream name
   * @return Name
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getName() {
    return name;
  }

  /**
   * Stream name
   * @param value - Name
   */
  public void setName(CharSequence value) {
    this.name = value;
  }

  /**
   * Stream name
   * @return true if Name is not null
   */
  public boolean hasName() {
    return name != null;
  }

  /**
   * Stream name
   */
  public void nullifyName() {
    this.name = null;
  }

  /**
   * Stream description
   * @return Description
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getDescription() {
    return description;
  }

  /**
   * Stream description
   * @param value - Description
   */
  public void setDescription(CharSequence value) {
    this.description = value;
  }

  /**
   * Stream description
   * @return true if Description is not null
   */
  public boolean hasDescription() {
    return description != null;
  }

  /**
   * Stream description
   */
  public void nullifyDescription() {
    this.description = null;
  }

  /**
   * Top types in stream schema
   * @return Top Types
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            TypeDescriptor.class}

  )
  public ObjectArrayList<TypeDescriptor> getTopTypes() {
    return topTypes;
  }

  /**
   * Top types in stream schema
   * @param value - Top Types
   */
  public void setTopTypes(ObjectArrayList<TypeDescriptor> value) {
    this.topTypes = value;
  }

  /**
   * Top types in stream schema
   * @return true if Top Types is not null
   */
  public boolean hasTopTypes() {
    return topTypes != null;
  }

  /**
   * Top types in stream schema
   */
  public void nullifyTopTypes() {
    this.topTypes = null;
  }

  /**
   * All types in stream schema
   * @return All Types
   */
  @SchemaElement
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            TypeDescriptor.class, EnumDescriptor.class}

  )
  public ObjectArrayList<UniqueDescriptor> getAllTypes() {
    return allTypes;
  }

  /**
   * All types in stream schema
   * @param value - All Types
   */
  public void setAllTypes(ObjectArrayList<UniqueDescriptor> value) {
    this.allTypes = value;
  }

  /**
   * All types in stream schema
   * @return true if All Types is not null
   */
  public boolean hasAllTypes() {
    return allTypes != null;
  }

  /**
   * All types in stream schema
   */
  public void nullifyAllTypes() {
    this.allTypes = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected StreamMessage createInstance() {
    return new StreamMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public StreamMessage nullify() {
    super.nullify();
    nullifyKey();
    nullifyName();
    nullifyDescription();
    nullifyTopTypes();
    nullifyAllTypes();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public StreamMessage reset() {
    super.reset();
    key = null;
    name = null;
    description = null;
    topTypes = null;
    allTypes = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public StreamMessage clone() {
    StreamMessage t = createInstance();
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
    if (!(obj instanceof StreamMessage)) return false;
    StreamMessage other =(StreamMessage)obj;
    if (hasKey() != other.hasKey()) return false;
    if (hasKey()) {
      if (getKey().length() != other.getKey().length()) return false; else {
        CharSequence s1 = getKey();
        CharSequence s2 = other.getKey();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasName() != other.hasName()) return false;
    if (hasName()) {
      if (getName().length() != other.getName().length()) return false; else {
        CharSequence s1 = getName();
        CharSequence s2 = other.getName();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasDescription() != other.hasDescription()) return false;
    if (hasDescription()) {
      if (getDescription().length() != other.getDescription().length()) return false; else {
        CharSequence s1 = getDescription();
        CharSequence s2 = other.getDescription();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasTopTypes() != other.hasTopTypes()) return false;
    if (hasTopTypes()) {
      if (getTopTypes().size() != other.getTopTypes().size()) return false;
      else for (int j = 0; j < getTopTypes().size(); ++j) {
        if ((getTopTypes().get(j) != null) != (other.getTopTypes().get(j) != null)) return false;
        if (getTopTypes().get(j) != null && !getTopTypes().get(j).equals(other.getTopTypes().get(j))) return false;
      }
    }
    if (hasAllTypes() != other.hasAllTypes()) return false;
    if (hasAllTypes()) {
      if (getAllTypes().size() != other.getAllTypes().size()) return false;
      else for (int j = 0; j < getAllTypes().size(); ++j) {
        if ((getAllTypes().get(j) != null) != (other.getAllTypes().get(j) != null)) return false;
        if (getAllTypes().get(j) != null && !getAllTypes().get(j).equals(other.getAllTypes().get(j))) return false;
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
    if (hasKey()) {
      hash = hash * 31 + getKey().hashCode();
    }
    if (hasName()) {
      hash = hash * 31 + getName().hashCode();
    }
    if (hasDescription()) {
      hash = hash * 31 + getDescription().hashCode();
    }
    if (hasTopTypes()) {
      for (int j = 0; j < getTopTypes().size(); ++j) {
        hash ^= getTopTypes().get(j).hashCode();
      }
    }
    if (hasAllTypes()) {
      for (int j = 0; j < getAllTypes().size(); ++j) {
        hash ^= getAllTypes().get(j).hashCode();
      }
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public StreamMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof StreamMessage) {
      StreamMessage t = (StreamMessage)template;
      if (t.hasKey()) {
        if (hasKey() && getKey() instanceof StringBuilder) {
          ((StringBuilder)getKey()).setLength(0);
        } else {
          setKey(new StringBuilder());
        }
        ((StringBuilder)getKey()).append(t.getKey());
      } else {
        nullifyKey();
      }
      if (t.hasName()) {
        if (hasName() && getName() instanceof StringBuilder) {
          ((StringBuilder)getName()).setLength(0);
        } else {
          setName(new StringBuilder());
        }
        ((StringBuilder)getName()).append(t.getName());
      } else {
        nullifyName();
      }
      if (t.hasDescription()) {
        if (hasDescription() && getDescription() instanceof StringBuilder) {
          ((StringBuilder)getDescription()).setLength(0);
        } else {
          setDescription(new StringBuilder());
        }
        ((StringBuilder)getDescription()).append(t.getDescription());
      } else {
        nullifyDescription();
      }
      if (t.hasTopTypes()) {
        if (!hasTopTypes()) {
          setTopTypes(new ObjectArrayList<TypeDescriptor>(t.getTopTypes().size()));
        } else {
          getTopTypes().clear();
        }
        for (int i = 0; i < t.getTopTypes().size(); ++i) ((ObjectArrayList<TypeDescriptor>)getTopTypes()).add((TypeDescriptor)t.getTopTypes().get(i).clone());
      } else {
        nullifyTopTypes();
      }
      if (t.hasAllTypes()) {
        if (!hasAllTypes()) {
          setAllTypes(new ObjectArrayList<UniqueDescriptor>(t.getAllTypes().size()));
        } else {
          getAllTypes().clear();
        }
        for (int i = 0; i < t.getAllTypes().size(); ++i) ((ObjectArrayList<UniqueDescriptor>)getAllTypes()).add((UniqueDescriptor)t.getAllTypes().get(i).clone());
      } else {
        nullifyAllTypes();
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
    str.append("{ \"$type\":  \"StreamMessage\"");
    if (hasKey()) {
      str.append(", \"key\": \"").append(getKey()).append("\"");
    }
    if (hasName()) {
      str.append(", \"name\": \"").append(getName()).append("\"");
    }
    if (hasDescription()) {
      str.append(", \"description\": \"").append(getDescription()).append("\"");
    }
    if (hasTopTypes()) {
      str.append(", \"topTypes\": [");
      if (getTopTypes().size() > 0) {
        if (getTopTypes().get(0) == null) {
          str.append("null");
        } else {
          getTopTypes().get(0).toString(str);
        }
      }
      for (int i = 1; i < getTopTypes().size(); ++i) {
        str.append(", ");
        if (getTopTypes().get(i) == null) {
          str.append("null");
        } else {
          getTopTypes().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasAllTypes()) {
      str.append(", \"allTypes\": [");
      if (getAllTypes().size() > 0) {
        if (getAllTypes().get(0) == null) {
          str.append("null");
        } else {
          getAllTypes().get(0).toString(str);
        }
      }
      for (int i = 1; i < getAllTypes().size(); ++i) {
        str.append(", ");
        if (getAllTypes().get(i) == null) {
          str.append("null");
        } else {
          getAllTypes().get(i).toString(str);
        }
      }
      str.append("]");
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
