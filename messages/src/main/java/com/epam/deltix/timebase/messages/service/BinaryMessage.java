package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.InstrumentMessage;
import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaDataType;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaGuid;
import com.epam.deltix.timebase.messages.SchemaType;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import com.epam.deltix.containers.BinaryArray;
import com.epam.deltix.containers.interfaces.BinaryArrayReadOnly;
import com.epam.deltix.containers.interfaces.BinaryArrayReadWrite;

/**
 */
@OldElementName("deltix.qsrv.hf.pub.BinaryMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.BinaryMessage",
    title = "Binary Message"
)
@SchemaGuid(BinaryMessage.DESCRIPTOR_GUID)
public class BinaryMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = BinaryMessage.class.getName();

  /**
   */
  public static final String DESCRIPTOR_GUID = "SYS:BinaryMessage:1";

  /**
   */
  protected BinaryArrayReadOnly data = null;

  /**
   * @return Data buffer
   */
  @SchemaElement(
      name = "data",
      title = "Data buffer"
  )
  @SchemaType(
      dataType = SchemaDataType.BINARY
  )
  public BinaryArrayReadOnly getData() {
    return data;
  }

  /**
   * @param value - Data buffer
   */
  public void setData(BinaryArrayReadOnly value) {
    this.data = value;
  }

  /**
   * @return true if Data buffer is not null
   */
  public boolean hasData() {
    return data != null;
  }

  /**
   */
  public void nullifyData() {
    this.data = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected BinaryMessage createInstance() {
    return new BinaryMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public BinaryMessage nullify() {
    super.nullify();
    nullifyData();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public BinaryMessage reset() {
    super.reset();
    data = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public BinaryMessage clone() {
    BinaryMessage t = createInstance();
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
    if (!(obj instanceof BinaryMessage)) return false;
    BinaryMessage other =(BinaryMessage)obj;
    if (hasData() != other.hasData()) return false;
    if (hasData()) {
      if (getData().size() != other.getData().size()) return false; else {
        BinaryArrayReadOnly b1 = getData();
        BinaryArrayReadOnly b2 = other.getData();
        if ((b1 instanceof BinaryArray && b2 instanceof BinaryArray)) {
          if (!b1.equals(b2)) return false;
        } else {
          for (int i = 0; i < b1.size(); ++i) if (b1.get(i) != b2.get(i)) return false;
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
    if (hasData()) {
      hash = hash * 31 + getData().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public BinaryMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof BinaryMessage) {
      BinaryMessage t = (BinaryMessage)template;
      if (t.hasData()) {
        if (!(hasData() && getData() instanceof BinaryArrayReadWrite)) {
          setData(new BinaryArray());
        }
        ((BinaryArrayReadWrite)getData()).assign(t.getData());
      } else {
        nullifyData();
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
    str.append("{ \"$type\":  \"BinaryMessage\"");
    if (hasData()) {
      str.append(", \"data\": ").append(getData());
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
