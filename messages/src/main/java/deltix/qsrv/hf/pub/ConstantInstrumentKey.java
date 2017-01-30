package deltix.qsrv.hf.pub;

import java.io.Serializable;

/**
 *
 */
public class ConstantInstrumentKey
    implements InstrumentIdentity, Comparable <InstrumentIdentity>, Serializable
{
    private static final long serialVersionUID = 1L;

    public final InstrumentType     instrumentType;
    public final String             symbol;

    /**
     *  If the argument is already a ConstantInstrumentKey, cast it and return.
     *  Otherwise, create a copy and return that.
     *
     *  @param id       The identity to make immutable.
     *  @return         A guaranteed immutable version of the argument.
     */
    public static ConstantInstrumentKey makeImmutable (InstrumentIdentity id) {
        if (id instanceof ConstantInstrumentKey)
            return ((ConstantInstrumentKey) id);
        else
            return (new ConstantInstrumentKey (id));
    }

    // JAXB
    protected ConstantInstrumentKey () {
        instrumentType = null;
        symbol = null;
    }

    public ConstantInstrumentKey (InstrumentType instrumentType, CharSequence symbol) {
        this.instrumentType = instrumentType;
        this.symbol = symbol.toString ();
    }

    public ConstantInstrumentKey (InstrumentIdentity copy) {
        this (copy.getInstrumentType (), copy.getSymbol ());
    }

    @Override
    public String               getSymbol () {
        return (symbol);
    }

    @Override
    public InstrumentType       getInstrumentType () {
        return (instrumentType);
    }

    @Override
    public final boolean              equals (Object obj) {
        if (!(obj instanceof InstrumentIdentity))
            return false;

        return (InstrumentKey.equals (this, (InstrumentIdentity) obj));
    }

    @Override
    public final int                  hashCode () {
        return (InstrumentKey.hashCode (this));
    }

    @Override
    public String               toString () {
        return (InstrumentKey.toString (this));
    }

    @Override
    public int                  compareTo (InstrumentIdentity o) {
        return (InstrumentKey.compare (this, o));
    }
}
