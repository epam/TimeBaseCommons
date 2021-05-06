package com.epam.deltix.timebase.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Mutable IdentityKey
 *
 * Warning: this class is mutable and cannot be used as java.util.Map key. Use {ConstantIdentityKey} instead.
 */
public class InstrumentKey
    implements IdentityKey, Comparable <IdentityKey>, Serializable
{
    private static final long serialVersionUID = 1L;

    public CharSequence         symbol;

    public InstrumentKey () { }

    public InstrumentKey (CharSequence symbol) {
        this.symbol = symbol;
    }

    public InstrumentKey (IdentityKey copy) {
        symbol = copy.getSymbol ();
    }

    /**
     * Identity name.
     * @return String representation of identity name.
     */
    @Override
    public CharSequence         getSymbol () {
        return (symbol);
    }

    public void                 makePersistent () {
        symbol = symbol.toString ();
    }

    public static int           hashCode (CharSequence symbol) {
        return (CharSequenceUtils.hashCode (symbol));
    }

    public static int           hashCode (IdentityKey id) {
        return (hashCode (id.getSymbol ()));
    }

    public static boolean       equals (IdentityKey id1, IdentityKey id2) {
        return (
            CharSequenceUtils.equals (id1.getSymbol (), id2.getSymbol ())
        );
    }

    public static int           compare (IdentityKey id1, IdentityKey id2) {

        return (CharSequenceUtils.compare (id1.getSymbol (), id2.getSymbol (), false));
    }

    public static String        toString (IdentityKey id) {
        if (id.getSymbol () != null)
            return (id.getSymbol ().toString());

        return "<null>";
    }

    /** Warning: this class is mutable and cannot be used as java.util.Map key. Use {ConstantInstrumentKey} instead. */
    @Override
    public final boolean              equals (Object obj) {
        // Must be the same as ConstantInstrumentKey.equals()

        if (!(obj instanceof IdentityKey))
            return false;

        return (equals (this, (IdentityKey) obj));
    }

    /** Warning: this class is mutable and cannot be used as java.util.Map key. Use {ConstantInstrumentKey} instead. */
    @Override
    public final int                  hashCode () {
        // Must be the same as ConstantInstrumentKey.hashCode()
        return (hashCode (this));
    }

    @Override
    public String               toString () {
        return (toString (this));
    }

    @Override
    public int                  compareTo (IdentityKey o) {
        return (compare (this, o));
    }

    /// Serializable

    private void writeObject(ObjectOutputStream oos) throws IOException {
        if (symbol instanceof Serializable) {
            oos.writeObject(symbol);
        } else {
            oos.writeObject(symbol.toString());
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        symbol = (CharSequence) ois.readObject();
    }
}
