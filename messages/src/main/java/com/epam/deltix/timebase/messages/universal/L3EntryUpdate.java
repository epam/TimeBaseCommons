package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaType;

/**
 * Reports incremental L3-updates: new, cancel, modify and replace of one quote in Order Book either on ask or bid side.
 * It also can encode L3-snapshot entry. Note L3 is quote oriented depth-of-the-book format and should be used
 * whenever quoteId is used to locate book changes.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.L3EntryUpdate",
    title = "L3 Entry Update"
)
public class L3EntryUpdate extends BasePriceEntry implements L3EntryUpdateInterface {
  public static final String CLASS_NAME = L3EntryUpdate.class.getName();

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   */
  protected QuoteUpdateAction action = null;

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   */
  protected QuoteSide side = null;

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @return Action
   */
  @SchemaType(
      isNullable = false
  )
  @SchemaElement(
      title = "Action"
  )
  public QuoteUpdateAction getAction() {
    return action;
  }

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @param value - Action
   */
  public void setAction(QuoteUpdateAction value) {
    this.action = value;
  }

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   * @return true if Action is not null
   */
  public boolean hasAction() {
    return action != null;
  }

  /**
   * Directs how to update an Order Book
   * <p>
   * <b>symbol, instrumentType, exchangeCode, quoteId</b> fields constitute the composite key
   * to identify the order book record.
   * </p>.
   */
  public void nullifyAction() {
    this.action = null;
  }

  /**
   * Quote side. Bid or Ask.
   * Ask = Sell limit order.
   * Bid = Buy limit  order.
   * @return Side
   */
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
  protected L3EntryUpdate createInstance() {
    return new L3EntryUpdate();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public L3EntryUpdate nullify() {
    super.nullify();
    nullifyAction();
    nullifySide();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public L3EntryUpdate reset() {
    super.reset();
    action = null;
    side = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public L3EntryUpdate clone() {
    L3EntryUpdate t = createInstance();
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
    if (!(obj instanceof L3EntryUpdateInfo)) return false;
    L3EntryUpdateInfo other =(L3EntryUpdateInfo)obj;
    if (hasAction() != other.hasAction()) return false;
    if (hasAction() && getAction() != other.getAction()) return false;
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
    if (hasAction()) {
      hash = hash * 31 + getAction().getNumber();
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
  public L3EntryUpdate copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof L3EntryUpdateInfo) {
      L3EntryUpdateInfo t = (L3EntryUpdateInfo)template;
      if (t.hasAction()) {
        setAction(t.getAction());
      } else {
        nullifyAction();
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
    str.append("{ \"$type\":  \"L3EntryUpdate\"");
    if (hasAction()) {
      str.append(", \"action\": \"").append(getAction()).append("\"");
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
