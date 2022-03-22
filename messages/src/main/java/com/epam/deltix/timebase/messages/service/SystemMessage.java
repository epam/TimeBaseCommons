package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.*;

/**
 * Base timebase system message.
 */
@OldElementName("deltix.qsrv.hf.pub.SystemMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.SystemMessage",
    title = "System Message"
)
public class SystemMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = SystemMessage.class.getName();

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected SystemMessage createInstance() {
    return new SystemMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public SystemMessage nullify() {
    super.nullify();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public SystemMessage reset() {
    super.reset();
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public SystemMessage clone() {
    SystemMessage t = createInstance();
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
    if (!(obj instanceof SystemMessage)) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public SystemMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
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
    str.append("{ \"$type\":  \"SystemMessage\"");
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
