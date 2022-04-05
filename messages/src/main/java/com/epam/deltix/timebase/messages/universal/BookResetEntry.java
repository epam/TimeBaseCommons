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

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * This entry used for empty snapshot indication. It is necessary because PackageHeader has no field exchangeId.
 * You should use this entry only for empty snapshot (both sides are empty) case.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.BookResetEntry",
    title = "Book Reset Entry"
)
public class BookResetEntry extends BaseEntry implements BookResetEntryInterface {
  public static final String CLASS_NAME = BookResetEntry.class.getName();

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  protected QuoteSide side = null;

  /**
   * Data Model Type to identify what book we should reset.
   */
  protected DataModelType modelType = null;

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
  @Deprecated
  @SchemaType(
      isNullable = true
  )
  @SchemaElement(
      title = "Side"
  )
  public QuoteSide getSide() {
    return side;
  }

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @param value - Side
   */
  public void setSide(QuoteSide value) {
    this.side = value;
  }

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return true if Side is not null
   */
  public boolean hasSide() {
    return side != null;
  }

  /**
   * Deprecated as of November 2019. No longer used. BookResetEntry now applies to both sides of Order Book and must be used only in snapshot package types.
   * Previous meaning: Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  public void nullifySide() {
    this.side = null;
  }

  /**
   * Data Model Type to identify what book we should reset.
   * @return Model Type
   */
  @SchemaType(
      isNullable = false
  )
  @SchemaElement(
      title = "Model Type"
  )
  public DataModelType getModelType() {
    return modelType;
  }

  /**
   * Data Model Type to identify what book we should reset.
   * @param value - Model Type
   */
  public void setModelType(DataModelType value) {
    this.modelType = value;
  }

  /**
   * Data Model Type to identify what book we should reset.
   * @return true if Model Type is not null
   */
  public boolean hasModelType() {
    return modelType != null;
  }

  /**
   * Data Model Type to identify what book we should reset.
   */
  public void nullifyModelType() {
    this.modelType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected BookResetEntry createInstance() {
    return new BookResetEntry();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public BookResetEntry nullify() {
    super.nullify();
    nullifySide();
    nullifyModelType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public BookResetEntry reset() {
    super.reset();
    side = null;
    modelType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public BookResetEntry clone() {
    BookResetEntry t = createInstance();
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
    if (!(obj instanceof BookResetEntryInfo)) return false;
    BookResetEntryInfo other =(BookResetEntryInfo)obj;
    if (hasSide() != other.hasSide()) return false;
    if (hasSide() && getSide() != other.getSide()) return false;
    if (hasModelType() != other.hasModelType()) return false;
    if (hasModelType() && getModelType() != other.getModelType()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasSide()) {
      hash = hash * 31 + getSide().getNumber();
    }
    if (hasModelType()) {
      hash = hash * 31 + getModelType().getNumber();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public BookResetEntry copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof BookResetEntryInfo) {
      BookResetEntryInfo t = (BookResetEntryInfo)template;
      if (t.hasSide()) {
        setSide(t.getSide());
      } else {
        nullifySide();
      }
      if (t.hasModelType()) {
        setModelType(t.getModelType());
      } else {
        nullifyModelType();
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
    str.append("{ \"$type\":  \"BookResetEntry\"");
    if (hasSide()) {
      str.append(", \"side\": \"").append(getSide()).append("\"");
    }
    if (hasModelType()) {
      str.append(", \"modelType\": \"").append(getModelType()).append("\"");
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
