package deltix.qsrv.hf.pub;


import deltix.util.collections.*;

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
    STREAM('M', "Stream");

    private final char             code;
    private final String           qoType;

    public static InstrumentType[] VALID_VALUES;
    static {
        VALID_VALUES = new InstrumentType[values ().length];
        System.arraycopy (values (),
                          0,
                          VALID_VALUES,
                          0,
                          values ().length);
        VALID_VALUES = CollectionUtil.remove (VALID_VALUES,
                                              SIMPLE_OPTION,
                                              InstrumentType.class);
    }
    
    private InstrumentType (int code, String qoType) { this.code = (char)code; this.qoType = qoType; }
         
    public char toChar () { return code; }  
         
    public static InstrumentType fromChar (char code) {
    	if (code == EQUITY.code) return EQUITY;
    	if (code == OPTION.code) return OPTION;
    	if (code == SIMPLE_OPTION.code) return SIMPLE_OPTION;
    	if (code == FUTURE.code) return FUTURE;
    	if (code == BOND.code)   return BOND;
    	if (code == FX.code)     return FX;
    	if (code == INDEX.code)  return INDEX;
        if (code == ETF.code)    return ETF;
        if (code == CUSTOM.code) return CUSTOM;
        if (code == STREAM.code) return STREAM;
    	
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
    
    
}
