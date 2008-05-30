package deltix.qsrv.hf.pub;

import deltix.qsrv.hf.pub.InstrumentType;

/**
 *
 */
public interface InstrumentIdentity {
    public CharSequence         getSymbol ();
    
    public InstrumentType       getType ();
}
