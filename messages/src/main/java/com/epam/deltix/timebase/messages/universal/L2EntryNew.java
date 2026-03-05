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

import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;
import com.epam.deltix.timebase.messages.TypeConstants;

/**
 * Reports incremental L2-updates: insert, delete or update of one line in Order Book either on ask or bid side.
 * It also can encode L2-snapshot entry. Note L2 is level oriented depth-of-the-book format and should be used
 * whenever price or integer index is used to locate book changes. It does support individual quotes book of
 * arbitrary depth. But if incremental changes key is a quoteId L3Entry should be used instead.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.L2EntryNew",
    title = "L2EntryNew"
)
public class L2EntryNew extends BasePriceEntry implements L2EntryNewInterface {
  public static final String CLASS_NAME = L2EntryNew.class.getName();

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   */
  protected short level = TypeConstants.INT16_NULL;

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  protected QuoteSide side = null;

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   * @return Level Index
   */
  @SchemaType(
      isNullable = false
  )
  @SchemaElement(
      title = "Level Index"
  )
  public short getLevel() {
    return level;
  }

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   * @param value - Level Index
   */
  public void setLevel(short value) {
    this.level = value;
  }

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   * @return true if Level Index is not null
   */
  public boolean hasLevel() {
    return level != TypeConstants.INT16_NULL;
  }

  /**
   * Market Depth  / Price Level.
   * This value is zero-based (top of the book will have depth=0).
   */
  public void nullifyLevel() {
    this.level = TypeConstants.INT16_NULL;
  }

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
  @SchemaType(
      isNullable = false
  )
  @SchemaElement(
      title = "Side"
  )
  public QuoteSide getSide() {
    return side;
  }

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @param value - Side
   */
  public void setSide(QuoteSide value) {
    this.side = value;
  }

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return true if Side is not null
   */
  public boolean hasSide() {
    return side != null;
  }

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  public void nullifySide() {
    this.side = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected L2EntryNew createInstance() {
    return new L2EntryNew();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public L2EntryNew nullify() {
    super.nullify();
    nullifyLevel();
    nullifySide();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public L2EntryNew reset() {
    super.reset();
    level = TypeConstants.INT16_NULL;
    side = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public L2EntryNew clone() {
    L2EntryNew t = createInstance();
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
    if (!(obj instanceof L2EntryNewInfo)) return false;
    L2EntryNewInfo other =(L2EntryNewInfo)obj;
    if (hasLevel() != other.hasLevel()) return false;
    if (hasLevel() && getLevel() != other.getLevel()) return false;
    if (hasSide() != other.hasSide()) return false;
    if (hasSide() && getSide() != other.getSide()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasLevel()) {
      hash = hash * 31 + ((int)getLevel());
    }
    if (hasSide()) {
      hash = hash * 31 + getSide().getNumber();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public L2EntryNew copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof L2EntryNewInfo) {
      L2EntryNewInfo t = (L2EntryNewInfo)template;
      if (t.hasLevel()) {
        setLevel(t.getLevel());
      } else {
        nullifyLevel();
      }
      if (t.hasSide()) {
        setSide(t.getSide());
      } else {
        nullifySide();
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
    str.append("{ \"$type\":  \"L2EntryNew\"");
    if (hasLevel()) {
      str.append(", \"level\": ").append(getLevel());
    }
    if (hasSide()) {
      str.append(", \"side\": \"").append(getSide()).append("\"");
    }
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
