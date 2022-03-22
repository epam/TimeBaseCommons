package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.Identifier;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.L3EntryNew",
    title = "L3EntryNew"
)
public class L3EntryNew extends BasePriceEntry implements L3EntryNewInterface {
  public static final String CLASS_NAME = L3EntryNew.class.getName();

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  protected QuoteSide side = null;

  /**
   * Insert type. Add front or Add back.
   */
  protected InsertType insertType = null;

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   */
  protected CharSequence insertBeforeQuoteId = null;

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
   * Insert type. Add front or Add back.
   * @return Insert Type
   */
  @SchemaElement
  public InsertType getInsertType() {
    return insertType;
  }

  /**
   * Insert type. Add front or Add back.
   * @param value - Insert Type
   */
  public void setInsertType(InsertType value) {
    this.insertType = value;
  }

  /**
   * Insert type. Add front or Add back.
   * @return true if Insert Type is not null
   */
  public boolean hasInsertType() {
    return insertType != null;
  }

  /**
   * Insert type. Add front or Add back.
   */
  public void nullifyInsertType() {
    this.insertType = null;
  }

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @return Insert Before Quote Id
   */
  @Identifier
  @SchemaType(
      isNullable = true
  )
  @SchemaElement
  public CharSequence getInsertBeforeQuoteId() {
    return insertBeforeQuoteId;
  }

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @param value - Insert Before Quote Id
   */
  public void setInsertBeforeQuoteId(CharSequence value) {
    this.insertBeforeQuoteId = value;
  }

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   * @return true if Insert Before Quote Id is not null
   */
  public boolean hasInsertBeforeQuoteId() {
    return insertBeforeQuoteId != null;
  }

  /**
   * In case of InsertType = ADD_BEFORE represents the id of the quote that should be after inserted.
   */
  public void nullifyInsertBeforeQuoteId() {
    this.insertBeforeQuoteId = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected L3EntryNew createInstance() {
    return new L3EntryNew();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public L3EntryNew nullify() {
    super.nullify();
    nullifySide();
    nullifyInsertType();
    nullifyInsertBeforeQuoteId();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public L3EntryNew reset() {
    super.reset();
    side = null;
    insertType = null;
    insertBeforeQuoteId = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public L3EntryNew clone() {
    L3EntryNew t = createInstance();
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
    if (!(obj instanceof L3EntryNewInfo)) return false;
    L3EntryNewInfo other =(L3EntryNewInfo)obj;
    if (hasSide() != other.hasSide()) return false;
    if (hasSide() && getSide() != other.getSide()) return false;
    if (hasInsertType() != other.hasInsertType()) return false;
    if (hasInsertType() && getInsertType() != other.getInsertType()) return false;
    if (hasInsertBeforeQuoteId() != other.hasInsertBeforeQuoteId()) return false;
    if (hasInsertBeforeQuoteId()) {
      if (getInsertBeforeQuoteId().length() != other.getInsertBeforeQuoteId().length()) return false; else {
        CharSequence s1 = getInsertBeforeQuoteId();
        CharSequence s2 = other.getInsertBeforeQuoteId();
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
    if (hasSide()) {
      hash = hash * 31 + getSide().getNumber();
    }
    if (hasInsertType()) {
      hash = hash * 31 + getInsertType().getNumber();
    }
    if (hasInsertBeforeQuoteId()) {
      hash = hash * 31 + getInsertBeforeQuoteId().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public L3EntryNew copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof L3EntryNewInfo) {
      L3EntryNewInfo t = (L3EntryNewInfo)template;
      if (t.hasSide()) {
        setSide(t.getSide());
      } else {
        nullifySide();
      }
      if (t.hasInsertType()) {
        setInsertType(t.getInsertType());
      } else {
        nullifyInsertType();
      }
      if (t.hasInsertBeforeQuoteId()) {
        if (!(hasInsertBeforeQuoteId() && getInsertBeforeQuoteId() instanceof BinaryAsciiString)) {
          setInsertBeforeQuoteId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getInsertBeforeQuoteId()).assign(t.getInsertBeforeQuoteId());
      } else {
        nullifyInsertBeforeQuoteId();
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
    str.append("{ \"$type\":  \"L3EntryNew\"");
    if (hasSide()) {
      str.append(", \"side\": \"").append(getSide()).append("\"");
    }
    if (hasInsertType()) {
      str.append(", \"insertType\": \"").append(getInsertType()).append("\"");
    }
    if (hasInsertBeforeQuoteId()) {
      str.append(", \"insertBeforeQuoteId\": \"").append(getInsertBeforeQuoteId()).append("\"");
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
