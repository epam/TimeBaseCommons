package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.timebase.messages.InstrumentMessage;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.CharSequence;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.SecurityFeedStatusMessage",
    title = "Security StatusMessage"
)
public class SecurityFeedStatusMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = SecurityFeedStatusMessage.class.getName();

  /**
   * Exchange code.
   */
  protected long exchangeId = TypeConstants.EXCHANGE_NULL;

  /**
   * Main status classification.
   */
  protected FeedStatus status = null;

  /**
   * Nullable original status sent by vendor.
   */
  protected CharSequence originalStatus = null;

  /**
   * Deltix comment about this status.
   */
  protected CharSequence cause = null;

  /**
   * Exchange code.
   * @return ExchangeId
   */
  @SchemaElement(
      name = "exchangeId",
      title = "ExchangeId"
  )
  public long getExchangeId() {
    return exchangeId;
  }

  /**
   * Exchange code.
   * @param value - ExchangeId
   */
  public void setExchangeId(long value) {
    this.exchangeId = value;
  }

  /**
   * Exchange code.
   * @return true if ExchangeId is not null
   */
  public boolean hasExchangeId() {
    return exchangeId != com.epam.deltix.timebase.messages.TypeConstants.EXCHANGE_NULL;
  }

  /**
   * Exchange code.
   */
  public void nullifyExchangeId() {
    this.exchangeId = com.epam.deltix.timebase.messages.TypeConstants.EXCHANGE_NULL;
  }

  /**
   * Main status classification.
   * @return Status
   */
  @SchemaElement(
      name = "status",
      title = "Status"
  )
  public FeedStatus getStatus() {
    return status;
  }

  /**
   * Main status classification.
   * @param value - Status
   */
  public void setStatus(FeedStatus value) {
    this.status = value;
  }

  /**
   * Main status classification.
   * @return true if Status is not null
   */
  public boolean hasStatus() {
    return status != null;
  }

  /**
   * Main status classification.
   */
  public void nullifyStatus() {
    this.status = null;
  }

  /**
   * Nullable original status sent by vendor.
   * @return OriginalStatus
   */
  @SchemaElement(
      name = "originalStatus",
      title = "OriginalStatus"
  )
  public CharSequence getOriginalStatus() {
    return originalStatus;
  }

  /**
   * Nullable original status sent by vendor.
   * @param value - OriginalStatus
   */
  public void setOriginalStatus(CharSequence value) {
    this.originalStatus = value;
  }

  /**
   * Nullable original status sent by vendor.
   * @return true if OriginalStatus is not null
   */
  public boolean hasOriginalStatus() {
    return originalStatus != null;
  }

  /**
   * Nullable original status sent by vendor.
   */
  public void nullifyOriginalStatus() {
    this.originalStatus = null;
  }

  /**
   * Deltix comment about this status.
   * @return Cause
   */
  @SchemaElement(
      name = "cause",
      title = "Cause"
  )
  public CharSequence getCause() {
    return cause;
  }

  /**
   * Deltix comment about this status.
   * @param value - Cause
   */
  public void setCause(CharSequence value) {
    this.cause = value;
  }

  /**
   * Deltix comment about this status.
   * @return true if Cause is not null
   */
  public boolean hasCause() {
    return cause != null;
  }

  /**
   * Deltix comment about this status.
   */
  public void nullifyCause() {
    this.cause = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected SecurityFeedStatusMessage createInstance() {
    return new SecurityFeedStatusMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public SecurityFeedStatusMessage nullify() {
    super.nullify();
    nullifyExchangeId();
    nullifyStatus();
    nullifyOriginalStatus();
    nullifyCause();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public SecurityFeedStatusMessage reset() {
    super.reset();
    exchangeId = com.epam.deltix.timebase.messages.TypeConstants.EXCHANGE_NULL;
    status = null;
    originalStatus = null;
    cause = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public SecurityFeedStatusMessage clone() {
    SecurityFeedStatusMessage t = createInstance();
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
    if (!(obj instanceof SecurityFeedStatusMessage)) return false;
    SecurityFeedStatusMessage other =(SecurityFeedStatusMessage)obj;
    if (hasExchangeId() != other.hasExchangeId()) return false;
    if (hasExchangeId() && getExchangeId() != other.getExchangeId()) return false;
    if (hasStatus() != other.hasStatus()) return false;
    if (hasStatus() && getStatus() != other.getStatus()) return false;
    if (hasOriginalStatus() != other.hasOriginalStatus()) return false;
    if (hasOriginalStatus()) {
      if (getOriginalStatus().length() != other.getOriginalStatus().length()) return false; else {
        CharSequence s1 = getOriginalStatus();
        CharSequence s2 = other.getOriginalStatus();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasCause() != other.hasCause()) return false;
    if (hasCause()) {
      if (getCause().length() != other.getCause().length()) return false; else {
        CharSequence s1 = getCause();
        CharSequence s2 = other.getCause();
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
    if (hasExchangeId()) {
      hash = hash * 31 + ((int)(getExchangeId() ^ (getExchangeId() >>> 32)));
    }
    if (hasStatus()) {
      hash = hash * 31 + getStatus().getNumber();
    }
    if (hasOriginalStatus()) {
      hash = hash * 31 + getOriginalStatus().hashCode();
    }
    if (hasCause()) {
      hash = hash * 31 + getCause().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public SecurityFeedStatusMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof SecurityFeedStatusMessage) {
      SecurityFeedStatusMessage t = (SecurityFeedStatusMessage)template;
      if (t.hasExchangeId()) {
        setExchangeId(t.getExchangeId());
      } else {
        nullifyExchangeId();
      }
      if (t.hasStatus()) {
        setStatus(t.getStatus());
      } else {
        nullifyStatus();
      }
      if (t.hasOriginalStatus()) {
        if (hasOriginalStatus() && getOriginalStatus() instanceof StringBuilder) {
          ((StringBuilder)getOriginalStatus()).setLength(0);
        } else {
          setOriginalStatus(new StringBuilder());
        }
        ((StringBuilder)getOriginalStatus()).append(t.getOriginalStatus());
      } else {
        nullifyOriginalStatus();
      }
      if (t.hasCause()) {
        if (hasCause() && getCause() instanceof StringBuilder) {
          ((StringBuilder)getCause()).setLength(0);
        } else {
          setCause(new StringBuilder());
        }
        ((StringBuilder)getCause()).append(t.getCause());
      } else {
        nullifyCause();
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
    str.append("{ \"$type\":  \"SecurityFeedStatusMessage\"");
    if (hasExchangeId()) {
      str.append(", \"exchangeId\": ").append(getExchangeId());
    }
    if (hasStatus()) {
      str.append(", \"status\": \"").append(getStatus()).append("\"");
    }
    if (hasOriginalStatus()) {
      str.append(", \"originalStatus\": \"").append(getOriginalStatus()).append("\"");
    }
    if (hasCause()) {
      str.append(", \"cause\": \"").append(getCause()).append("\"");
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
