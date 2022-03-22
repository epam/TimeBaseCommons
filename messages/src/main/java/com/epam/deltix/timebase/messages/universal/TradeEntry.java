package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;
import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.timebase.messages.*;

/**
 * Basic information about a market trade.
 */
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.universal.TradeEntry",
    title = "Trade Entry"
)
public class TradeEntry extends BaseEntry implements TradeEntryInterface {
  public static final String CLASS_NAME = TradeEntry.class.getName();

  /**
   * Ask, Bid or Trade price.
   */
  protected long price = TypeConstants.DECIMAL_NULL;

  /**
   * Ask, Bid or Trade quantity.
   */
  protected long size = TypeConstants.DECIMAL_NULL;

  /**
   * Market specific trade condition.
   */
  protected CharSequence condition = null;

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   */
  protected TradeType tradeType = null;

  /**
   * Seller number of orders involved in match.
   */
  protected long sellerNumberOfOrders = TypeConstants.INT64_NULL;

  /**
   * Buyer number of orders involved in match.
   */
  protected long buyerNumberOfOrders = TypeConstants.INT64_NULL;

  /**
   * ID of seller order.
   */
  protected CharSequence sellerOrderId = null;

  /**
   * ID of buyer order.
   */
  protected CharSequence buyerOrderId = null;

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   */
  protected CharSequence sellerParticipantId = null;

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   */
  protected CharSequence buyerParticipantId = null;

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   */
  protected AggressorSide side = null;

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   */
  protected CharSequence matchId = null;

