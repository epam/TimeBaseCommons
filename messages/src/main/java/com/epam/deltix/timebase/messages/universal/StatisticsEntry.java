package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.*;

/**
 * Represents arbitrary exchange event.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.StatisticsEntry",
    title = "StatisticsEntry"
)
public class StatisticsEntry extends BaseEntry implements StatisticsEntryInterface {
  public static final String CLASS_NAME = StatisticsEntry.class.getName();

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   */
  protected StatisticsType type = null;

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   */
  protected long value = TypeConstants.DECIMAL_NULL;

  /**
   * Original event type, vendor specific.
   */
  protected CharSequence originalType = null;

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return Type
   */
  @SchemaElement(
      title = "Type"
  )
  public StatisticsType getType() {
    return type;
  }

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @param value - Type
   */
  public void setType(StatisticsType value) {
    this.type = value;
  }

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   * @return true if Type is not null
   */
  public boolean hasType() {
    return type != null;
  }

  /**
   * Type of the event and meaning of the value.
   * Not nullable, use StatisticsType.Custom if unknown/non-mapped
   */
  public void nullifyType() {
    this.type = null;
  }

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return Value
   */
  @SchemaElement(
      title = "Value"
  )
  @SchemaType(
      encoding = "DECIMAL64",
      isNullable = true,
      dataType = SchemaDataType.FLOAT
  )
  public long getValue() {
    return value;
  }

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @param value - Value
   */
  public void setValue(long value) {
    this.value = value;
  }

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   * @return true if Value is not null
   */
  public boolean hasValue() {
    return value != TypeConstants.DECIMAL_NULL;
  }

  /**
   * Generic field to store value. Meaning is depends on StatisticsType
   */
  public void nullifyValue() {
    this.value = TypeConstants.DECIMAL_NULL;
  }

  /**
   * Original event type, vendor specific.
   * @return OriginalType
   */
  @SchemaElement(
      title = "OriginalType"
  )
  public CharSequence getOriginalType() {
    return originalType;
  }

  /**
   * Original event type, vendor specific.
   * @param value - OriginalType
   */
  public void setOriginalType(CharSequence value) {
    this.originalType = value;
  }

  /**
   * Original event type, vendor specific.
   * @return true if OriginalType is not null
   */
  public boolean hasOriginalType() {
    return originalType != null;
  }

  /**
   * Original event type, vendor specific.
   */
  public void nullifyOriginalType() {
    this.originalType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected StatisticsEntry createInstance() {
    return new StatisticsEntry();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public StatisticsEntry nullify() {
    super.nullify();
    nullifyType();
    nullifyValue();
    nullifyOriginalType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public StatisticsEntry reset() {
    super.reset();
    type = null;
    value = TypeConstants.DECIMAL_NULL;
    originalType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public StatisticsEntry clone() {
    StatisticsEntry t = createInstance();
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
    if (!(obj instanceof StatisticsEntryInfo)) return false;
    StatisticsEntryInfo other =(StatisticsEntryInfo)obj;
    if (hasType() != other.hasType()) return false;
    if (hasType() && getType() != other.getType()) return false;
    if (hasValue() != other.hasValue()) return false;
    if (hasValue() && !Decimal64Utils.equals(getValue(), other.getValue())) return false;
    if (hasOriginalType() != other.hasOriginalType()) return false;
    if (hasOriginalType()) {
      if (getOriginalType().length() != other.getOriginalType().length()) return false; else {
        CharSequence s1 = getOriginalType();
        CharSequence s2 = other.getOriginalType();
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
    if (hasType()) {
      hash = hash * 31 + getType().getNumber();
    }
    if (hasValue()) {
      hash = hash * 31 + ((int)(getValue() ^ (getValue() >>> 32)));
    }
    if (hasOriginalType()) {
      hash = hash * 31 + getOriginalType().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public StatisticsEntry copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof StatisticsEntryInfo) {
      StatisticsEntryInfo t = (StatisticsEntryInfo)template;
      if (t.hasType()) {
        setType(t.getType());
      } else {
        nullifyType();
      }
      if (t.hasValue()) {
        setValue(t.getValue());
      } else {
        nullifyValue();
      }
      if (t.hasOriginalType()) {
        if (hasOriginalType() && getOriginalType() instanceof StringBuilder) {
          ((StringBuilder)getOriginalType()).setLength(0);
        } else {
          setOriginalType(new StringBuilder());
        }
        ((StringBuilder)getOriginalType()).append(t.getOriginalType());
      } else {
        nullifyOriginalType();
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
    str.append("{ \"$type\":  \"StatisticsEntry\"");
    if (hasType()) {
      str.append(", \"type\": \"").append(getType()).append("\"");
    }
    if (hasValue()) {
      str.append(", \"value\": ");
      Decimal64Utils.appendTo(getValue(), str);
    }
    if (hasOriginalType()) {
      str.append(", \"originalType\": \"").append(getOriginalType()).append("\"");
    }
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
