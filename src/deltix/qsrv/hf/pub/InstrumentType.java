package deltix.qsrv.hf.pub;



/**
 *  Type of financial instrument
 */
public enum InstrumentType {
    EQUITY('S', "Stock"),
    OPTION('O', "Option"),
    FUTURE('F', "Futures"),
    BOND('B', "Bond"),
    FX('X', "Currency"),
    INDEX('I', "Index"),
    ETF('E', "ETF"),
    CUSTOM('C', "Custom"),
    SIMPLE_OPTION('P', "SimpleOption"),
    
    EXCHANGE('G', "Exchange"),
    TRADING_SESSION('T', "TradingSession"),
    STREAM('M', "Stream"),
    DATA_CONNECTOR('Q', "DataConnector"),
    SPREAD('Z', "Spread"),

    SYSTEM('X', "System");

    //NB: If you plan to extend this enum, please support isTradable() method below!

    private final char             code;
    private final String           qoType;
    
    private InstrumentType (int code, String qoType) { this.code = (char)code; this.qoType = qoType; }

    //Used by UHF CharEnumCodes
    public char toChar () { return code; }
         
    public static InstrumentType fromChar (char code) {
        for (InstrumentType itv : values ())
            if (code == itv.code)
                return (itv);
            	
        throw new IllegalArgumentException ("Unknown InstrumentType code: " + code);
    }

    public String               toQOTypeString () {
        return qoType;
    }

    public static InstrumentType fromQOTypeString (String qoType) {
        for (InstrumentType instrumentType : values()) {
            if (instrumentType.qoType.equalsIgnoreCase(qoType))
                return instrumentType;
        }
        
        throw new IllegalArgumentException("Unknown InstrumentType qoType: " + qoType);
    }

    /** @return true if instrument of this type can accumulate positions and appear in trade orders */
    public boolean isTradable() {
        int ordinal = ordinal();
        return (ordinal <= SIMPLE_OPTION.ordinal() || ordinal == SPREAD.ordinal());
    }
    
}