  /**
   * Ask, Bid or Trade price.
   * @return Price
   */
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
  public void setPrice(long value) {
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
   * Market specific trade condition.
   * @return Condition
   */
  @SchemaElement(
      title = "Condition"
  )
  @SchemaType(
      encoding = "UTF8"
  )
  public CharSequence getCondition() {
    return condition;
  }

  /**
   * Market specific trade condition.
   * @param value - Condition
   */
  public void setCondition(CharSequence value) {
    this.condition = value;
  }

  /**
   * Market specific trade condition.
   * @return true if Condition is not null
   */
  public boolean hasCondition() {
    return condition != null;
  }

  /**
   * Market specific trade condition.
   */
  public void nullifyCondition() {
    this.condition = null;
  }

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @return Trade Type
   */
  @SchemaElement(
      title = "Trade Type"
  )
  public TradeType getTradeType() {
    return tradeType;
  }

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @param value - Trade Type
   */
  public void setTradeType(TradeType value) {
    this.tradeType = value;
  }

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   * @return true if Trade Type is not null
   */
  public boolean hasTradeType() {
    return tradeType != null;
  }

  /**
   * Explains the meaning of the given price and/or size.
   * The value is null for regular trades.
   */
  public void nullifyTradeType() {
    this.tradeType = null;
  }

  /**
   * Seller number of orders involved in match.
   * @return Seller Number Of Orders
   */
  @SchemaElement(
      title = "Seller Number Of Orders"
  )
  public long getSellerNumberOfOrders() {
    return sellerNumberOfOrders;
  }

  /**
   * Seller number of orders involved in match.
   * @param value - Seller Number Of Orders
   */
  public void setSellerNumberOfOrders(long value) {
    this.sellerNumberOfOrders = value;
  }

  /**
   * Seller number of orders involved in match.
   * @return true if Seller Number Of Orders is not null
   */
  public boolean hasSellerNumberOfOrders() {
    return sellerNumberOfOrders != TypeConstants.INT64_NULL;
  }

  /**
   * Seller number of orders involved in match.
   */
  public void nullifySellerNumberOfOrders() {
    this.sellerNumberOfOrders = TypeConstants.INT64_NULL;
  }

  /**
   * Buyer number of orders involved in match.
   * @return Buyer Number Of Orders
   */
  @SchemaElement(
      title = "Buyer Number Of Orders"
  )
  public long getBuyerNumberOfOrders() {
    return buyerNumberOfOrders;
  }

  /**
   * Buyer number of orders involved in match.
   * @param value - Buyer Number Of Orders
   */
  public void setBuyerNumberOfOrders(long value) {
    this.buyerNumberOfOrders = value;
  }

  /**
   * Buyer number of orders involved in match.
   * @return true if Buyer Number Of Orders is not null
   */
  public boolean hasBuyerNumberOfOrders() {
    return buyerNumberOfOrders != TypeConstants.INT64_NULL;
  }

  /**
   * Buyer number of orders involved in match.
   */
  public void nullifyBuyerNumberOfOrders() {
    this.buyerNumberOfOrders = TypeConstants.INT64_NULL;
  }

  /**
   * ID of seller order.
   * @return Seller Order ID
   */
  @Identifier
  @SchemaElement(
      title = "Seller Order ID"
  )
  public CharSequence getSellerOrderId() {
    return sellerOrderId;
  }

  /**
   * ID of seller order.
   * @param value - Seller Order ID
   */
  public void setSellerOrderId(CharSequence value) {
    this.sellerOrderId = value;
  }

  /**
   * ID of seller order.
   * @return true if Seller Order ID is not null
   */
  public boolean hasSellerOrderId() {
    return sellerOrderId != null;
  }

  /**
   * ID of seller order.
   */
  public void nullifySellerOrderId() {
    this.sellerOrderId = null;
  }

  /**
   * ID of buyer order.
   * @return Buyer Order ID
   */
  @Identifier
  @SchemaElement(
      title = "Buyer Order ID"
  )
  public CharSequence getBuyerOrderId() {
    return buyerOrderId;
  }

  /**
   * ID of buyer order.
   * @param value - Buyer Order ID
   */
  public void setBuyerOrderId(CharSequence value) {
    this.buyerOrderId = value;
  }

  /**
   * ID of buyer order.
   * @return true if Buyer Order ID is not null
   */
  public boolean hasBuyerOrderId() {
    return buyerOrderId != null;
  }

  /**
   * ID of buyer order.
   */
  public void nullifyBuyerOrderId() {
    this.buyerOrderId = null;
  }

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @return Seller Participant ID
   */
  @Identifier
  @SchemaElement(
      title = "Seller Participant ID"
  )
  public CharSequence getSellerParticipantId() {
    return sellerParticipantId;
  }

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @param value - Seller Participant ID
   */
  public void setSellerParticipantId(CharSequence value) {
    this.sellerParticipantId = value;
  }

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   * @return true if Seller Participant ID is not null
   */
  public boolean hasSellerParticipantId() {
    return sellerParticipantId != null;
  }

  /**
   * Seller participant ID (or broker ID) for trader that submit selling order.
   */
  public void nullifySellerParticipantId() {
    this.sellerParticipantId = null;
  }

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @return Buyer Participant ID
   */
  @Identifier
  @SchemaElement(
      title = "Buyer Participant ID"
  )
  public CharSequence getBuyerParticipantId() {
    return buyerParticipantId;
  }

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @param value - Buyer Participant ID
   */
  public void setBuyerParticipantId(CharSequence value) {
    this.buyerParticipantId = value;
  }

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   * @return true if Buyer Participant ID is not null
   */
  public boolean hasBuyerParticipantId() {
    return buyerParticipantId != null;
  }

  /**
   * Buyer participant ID (or broker ID) for trader that submit buying order.
   */
  public void nullifyBuyerParticipantId() {
    this.buyerParticipantId = null;
  }

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @return Side
   */
  @SchemaElement(
      title = "Side"
  )
  public AggressorSide getSide() {
    return side;
  }

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @param value - Side
   */
  public void setSide(AggressorSide value) {
    this.side = value;
  }

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   * @return true if Side is not null
   */
  public boolean hasSide() {
    return side != null;
  }

  /**
   * Trade side. Sell or Buy.
   * For Trade it's aggressor side, i.e. side from where market order has came.
   */
  public void nullifySide() {
    this.side = null;
  }

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @return Match ID
   */
  @Identifier
  @SchemaElement(
      title = "Match ID"
  )
  public CharSequence getMatchId() {
    return matchId;
  }

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @param value - Match ID
   */
  public void setMatchId(CharSequence value) {
    this.matchId = value;
  }

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   * @return true if Match ID is not null
   */
  public boolean hasMatchId() {
    return matchId != null;
  }

  /**
   * Id of particular execution event (ExecutionId, TradeId, MatchId)
   */
  public void nullifyMatchId() {
    this.matchId = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected TradeEntry createInstance() {
    return new TradeEntry();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public TradeEntry nullify() {
    super.nullify();
    nullifyPrice();
    nullifySize();
    nullifyCondition();
    nullifyTradeType();
    nullifySellerNumberOfOrders();
    nullifyBuyerNumberOfOrders();
    nullifySellerOrderId();
    nullifyBuyerOrderId();
    nullifySellerParticipantId();
    nullifyBuyerParticipantId();
    nullifySide();
    nullifyMatchId();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public TradeEntry reset() {
    super.reset();
    price = TypeConstants.DECIMAL_NULL;
    size = TypeConstants.DECIMAL_NULL;
    condition = null;
    tradeType = null;
    sellerNumberOfOrders = TypeConstants.INT64_NULL;
    buyerNumberOfOrders = TypeConstants.INT64_NULL;
    sellerOrderId = null;
    buyerOrderId = null;
    sellerParticipantId = null;
    buyerParticipantId = null;
    side = null;
    matchId = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public TradeEntry clone() {
    TradeEntry t = createInstance();
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
    if (!(obj instanceof TradeEntryInfo)) return false;
    TradeEntryInfo other =(TradeEntryInfo)obj;
    if (hasPrice() != other.hasPrice()) return false;
    if (hasPrice() && !Decimal64Utils.equals(getPrice(), other.getPrice())) return false;
    if (hasSize() != other.hasSize()) return false;
    if (hasSize() && !Decimal64Utils.equals(getSize(), other.getSize())) return false;
    if (hasCondition() != other.hasCondition()) return false;
    if (hasCondition()) {
      if (getCondition().length() != other.getCondition().length()) return false; else {
        CharSequence s1 = getCondition();
        CharSequence s2 = other.getCondition();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasTradeType() != other.hasTradeType()) return false;
    if (hasTradeType() && getTradeType() != other.getTradeType()) return false;
    if (hasSellerNumberOfOrders() != other.hasSellerNumberOfOrders()) return false;
    if (hasSellerNumberOfOrders() && getSellerNumberOfOrders() != other.getSellerNumberOfOrders()) return false;
    if (hasBuyerNumberOfOrders() != other.hasBuyerNumberOfOrders()) return false;
    if (hasBuyerNumberOfOrders() && getBuyerNumberOfOrders() != other.getBuyerNumberOfOrders()) return false;
    if (hasSellerOrderId() != other.hasSellerOrderId()) return false;
    if (hasSellerOrderId()) {
      if (getSellerOrderId().length() != other.getSellerOrderId().length()) return false; else {
        CharSequence s1 = getSellerOrderId();
        CharSequence s2 = other.getSellerOrderId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasBuyerOrderId() != other.hasBuyerOrderId()) return false;
    if (hasBuyerOrderId()) {
      if (getBuyerOrderId().length() != other.getBuyerOrderId().length()) return false; else {
        CharSequence s1 = getBuyerOrderId();
        CharSequence s2 = other.getBuyerOrderId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasSellerParticipantId() != other.hasSellerParticipantId()) return false;
    if (hasSellerParticipantId()) {
      if (getSellerParticipantId().length() != other.getSellerParticipantId().length()) return false; else {
        CharSequence s1 = getSellerParticipantId();
        CharSequence s2 = other.getSellerParticipantId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasBuyerParticipantId() != other.hasBuyerParticipantId()) return false;
    if (hasBuyerParticipantId()) {
      if (getBuyerParticipantId().length() != other.getBuyerParticipantId().length()) return false; else {
        CharSequence s1 = getBuyerParticipantId();
        CharSequence s2 = other.getBuyerParticipantId();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasSide() != other.hasSide()) return false;
    if (hasSide() && getSide() != other.getSide()) return false;
    if (hasMatchId() != other.hasMatchId()) return false;
    if (hasMatchId()) {
      if (getMatchId().length() != other.getMatchId().length()) return false; else {
        CharSequence s1 = getMatchId();
        CharSequence s2 = other.getMatchId();
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
    if (hasCondition()) {
      hash = hash * 31 + getCondition().hashCode();
    }
    if (hasTradeType()) {
      hash = hash * 31 + getTradeType().getNumber();
    }
    if (hasSellerNumberOfOrders()) {
      hash = hash * 31 + ((int)(getSellerNumberOfOrders() ^ (getSellerNumberOfOrders() >>> 32)));
    }
    if (hasBuyerNumberOfOrders()) {
      hash = hash * 31 + ((int)(getBuyerNumberOfOrders() ^ (getBuyerNumberOfOrders() >>> 32)));
    }
    if (hasSellerOrderId()) {
      hash = hash * 31 + getSellerOrderId().hashCode();
    }
    if (hasBuyerOrderId()) {
      hash = hash * 31 + getBuyerOrderId().hashCode();
    }
    if (hasSellerParticipantId()) {
      hash = hash * 31 + getSellerParticipantId().hashCode();
    }
    if (hasBuyerParticipantId()) {
      hash = hash * 31 + getBuyerParticipantId().hashCode();
    }
    if (hasSide()) {
      hash = hash * 31 + getSide().getNumber();
    }
    if (hasMatchId()) {
      hash = hash * 31 + getMatchId().hashCode();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public TradeEntry copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof TradeEntryInfo) {
      TradeEntryInfo t = (TradeEntryInfo)template;
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
      if (t.hasCondition()) {
        if (hasCondition() && getCondition() instanceof StringBuilder) {
          ((StringBuilder)getCondition()).setLength(0);
        } else {
          setCondition(new StringBuilder());
        }
        ((StringBuilder)getCondition()).append(t.getCondition());
      } else {
        nullifyCondition();
      }
      if (t.hasTradeType()) {
        setTradeType(t.getTradeType());
      } else {
        nullifyTradeType();
      }
      if (t.hasSellerNumberOfOrders()) {
        setSellerNumberOfOrders(t.getSellerNumberOfOrders());
      } else {
        nullifySellerNumberOfOrders();
      }
      if (t.hasBuyerNumberOfOrders()) {
        setBuyerNumberOfOrders(t.getBuyerNumberOfOrders());
      } else {
        nullifyBuyerNumberOfOrders();
      }
      if (t.hasSellerOrderId()) {
        if (!(hasSellerOrderId() && getSellerOrderId() instanceof BinaryAsciiString)) {
          setSellerOrderId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getSellerOrderId()).assign(t.getSellerOrderId());
      } else {
        nullifySellerOrderId();
      }
      if (t.hasBuyerOrderId()) {
        if (!(hasBuyerOrderId() && getBuyerOrderId() instanceof BinaryAsciiString)) {
          setBuyerOrderId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getBuyerOrderId()).assign(t.getBuyerOrderId());
      } else {
        nullifyBuyerOrderId();
      }
      if (t.hasSellerParticipantId()) {
        if (!(hasSellerParticipantId() && getSellerParticipantId() instanceof BinaryAsciiString)) {
          setSellerParticipantId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getSellerParticipantId()).assign(t.getSellerParticipantId());
      } else {
        nullifySellerParticipantId();
      }
      if (t.hasBuyerParticipantId()) {
        if (!(hasBuyerParticipantId() && getBuyerParticipantId() instanceof BinaryAsciiString)) {
          setBuyerParticipantId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getBuyerParticipantId()).assign(t.getBuyerParticipantId());
      } else {
        nullifyBuyerParticipantId();
      }
      if (t.hasSide()) {
        setSide(t.getSide());
      } else {
        nullifySide();
      }
      if (t.hasMatchId()) {
        if (!(hasMatchId() && getMatchId() instanceof BinaryAsciiString)) {
          setMatchId(new BinaryAsciiString());
        }
        ((BinaryAsciiString)getMatchId()).assign(t.getMatchId());
      } else {
        nullifyMatchId();
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
    str.append("{ \"$type\":  \"TradeEntry\"");
    if (hasPrice()) {
      str.append(", \"price\": ");
      Decimal64Utils.appendTo(getPrice(), str);
    }
    if (hasSize()) {
      str.append(", \"size\": ");
      Decimal64Utils.appendTo(getSize(), str);
    }
    if (hasCondition()) {
      str.append(", \"condition\": \"").append(getCondition()).append("\"");
    }
    if (hasTradeType()) {
      str.append(", \"tradeType\": \"").append(getTradeType()).append("\"");
    }
    if (hasSellerNumberOfOrders()) {
      str.append(", \"sellerNumberOfOrders\": ").append(getSellerNumberOfOrders());
    }
    if (hasBuyerNumberOfOrders()) {
      str.append(", \"buyerNumberOfOrders\": ").append(getBuyerNumberOfOrders());
    }
    if (hasSellerOrderId()) {
      str.append(", \"sellerOrderId\": \"").append(getSellerOrderId()).append("\"");
    }
    if (hasBuyerOrderId()) {
      str.append(", \"buyerOrderId\": \"").append(getBuyerOrderId()).append("\"");
    }
    if (hasSellerParticipantId()) {
      str.append(", \"sellerParticipantId\": \"").append(getSellerParticipantId()).append("\"");
    }
    if (hasBuyerParticipantId()) {
      str.append(", \"buyerParticipantId\": \"").append(getBuyerParticipantId()).append("\"");
    }
    if (hasSide()) {
      str.append(", \"side\": \"").append(getSide()).append("\"");
    }
    if (hasMatchId()) {
      str.append(", \"matchId\": \"").append(getMatchId()).append("\"");
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
