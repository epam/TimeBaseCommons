package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * This is a base class for schema nodes.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.schema.Descriptor",
    title = "Descriptor"
)
public abstract class Descriptor implements RecordInterface {
  public static final String CLASS_NAME = Descriptor.class.getName();

  /**
   * Node name.
   */
  protected CharSequence name = null;

  /**
   * Node title.
   */
  protected CharSequence title = null;

  /**
   * Node description.
   */
  protected CharSequence description = null;

  /**
   * Node name.
   * @return Name
   */
  @SchemaElement
  @SchemaType(
      isNullable = false
  )
  public CharSequence getName() {
    return name;
  }

  /**
   * Node name.
   * @param value - Name
   */
  public void setName(CharSequence value) {
    this.name = value;
  }

  /**
   * Node name.
   * @return true if Name is not null
   */
  public boolean hasName() {
    return name != null;
  }

  /**
   * Node name.
   */
  public void nullifyName() {
    this.name = null;
  }

  /**
   * Node title.
   * @return Title
   */
  @SchemaElement
  @SchemaType(
      isNullable = true
  )
  public CharSequence getTitle() {
    return title;
  }

  /**
   * Node title.
   * @param value - Title
   */
  public void setTitle(CharSequence value) {
    this.title = value;
  }

  /**
   * Node title.
   * @return true if Title is not null
   */
  public boolean hasTitle() {
    return title != null;
  }

  /**
   * Node title.
   */
  public void nullifyTitle() {
    this.title = null;
  }

  /**
   * Node description.
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
   * Node description.
   * @param value - Description
   */
  public void setDescription(CharSequence value) {
    this.description = value;
  }

  /**
   * Node description.
   * @return true if Description is not null
   */
  public boolean hasDescription() {
    return description != null;
  }

  /**
   * Node description.
   */
  public void nullifyDescription() {
    this.description = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected Descriptor createInstance() {
    throw new UnsupportedOperationException();
  }

  /**
   * Method nullifies all instance properties
   */
  public Descriptor nullify() {
    nullifyName();
    nullifyTitle();
    nullifyDescription();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public Descriptor reset() {
    name = null;
    title = null;
    description = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public Descriptor clone() {
    Descriptor t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Descriptor)) return false;
    Descriptor other =(Descriptor)obj;
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
    if (hasTitle() != other.hasTitle()) return false;
    if (hasTitle()) {
      if (getTitle().length() != other.getTitle().length()) return false; else {
        CharSequence s1 = getTitle();
        CharSequence s2 = other.getTitle();
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
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasName()) {
      hash = hash * 31 + getName().hashCode();
    }
    if (hasTitle()) {
      hash = hash * 31 + getTitle().hashCode();
    }
    if (hasDescription()) {
      hash = hash * 31 + getDescription().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public Descriptor copyFrom(RecordInfo template) {
    if (template instanceof Descriptor) {
      Descriptor t = (Descriptor)template;
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
      if (t.hasTitle()) {
        if (hasTitle() && getTitle() instanceof StringBuilder) {
          ((StringBuilder)getTitle()).setLength(0);
        } else {
          setTitle(new StringBuilder());
        }
        ((StringBuilder)getTitle()).append(t.getTitle());
      } else {
        nullifyTitle();
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
    str.append("{ \"$type\":  \"Descriptor\"");
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
