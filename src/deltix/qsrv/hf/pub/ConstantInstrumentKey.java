package deltix.qsrv.hf.pub;

import deltix.data.stream.*;

/**
 *
 */
public class ConstantInstrumentKey implements InstrumentIdentity {
    public final InstrumentType     instrumentType;    
    public final String             symbol;

    public ConstantInstrumentKey (InstrumentType instrumentType, CharSequence symbol) {
        this.instrumentType = instrumentType;
        this.symbol = symbol.toString ();
    }
    
    public ConstantInstrumentKey (InstrumentIdentity copy) {
        this (copy.getType (), copy.getSymbol ());
    }
    
    public String               getSymbol () {
        return (symbol);
    }

    public InstrumentType       getType () {
        return (instrumentType);
    }

    @Override
    public boolean              equals (Object obj) {
        if (!(obj instanceof InstrumentIdentity))
            return false;
       
        return (InstrumentKey.equals (this, (InstrumentIdentity) obj));
    }

    @Override
    public int                  hashCode () {
        return (InstrumentKey.hashCode (this));
    }        
}
