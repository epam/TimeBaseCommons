package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Schema definition for a schema node.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.UniqueDescriptor",
    title = "UniqueDescriptor"
)
public abstract class UniqueDescriptor extends Descriptor implements RecordInterface {
  public static final String CLASS_NAME = UniqueDescriptor.class.getName();

  /**
   * Optional GUID for this node.
   */
  protected CharSequence guid = null;

  /**
   * Optional GUID for this node.
   * @return Guid
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getGuid() {
    return guid;
  }

  /**
   * Optional GUID for this node.
   * @param value - Guid
   */
  public void setGuid(CharSequence value) {
    this.guid = value;
  }

  /**
   * Optional GUID for this node.
   * @return true if Guid is not null
   */
  public boolean hasGuid() {
    return guid != null;
  }

  /**
   * Optional GUID for this node.
   */
  public void nullifyGuid() {
    this.guid = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected UniqueDescriptor createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public UniqueDescriptor nullify() {
    super.nullify();
    nullifyGuid();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public UniqueDescriptor reset() {
    super.reset();
    guid = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public UniqueDescriptor clone() {
    UniqueDescriptor t = createInstance();
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
    if (!(obj instanceof UniqueDescriptor)) return false;
    UniqueDescriptor other =(UniqueDescriptor)obj;
    if (hasGuid() != other.hasGuid()) return false;
    if (hasGuid()) {
      if (getGuid().length() != other.getGuid().length()) return false; else {
        CharSequence s1 = getGuid();
        CharSequence s2 = other.getGuid();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
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
    if (hasGuid()) {
      hash = hash * 31 + getGuid().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public UniqueDescriptor copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof UniqueDescriptor) {
      UniqueDescriptor t = (UniqueDescriptor)template;
      if (t.hasGuid()) {
        if (hasGuid() && getGuid() instanceof StringBuilder) {
          ((StringBuilder)getGuid()).setLength(0);
        } else {
          setGuid(new StringBuilder());
        }
        ((StringBuilder)getGuid()).append(t.getGuid());
      } else {
        nullifyGuid();
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
    str.append("{ \"$type\":  \"UniqueDescriptor\"");
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
