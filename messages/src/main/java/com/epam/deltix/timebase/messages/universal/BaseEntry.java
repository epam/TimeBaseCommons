package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.*;

/**
 * Base class for market data entry to be included in package (PackageHeader).
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.BaseEntry",
    title = "Base Entry"
)
public class BaseEntry implements BaseEntryInterface {
  public static final String CLASS_NAME = BaseEntry.class.getName();

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   */
  protected long exchangeId = TypeConstants.EXCHANGE_NULL;

  /**
   * True, if quote (or trade) comes from an implied Order book.
   */
  protected byte isImplied = TypeConstants.BOOLEAN_NULL;

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   */
  protected long contractId = TypeConstants.INT64_NULL;

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @return Exchange Code
   */
  @SchemaElement(
      title = "Exchange Code"
  )
  @SchemaType(
      encoding = "ALPHANUMERIC(10)",
      dataType = SchemaDataType.VARCHAR
  )
  public long getExchangeId() {
    return exchangeId;
  }

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @param value - Exchange Code
   */
  public void setExchangeId(long value) {
    this.exchangeId = value;
  }

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @return true if Exchange Code is not null
   */
  public boolean hasExchangeId() {
    return exchangeId != TypeConstants.EXCHANGE_NULL;
  }

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   */
  public void nullifyExchangeId() {
    this.exchangeId = TypeConstants.EXCHANGE_NULL;
  }

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @return Is Implied
   */
  @SchemaElement(
      title = "Is Implied"
  )
  @SchemaType(
      dataType = SchemaDataType.BOOLEAN
  )
  public boolean isImplied() {
    return isImplied == 1;
  }

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @param value - Is Implied
   */
  public void setIsImplied(boolean value) {
    this.isImplied = (byte)(value ? 1 : 0);
  }

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @return true if Is Implied is not null
   */
  public boolean hasIsImplied() {
    return isImplied != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * True, if quote (or trade) comes from an implied Order book.
   */
  public void nullifyIsImplied() {
    this.isImplied = TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @return Contract ID
   */
  @SchemaElement(
      title = "Contract ID"
  )
  @SchemaType(
      encoding = "ALPHANUMERIC(10)",
      dataType = SchemaDataType.VARCHAR
  )
  public long getContractId() {
    return contractId;
  }

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @param value - Contract ID
   */
  public void setContractId(long value) {
    this.contractId = value;
  }

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @return true if Contract ID is not null
   */
  public boolean hasContractId() {
    return contractId != TypeConstants.INT64_NULL;
  }

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   */
  public void nullifyContractId() {
    this.contractId = TypeConstants.INT64_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  protected BaseEntry createInstance() {
    return new BaseEntry();
  }

  /**
   * Method nullifies all instance properties
   */
  public BaseEntry nullify() {
    nullifyExchangeId();
    nullifyIsImplied();
    nullifyContractId();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  public BaseEntry reset() {
    exchangeId = TypeConstants.EXCHANGE_NULL;
    isImplied = TypeConstants.BOOLEAN_NULL;
    contractId = TypeConstants.INT64_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  public BaseEntry clone() {
    BaseEntry t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof BaseEntryInfo)) return false;
    BaseEntryInfo other =(BaseEntryInfo)obj;
    if (hasExchangeId() != other.hasExchangeId()) return false;
    if (hasExchangeId() && getExchangeId() != other.getExchangeId()) return false;
    if (hasIsImplied() != other.hasIsImplied()) return false;
    if (hasIsImplied() && isImplied() != other.isImplied()) return false;
    if (hasContractId() != other.hasContractId()) return false;
    if (hasContractId() && getContractId() != other.getContractId()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (hasExchangeId()) {
      hash = hash * 31 + ((int)(getExchangeId() ^ (getExchangeId() >>> 32)));
    }
    if (hasIsImplied()) {
      hash = hash * 31 + (isImplied() ? 1231 : 1237);
    }
    if (hasContractId()) {
      hash = hash * 31 + ((int)(getContractId() ^ (getContractId() >>> 32)));
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  public BaseEntry copyFrom(RecordInfo template) {
    if (template instanceof BaseEntryInfo) {
      BaseEntryInfo t = (BaseEntryInfo)template;
      if (t.hasExchangeId()) {
        setExchangeId(t.getExchangeId());
      } else {
        nullifyExchangeId();
      }
      if (t.hasIsImplied()) {
        setIsImplied(t.isImplied());
      } else {
        nullifyIsImplied();
      }
      if (t.hasContractId()) {
        setContractId(t.getContractId());
      } else {
        nullifyContractId();
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
    str.append("{ \"$type\":  \"BaseEntry\"");
    if (hasExchangeId()) {
      str.append(", \"exchangeId\": ").append(getExchangeId());
    }
    if (hasIsImplied()) {
      str.append(", \"isImplied\": ").append(isImplied());
    }
    if (hasContractId()) {
      str.append(", \"contractId\": ").append(getContractId());
    }
    str.append("}");
    return str;
  }
}
