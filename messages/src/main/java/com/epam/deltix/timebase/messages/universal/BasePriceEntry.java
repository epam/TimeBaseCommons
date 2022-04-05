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
package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.dfp.Decimal;
import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.*;

/**
 * This is base class for price entry.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.BasePriceEntry",
    title = "Base Price Entry"
)
public class BasePriceEntry extends BaseEntry implements BasePriceEntryInterface {
  public static final String CLASS_NAME = BasePriceEntry.class.getName();

  /**
   * Ask, Bid or Trade price.
   */
  @Decimal
  protected long price = TypeConstants.DECIMAL_NULL;

  /**
   * Ask, Bid or Trade quantity.
   */
  protected long size = TypeConstants.DECIMAL_NULL;

  /**
   * Numbers of orders.
   */
  protected long numberOfOrders = TypeConstants.INT64_NULL;

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   */
  protected CharSequence quoteId = null;

  /**
   * Id of participant (or broker ID).
   */
  protected CharSequence participantId = null;

  /**
   * Ask, Bid or Trade price.
   * @return Price
   */
  @Decimal
  @SchemaElement(
      title = "Price"
  )
  @SchemaType(
      encoding = "DECIMAL64",
      dataType = SchemaDataType.FLOAT
  )
  public long getPrice() {
    return price;
  }

  /**
   * Ask, Bid or Trade price.
   * @param value - Price
   */
  public void setPrice(@Decimal long value) {
    this.price = value;
  }

  /**
   * Ask, Bid or Trade price.
   * @return true if Price is not null
   */
  public boolean hasPrice() {
    return price != TypeConstants.DECIMAL_NULL;
  }

  /**
   * Ask, Bid or Trade price.
   */
  public void nullifyPrice() {
    this.price = TypeConstants.DECIMAL_NULL;
  }

  /**
   * Ask, Bid or Trade quantity.
   * @return Size
   */
  @SchemaElement(
      title = "Size"
  )
  @SchemaType(
      encoding = "DECIMAL64",
      dataType = SchemaDataType.FLOAT
  )
  public long getSize() {
    return size;
  }

  /**
   * Ask, Bid or Trade quantity.
   * @param value - Size
   */
  public void setSize(long value) {
    this.size = value;
  }

  /**
   * Ask, Bid or Trade quantity.
   * @return true if Size is not null
   */
  public boolean hasSize() {
    return size != TypeConstants.DECIMAL_NULL;
  }

  /**
   * Ask, Bid or Trade quantity.
   */
  public void nullifySize() {
    this.size = TypeConstants.DECIMAL_NULL;
  }

  /**
   * Numbers of orders.
   * @return Number Of Orders
   */
  @SchemaElement(
      title = "Number Of Orders"
  )
  public long getNumberOfOrders() {
    return numberOfOrders;
  }

  /**
   * Numbers of orders.
   * @param value - Number Of Orders
   */
  public void setNumberOfOrders(long value) {
    this.numberOfOrders = value;
  }

  /**
   * Numbers of orders.
   * @return true if Number Of Orders is not null
   */
  public boolean hasNumberOfOrders() {
    return numberOfOrders != TypeConstants.INT64_NULL;
  }

  /**
   * Numbers of orders.
   */
  public void nullifyNumberOfOrders() {
    this.numberOfOrders = TypeConstants.INT64_NULL;
  }

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @return Quote ID
   */
  @Identifier
  @SchemaElement(
      title = "Quote ID"
  )
  public CharSequence getQuoteId() {
    return quoteId;
  }

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @param value - Quote ID
   */
  public void setQuoteId(CharSequence value) {
    this.quoteId = value;
  }

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   * @return true if Quote ID is not null
   */
  public boolean hasQuoteId() {
    return quoteId != null;
  }

  /**
   * Quote ID. In Forex market, for example, quote ID can be referenced in
   * TradeOrders (to identify market maker's quote/rate we want to deal with).
   * Each market maker usually keeps this ID unique per session per day. This
   * is a alpha-numeric text text field that can reach 64 characters or more,
   * depending on market maker.
   */
  public void nullifyQuoteId() {
    this.quoteId = null;
  }

  /**
   * Id of participant (or broker ID).
   * @return Participant
   */
  @Identifier
  @SchemaElement(
      title = "Participant"
  )
  public CharSequence getParticipantId() {
    return participantId;
  }

  /**
   * Id of participant (or broker ID).
   * @param value - Participant
   */
  public void setParticipantId(CharSequence value) {
    this.participantId = value;
  }

  /**
   * Id of participant (or broker ID).
   * @return true if Participant is not null
   */
  public boolean hasParticipantId() {
    return participantId != null;
  }

  /**
   * Id of participant (or broker ID).
   */
  public void nullifyParticipantId() {
    this.participantId = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected BasePriceEntry createInstance() {
    return new BasePriceEntry();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public BasePriceEntry nullify() {
    super.nullify();
    nullifyPrice();
    nullifySize();
    nullifyNumberOfOrders();
    nullifyQuoteId();
    nullifyParticipantId();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public BasePriceEntry reset() {
    super.reset();
    price = TypeConstants.DECIMAL_NULL;
    size = TypeConstants.DECIMAL_NULL;
    numberOfOrders = TypeConstants.INT64_NULL;
    quoteId = null;
    participantId = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public BasePriceEntry clone() {
    BasePriceEntry t = createInstance();
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
    if (!(obj instanceof BasePriceEntryInfo)) return false;
    BasePriceEntryInfo other =(BasePriceEntryInfo)obj;
    if (hasPrice() != other.hasPrice()) return false;
    if (hasPrice() && !Decimal64Utils.equals(getPrice(), other.getPrice())) return false;
    if (hasSize() != other.hasSize()) return false;
    if (hasSize() && !Decimal64Utils.equals(getSize(), other.getSize())) return false;
    if (hasNumberOfOrders() != other.hasNumberOfOrders()) return false;
    if (hasNumberOfOrders() && getNumberOfOrders() != other.getNumberOfOrders()) return false;
    if (hasQuoteId() != other.hasQuoteId()) return false;
    if (hasQuoteId()) {
      if (getQuoteId().length() != other.getQuoteId().length()) return false; else {
        CharSequence s1 = getQuoteId();
        CharSequence s2 = other.getQuoteId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasParticipantId() != other.hasParticipantId()) return false;
    if (hasParticipantId()) {
      if (getParticipantId().length() != other.getParticipantId().length()) return false; else {
        CharSequence s1 = getParticipantId();
        CharSequence s2 = other.getParticipantId();
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
    if (hasPrice()) {
      hash = hash * 31 + ((int)(getPrice() ^ (getPrice() >>> 32)));
    }
    if (hasSize()) {
      hash = hash * 31 + ((int)(getSize() ^ (getSize() >>> 32)));
    }
    if (hasNumberOfOrders()) {
      hash = hash * 31 + ((int)(getNumberOfOrders() ^ (getNumberOfOrders() >>> 32)));
    }
    if (hasQuoteId()) {
      hash = hash * 31 + getQuoteId().hashCode();
    }
    if (hasParticipantId()) {
      hash = hash * 31 + getParticipantId().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public BasePriceEntry copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof BasePriceEntryInfo) {
      BasePriceEntryInfo t = (BasePriceEntryInfo)template;
      if (t.hasPrice()) {
        setPrice(t.getPrice());
      } else {
        nullifyPrice();
      }
      if (t.hasSize()) {
        setSize(t.getSize());
      } else {
        nullifySize();
      }
      if (t.hasNumberOfOrders()) {
        setNumberOfOrders(t.getNumberOfOrders());
      } else {
        nullifyNumberOfOrders();
      }
      if (t.hasQuoteId()) {
        if (!(hasQuoteId() && getQuoteId() instanceof BinaryAsciiString)) {
          setQuoteId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getQuoteId()).assign(t.getQuoteId());
      } else {
        nullifyQuoteId();
      }
      if (t.hasParticipantId()) {
        if (!(hasParticipantId() && getParticipantId() instanceof BinaryAsciiString)) {
          setParticipantId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getParticipantId()).assign(t.getParticipantId());
      } else {
        nullifyParticipantId();
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
    str.append("{ \"$type\":  \"BasePriceEntry\"");
    if (hasPrice()) {
      str.append(", \"price\": ");
      Decimal64Utils.appendTo(getPrice(), str);
    }
    if (hasSize()) {
      str.append(", \"size\": ");
      Decimal64Utils.appendTo(getSize(), str);
    }
    if (hasNumberOfOrders()) {
      str.append(", \"numberOfOrders\": ").append(getNumberOfOrders());
    }
    if (hasQuoteId()) {
      str.append(", \"quoteId\": \"").append(getQuoteId()).append("\"");
    }
    if (hasParticipantId()) {
      str.append(", \"participantId\": \"").append(getParticipantId()).append("\"");
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
