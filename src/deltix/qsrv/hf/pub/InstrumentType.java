package deltix.qsrv.hf.pub;

/**
 *  Type of financial instrument
 */
public enum InstrumentType {
    EQUITY('S'),
    OPTION('O'),
    FUTURE('F'),
    BOND('B'),
    FX('X');
    
    private final char code;
    
    InstrumentType (int code) { this.code = (char)code; }
         
    public char toChar () { return code; }  
         
    public static InstrumentType fromChar (char code) {
    	if (code == EQUITY.code) return EQUITY;
    	if (code == OPTION.code) return OPTION;
    	if (code == FUTURE.code) return FUTURE;
    	if (code == BOND.code)   return BOND;
    	if (code == FX.code)     return FX;
    	
        throw new IllegalArgumentException ("Unknown InstrumentType code: " + code);
    }
}
