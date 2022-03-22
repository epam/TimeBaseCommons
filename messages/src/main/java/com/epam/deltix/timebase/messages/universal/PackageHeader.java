package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.*;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Represents market data package.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.PackageHeader",
    title = "Package Header"
)
public class PackageHeader extends MarketMessage implements PackageHeaderInterface {
  public static final String CLASS_NAME = PackageHeader.class.getName();

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   */
  protected ObjectArrayList<BaseEntryInfo> entries = null;

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   */
  protected PackageType packageType = null;

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @return Entries
   */
  @SchemaElement(
      title = "Entries"
  )
  @SchemaArrayType(
      isNullable = false,
      isElementNullable = false,
      elementTypes =  {
            TradeEntry.class, L1Entry.class, L2EntryNew.class, L2EntryUpdate.class, L3EntryNew.class, L3EntryUpdate.class, BookResetEntry.class, StatisticsEntry.class}

  )
  public ObjectArrayList<BaseEntryInfo> getEntries() {
    return entries;
  }

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @param value - Entries
   */
  public void setEntries(ObjectArrayList<BaseEntryInfo> value) {
    this.entries = value;
  }

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @return true if Entries is not null
   */
  public boolean hasEntries() {
    return entries != null;
  }

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   */
  public void nullifyEntries() {
    this.entries = null;
  }

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @return Package Type
   */
  @SchemaType(
      isNullable = false
  )
  @SchemaElement(
      title = "Package Type"
  )
  public PackageType getPackageType() {
    return packageType;
  }

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @param value - Package Type
   */
  public void setPackageType(PackageType value) {
    this.packageType = value;
  }

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @return true if Package Type is not null
   */
  public boolean hasPackageType() {
    return packageType != null;
  }

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   */
  public void nullifyPackageType() {
    this.packageType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected PackageHeader createInstance() {
    return new PackageHeader();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public PackageHeader nullify() {
    super.nullify();
    nullifyEntries();
    nullifyPackageType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public PackageHeader reset() {
    super.reset();
    entries = null;
    packageType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public PackageHeader clone() {
    PackageHeader t = createInstance();
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
    if (!(obj instanceof PackageHeaderInfo)) return false;
    PackageHeaderInfo other =(PackageHeaderInfo)obj;
    if (hasEntries() != other.hasEntries()) return false;
    if (hasEntries()) {
      if (getEntries().size() != other.getEntries().size()) return false;
      else for (int j = 0; j < getEntries().size(); ++j) {
        if ((getEntries().get(j) != null) != (other.getEntries().get(j) != null)) return false;
        if (getEntries().get(j) != null && !getEntries().get(j).equals(other.getEntries().get(j))) return false;
      }
    }
    if (hasPackageType() != other.hasPackageType()) return false;
    if (hasPackageType() && getPackageType() != other.getPackageType()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasEntries()) {
      for (int j = 0; j < getEntries().size(); ++j) {
        hash ^= getEntries().get(j).hashCode();
      }
    }
    if (hasPackageType()) {
      hash = hash * 31 + getPackageType().getNumber();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public PackageHeader copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof PackageHeaderInfo) {
      PackageHeaderInfo t = (PackageHeaderInfo)template;
      if (t.hasEntries()) {
        if (!hasEntries()) {
          setEntries(new ObjectArrayList<BaseEntryInfo>(t.getEntries().size()));
        } else {
          getEntries().clear();
        }
        for (int i = 0; i < t.getEntries().size(); ++i) ((ObjectArrayList<BaseEntryInfo>)getEntries()).add((BaseEntryInfo)t.getEntries().get(i).clone());
      } else {
        nullifyEntries();
      }
      if (t.hasPackageType()) {
        setPackageType(t.getPackageType());
      } else {
        nullifyPackageType();
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
    str.append("{ \"$type\":  \"PackageHeader\"");
    if (hasEntries()) {
      str.append(", \"entries\": [");
      if (getEntries().size() > 0) {
        if (getEntries().get(0) == null) {
          str.append("null");
        } else {
          getEntries().get(0).toString(str);
        }
      }
      for (int i = 1; i < getEntries().size(); ++i) {
        str.append(", ");
        if (getEntries().get(i) == null) {
          str.append("null");
        } else {
          getEntries().get(i).toString(str);
        }
      }
      str.append("]");
    }
    if (hasPackageType()) {
      str.append(", \"packageType\": \"").append(getPackageType()).append("\"");
    }
    if (hasOriginalTimestamp()) {
      str.append(", \"originalTimestamp\": \"").append(getOriginalTimestamp()).append("\"");
    }
    if (hasCurrency()) {
      str.append(", \"currency\": ").append(getCurrency());
    }
    if (hasSequenceNumber()) {
      str.append(", \"sequenceNumber\": ").append(getSequenceNumber());
    }
    if (hasSourceId()) {
      str.append(", \"sourceId\": ").append(getSourceId());
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
