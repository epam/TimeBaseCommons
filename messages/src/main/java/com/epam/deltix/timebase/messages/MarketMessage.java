/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.timebase.messages;

/**
 * Most financial market-related messages subclass this abstract class.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.MarketMessage",
    title = "Market Message"
)
public class MarketMessage extends InstrumentMessage implements MarketMessageInterface {
  public static final String CLASS_NAME = MarketMessage.class.getName();

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   */
  protected long originalTimestamp = TypeConstants.TIMESTAMP_UNKNOWN;

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   */
  protected long currency = TypeConstants.INT64_NULL;

  /**
   * Market specific identifier of the given event in a sequence of market events.
   */
  protected long sequenceNumber = TypeConstants.INT64_NULL;

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   */
  protected long sourceId = TypeConstants.INT64_NULL;

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return Original Timestamp
   */
  @SchemaType(
      dataType = SchemaDataType.TIMESTAMP
  )
  @SchemaElement(
      description = "Original Timestamp"
  )
  public long getOriginalTimestamp() {
    return originalTimestamp;
  }

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @param value - Original Timestamp
   */
  public void setOriginalTimestamp(long value) {
    this.originalTimestamp = value;
  }

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   * @return true if Original Timestamp is not null
   */
  public boolean hasOriginalTimestamp() {
    return originalTimestamp != TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * Exchange Time is measured in milliseconds that passed since January 1, 1970 UTC
   */
  public void nullifyOriginalTimestamp() {
    this.originalTimestamp = TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return Currency
   */
  @SchemaElement
  @SchemaType(
      encoding = "ALPHANUMERIC(10)",
      dataType = SchemaDataType.VARCHAR
  )
  public long getCurrency() {
    return currency;
  }

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @param value - Currency
   */
  public void setCurrency(long value) {
    this.currency = value;
  }

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   * @return true if Currency is not null
   */
  public boolean hasCurrency() {
    return currency != TypeConstants.INT64_NULL;
  }

  /**
   * Identifies currency in which quantity is denominated (for currencies this must be base or term currency)
   * using 'alphanumeric' encoding
   */
  public void nullifyCurrency() {
    this.currency = TypeConstants.INT64_NULL;
  }

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return Sequence Number
   */
  @SchemaElement
  public long getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @param value - Sequence Number
   */
  public void setSequenceNumber(long value) {
    this.sequenceNumber = value;
  }

  /**
   * Market specific identifier of the given event in a sequence of market events.
   * @return true if Sequence Number is not null
   */
  public boolean hasSequenceNumber() {
    return sequenceNumber != TypeConstants.INT64_NULL;
  }

  /**
   * Market specific identifier of the given event in a sequence of market events.
   */
  public void nullifySequenceNumber() {
    this.sequenceNumber = TypeConstants.INT64_NULL;
  }

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return Source Id
   */
  @SchemaType(
      encoding = "ALPHANUMERIC(10)",
      dataType = SchemaDataType.VARCHAR
  )
  @SchemaElement
  public long getSourceId() {
    return sourceId;
  }

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @param value - Source Id
   */
  public void setSourceId(long value) {
    this.sourceId = value;
  }

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   * @return true if Source Id is not null
   */
  public boolean hasSourceId() {
    return sourceId != TypeConstants.INT64_NULL;
  }

  /**
   * Identifies market data source. Different sessions of same connector
   * to a same data provider should have different id.
   */
  public void nullifySourceId() {
    this.sourceId = TypeConstants.INT64_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected MarketMessage createInstance() {
    return new MarketMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public MarketMessage nullify() {
    super.nullify();
    nullifyOriginalTimestamp();
    nullifyCurrency();
    nullifySequenceNumber();
    nullifySourceId();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public MarketMessage reset() {
    super.reset();
    originalTimestamp = TypeConstants.TIMESTAMP_UNKNOWN;
    currency = TypeConstants.INT64_NULL;
    sequenceNumber = TypeConstants.INT64_NULL;
    sourceId = TypeConstants.INT64_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public MarketMessage clone() {
    MarketMessage t = createInstance();
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
    if (!(obj instanceof MarketMessageInfo)) return false;
    MarketMessageInfo other =(MarketMessageInfo)obj;
    if (hasOriginalTimestamp() != other.hasOriginalTimestamp()) return false;
    if (hasOriginalTimestamp() && getOriginalTimestamp() != other.getOriginalTimestamp()) return false;
    if (hasCurrency() != other.hasCurrency()) return false;
    if (hasCurrency() && getCurrency() != other.getCurrency()) return false;
    if (hasSequenceNumber() != other.hasSequenceNumber()) return false;
    if (hasSequenceNumber() && getSequenceNumber() != other.getSequenceNumber()) return false;
    if (hasSourceId() != other.hasSourceId()) return false;
    if (hasSourceId() && getSourceId() != other.getSourceId()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasOriginalTimestamp()) {
      hash = hash * 31 + ((int)(getOriginalTimestamp() ^ (getOriginalTimestamp() >>> 32)));
    }
    if (hasCurrency()) {
      hash = hash * 31 + ((int)(getCurrency() ^ (getCurrency() >>> 32)));
    }
    if (hasSequenceNumber()) {
      hash = hash * 31 + ((int)(getSequenceNumber() ^ (getSequenceNumber() >>> 32)));
    }
    if (hasSourceId()) {
      hash = hash * 31 + ((int)(getSourceId() ^ (getSourceId() >>> 32)));
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public MarketMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof MarketMessageInfo) {
      MarketMessageInfo t = (MarketMessageInfo)template;
      if (t.hasOriginalTimestamp()) {
        setOriginalTimestamp(t.getOriginalTimestamp());
      } else {
        nullifyOriginalTimestamp();
      }
      if (t.hasCurrency()) {
        setCurrency(t.getCurrency());
      } else {
        nullifyCurrency();
      }
      if (t.hasSequenceNumber()) {
        setSequenceNumber(t.getSequenceNumber());
      } else {
        nullifySequenceNumber();
      }
      if (t.hasSourceId()) {
        setSourceId(t.getSourceId());
      } else {
        nullifySourceId();
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
    str.append("{ \"$type\":  \"MarketMessage\"");
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
