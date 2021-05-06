package com.epam.deltix.timebase.messages;

import java.io.Serializable;

/**
 * Immutable IdentityKey
 */
public class ConstantIdentityKey
    implements IdentityKey, Comparable <IdentityKey>, Serializable
{
    private static final long serialVersionUID = 1L;

    public final String             symbol;

    /**
     *  If the argument is already a ConstantInstrumentKey, cast it and return.
     *  Otherwise, create a copy and return that.
     *
     *  @param id       The identity to make immutable.
     *  @return         A guaranteed immutable version of the argument.
     */
    public static ConstantIdentityKey makeImmutable (IdentityKey id) {
        if (id instanceof ConstantIdentityKey)
            return ((ConstantIdentityKey) id);
        else
            return (new ConstantIdentityKey (id.getSymbol()));
    }

    // JAXB
    protected ConstantIdentityKey () {
        symbol = null;
    }

    public ConstantIdentityKey (CharSequence symbol) {
        this.symbol = symbol.toString ();
    }

    public ConstantIdentityKey (InstrumentKey copy) {
        this (copy.getSymbol ());
    }

    @Override
    public String               getSymbol () {
        return (symbol);
    }

    @Override
    public final boolean        equals (Object obj) {
        if (!(obj instanceof IdentityKey))
            return false;

        return (InstrumentKey.equals (this, (IdentityKey) obj));
    }

    @Override
    public final int            hashCode () {
        return (InstrumentKey.hashCode (this));
    }

    @Override
    public String               toString () {
        return (InstrumentKey.toString (this));
    }

    @Override
    public int                  compareTo (IdentityKey o) {
        return (InstrumentKey.compare (this, o));
    }
}
