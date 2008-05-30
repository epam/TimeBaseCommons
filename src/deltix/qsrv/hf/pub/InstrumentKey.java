package deltix.qsrv.hf.pub;

import deltix.data.stream.*;
import deltix.util.Util;

/**
 *
 */
public class InstrumentKey implements InstrumentIdentity {
    public InstrumentType       instrumentType;    
    public CharSequence         symbol;

    public InstrumentKey () { }

    public InstrumentKey (InstrumentType instrumentType, CharSequence symbol) {
        this.instrumentType = instrumentType;
        this.symbol = symbol;
    }
    
    public InstrumentKey (InstrumentIdentity copy) {
        instrumentType = copy.getType ();
        symbol = copy.getSymbol ();
    }
    
    public CharSequence         getSymbol () {
        return (symbol);
    }

    public InstrumentType       getType () {
        return (instrumentType);
    }

    public void                 makePersistent () {
        symbol = symbol.toString ();
    }
    
    public static int           hashCode (InstrumentIdentity id) {
        return (id.getType ().hashCode () + Util.hashCode (id.getSymbol ()));
    }
    
    public static boolean       equals (InstrumentIdentity id1, InstrumentIdentity id2) {
        return (
            id1.getType () == id2.getType () &&
            Util.equals (id1.getSymbol (), id2.getSymbol ())
        );
    }
    
    @Override
    public boolean              equals (Object obj) {
        if (!(obj instanceof InstrumentIdentity))
            return false;
       
        return (equals (this, (InstrumentIdentity) obj));
    }

    @Override
    public int                  hashCode () {
        return (hashCode (this));
    }        
}
