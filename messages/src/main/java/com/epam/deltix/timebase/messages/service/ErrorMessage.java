package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.*;

/**
 */
@OldElementName("deltix.qsrv.hf.pub.ErrorMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.ErrorMessage",
    title = "Error Message"
)
@SchemaGuid(ErrorMessage.DESCRIPTOR_GUID)
public class ErrorMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = ErrorMessage.class.getName();

  /**
   */
  public static final String DESCRIPTOR_GUID = "SYS:ErrorMessage:2";

  /**
   */
  protected CharSequence errorType = null;

  /**
   */
  protected long seqNum = 0L;

  /**
   */
  protected ErrorLevel level = null;

  /**
   */
  protected CharSequence messageText = null;

  /**
   */
  protected CharSequence details = null;

  /**
   * @return ErrorType
   */
  @SchemaElement(
      name = "errorType",
      title = "ErrorType"
  )
  public CharSequence getErrorType() {
    return errorType;
  }

  /**
   * @param value - ErrorType
   */
  public void setErrorType(CharSequence value) {
    this.errorType = value;
  }

  /**
   * @return true if ErrorType is not null
   */
  public boolean hasErrorType() {
    return errorType != null;
  }

  /**
   */
  public void nullifyErrorType() {
    this.errorType = null;
  }

  /**
   * @return Sequence Number
   */
  @SchemaElement(
      name = "seqNum",
      title = "Sequence Number"
  )
  public long getSeqNum() {
    return seqNum;
  }

  /**
   * @param value - Sequence Number
   */
  public void setSeqNum(long value) {
    this.seqNum = value;
  }

  /**
   * @return true if Sequence Number is not null
   */
  public boolean hasSeqNum() {
    return seqNum != TypeConstants.INT64_NULL;
  }

  /**
   */
  public void nullifySeqNum() {
    this.seqNum = TypeConstants.INT64_NULL;
  }

  /**
   * @return Error Level
   */
  @SchemaElement(
      name = "level",
      title = "Error Level"
  )
  public ErrorLevel getLevel() {
    return level;
  }

  /**
   * @param value - Error Level
   */
  public void setLevel(ErrorLevel value) {
    this.level = value;
  }

  /**
   * @return true if Error Level is not null
   */
  public boolean hasLevel() {
    return level != null;
  }

  /**
   */
  public void nullifyLevel() {
    this.level = null;
  }

  /**
   * @return Text
   */
  @SchemaElement(
      name = "messageText",
      title = "Text"
  )
  public CharSequence getMessageText() {
    return messageText;
  }

  /**
   * @param value - Text
   */
  public void setMessageText(CharSequence value) {
    this.messageText = value;
  }

  /**
   * @return true if Text is not null
   */
  public boolean hasMessageText() {
    return messageText != null;
  }

  /**
   */
  public void nullifyMessageText() {
    this.messageText = null;
  }

  /**
   * @return Details
   */
  @SchemaElement(
      name = "details",
      title = "Details"
  )
  public CharSequence getDetails() {
    return details;
  }

  /**
   * @param value - Details
   */
  public void setDetails(CharSequence value) {
    this.details = value;
  }

  /**
   * @return true if Details is not null
   */
  public boolean hasDetails() {
    return details != null;
  }

  /**
   */
  public void nullifyDetails() {
    this.details = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected ErrorMessage createInstance() {
    return new ErrorMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public ErrorMessage nullify() {
    super.nullify();
    nullifyErrorType();
    nullifySeqNum();
    nullifyLevel();
    nullifyMessageText();
    nullifyDetails();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public ErrorMessage reset() {
    super.reset();
    errorType = null;
    seqNum = 0L;
    level = null;
    messageText = null;
    details = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public ErrorMessage clone() {
    ErrorMessage t = createInstance();
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
    if (!(obj instanceof ErrorMessage)) return false;
    ErrorMessage other =(ErrorMessage)obj;
    if (hasErrorType() != other.hasErrorType()) return false;
    if (hasErrorType()) {
      if (getErrorType().length() != other.getErrorType().length()) return false; else {
        CharSequence s1 = getErrorType();
        CharSequence s2 = other.getErrorType();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasSeqNum() != other.hasSeqNum()) return false;
    if (hasSeqNum() && getSeqNum() != other.getSeqNum()) return false;
    if (hasLevel() != other.hasLevel()) return false;
    if (hasLevel() && getLevel() != other.getLevel()) return false;
    if (hasMessageText() != other.hasMessageText()) return false;
    if (hasMessageText()) {
      if (getMessageText().length() != other.getMessageText().length()) return false; else {
        CharSequence s1 = getMessageText();
        CharSequence s2 = other.getMessageText();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasDetails() != other.hasDetails()) return false;
    if (hasDetails()) {
      if (getDetails().length() != other.getDetails().length()) return false; else {
        CharSequence s1 = getDetails();
        CharSequence s2 = other.getDetails();
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
    if (hasErrorType()) {
      hash = hash * 31 + getErrorType().hashCode();
    }
    if (hasSeqNum()) {
      hash = hash * 31 + ((int)(getSeqNum() ^ (getSeqNum() >>> 32)));
    }
    if (hasLevel()) {
      hash = hash * 31 + getLevel().getNumber();
    }
    if (hasMessageText()) {
      hash = hash * 31 + getMessageText().hashCode();
    }
    if (hasDetails()) {
      hash = hash * 31 + getDetails().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public ErrorMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof ErrorMessage) {
      ErrorMessage t = (ErrorMessage)template;
      if (t.hasErrorType()) {
        if (hasErrorType() && getErrorType() instanceof StringBuilder) {
          ((StringBuilder)getErrorType()).setLength(0);
        } else {
          setErrorType(new StringBuilder());
        }
        ((StringBuilder)getErrorType()).append(t.getErrorType());
      } else {
        nullifyErrorType();
      }
      if (t.hasSeqNum()) {
        setSeqNum(t.getSeqNum());
      } else {
        nullifySeqNum();
      }
      if (t.hasLevel()) {
        setLevel(t.getLevel());
      } else {
        nullifyLevel();
      }
      if (t.hasMessageText()) {
        if (hasMessageText() && getMessageText() instanceof StringBuilder) {
          ((StringBuilder)getMessageText()).setLength(0);
        } else {
          setMessageText(new StringBuilder());
        }
        ((StringBuilder)getMessageText()).append(t.getMessageText());
      } else {
        nullifyMessageText();
      }
      if (t.hasDetails()) {
        if (hasDetails() && getDetails() instanceof StringBuilder) {
          ((StringBuilder)getDetails()).setLength(0);
        } else {
          setDetails(new StringBuilder());
        }
        ((StringBuilder)getDetails()).append(t.getDetails());
      } else {
        nullifyDetails();
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
    str.append("{ \"$type\":  \"ErrorMessage\"");
    if (hasErrorType()) {
      str.append(", \"errorType\": \"").append(getErrorType()).append("\"");
    }
    if (hasSeqNum()) {
      str.append(", \"seqNum\": ").append(getSeqNum());
    }
    if (hasLevel()) {
      str.append(", \"level\": \"").append(getLevel()).append("\"");
    }
    if (hasMessageText()) {
      str.append(", \"messageText\": \"").append(getMessageText()).append("\"");
    }
    if (hasDetails()) {
      str.append(", \"details\": \"").append(getDetails()).append("\"");
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
