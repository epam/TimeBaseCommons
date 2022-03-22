package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.*;

/**
 * This class may represent both exchange-local top of the book (BBO) as well as National Best Bid Offer (NBBO).
 * You can use method {getIsNational()} to filter out NBBO messages.
 * This is always a one side quote, unlike old BestBidOfferMessage which is two-side (with nullable properties).
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.L1Entry",
    title = "L1Entry"
)
public class L1Entry extends BasePriceEntry implements L1EntryInterface {
  public static final String CLASS_NAME = L1Entry.class.getName();

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   */
  protected byte isNational = TypeConstants.BOOLEAN_NULL;

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  protected QuoteSide side = null;

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @return Is National
   */
  @SchemaElement(
      title = "Is National"
  )
  @SchemaType(
      dataType = SchemaDataType.BOOLEAN
  )
  public boolean isNational() {
    return isNational == 1;
  }

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @param value - Is National
   */
  public void setIsNational(boolean value) {
    this.isNational = (byte)(value ? 1 : 0);
  }

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   * @return true if Is National is not null
   */
  public boolean hasIsNational() {
    return isNational != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * return <code>1</code> if this BBO quote represents the national best, <code>0</code> if this BBO is regional
   * and <code>BooleanDataType.NULL</code> if the property is undefined. In case of NBBO you can inspect {#getExchangeId()}
   * to see what exchange/ECN has the national best price.
   */
  public void nullifyIsNational() {
    this.isNational = TypeConstants.BOOLEAN_NULL;
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
  protected L1Entry createInstance() {
    return new L1Entry();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public L1Entry nullify() {
    super.nullify();
    nullifyIsNational();
    nullifySide();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public L1Entry reset() {
    super.reset();
    isNational = TypeConstants.BOOLEAN_NULL;
    side = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public L1Entry clone() {
    L1Entry t = createInstance();
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
    if (!(obj instanceof L1EntryInfo)) return false;
    L1EntryInfo other =(L1EntryInfo)obj;
    if (hasIsNational() != other.hasIsNational()) return false;
    if (hasIsNational() && isNational() != other.isNational()) return false;
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
    if (hasIsNational()) {
      hash = hash * 31 + (isNational() ? 1231 : 1237);
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
  public L1Entry copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof L1EntryInfo) {
      L1EntryInfo t = (L1EntryInfo)template;
      if (t.hasIsNational()) {
        setIsNational(t.isNational());
      } else {
        nullifyIsNational();
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
    str.append("{ \"$type\":  \"L1Entry\"");
    if (hasIsNational()) {
      str.append(", \"isNational\": ").append(isNational());
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
