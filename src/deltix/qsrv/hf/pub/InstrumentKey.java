package deltix.qsrv.hf.pub;

import deltix.util.lang.Util;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Pair { InstrumentType, symbol } that provides InstrumentIdentity.
 *
 * Warning: this class is mutable and cannot be used as java.util.Map key. Use {@link ConstantInstrumentKey} instead.
 */
public class InstrumentKey
    implements InstrumentIdentity, Comparable <InstrumentIdentity>, Serializable
{
    private static final long serialVersionUID = 1L;
    
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

    public static int           hashCode (InstrumentType type, CharSequence symbol) {
        return (23 * type.ordinal()  + Util.hashCode (symbol));
    }

    public static int           hashCode (InstrumentIdentity id) {
        return (hashCode (id.getType (), id.getSymbol ()));
    }

    public static boolean       equals (InstrumentIdentity id1, InstrumentIdentity id2) {
        return (
            id1.getType () == id2.getType () &&
            Util.equals (id1.getSymbol (), id2.getSymbol ())
        );
    }

    public static int           compare (InstrumentIdentity id1, InstrumentIdentity id2) {
        int dif = id1.getType ().compareTo (id2.getType ());

        if (dif != 0)
            return (dif);

        return (Util.compare (id1.getSymbol (), id2.getSymbol (), false));
    }

    public static String        toString (InstrumentIdentity id) {
        return (id.getSymbol () + ":" + id.getType ());
    }

    /** Warning: this class is mutable and cannot be used as java.util.Map key. Use {@link ConstantInstrumentKey} instead. */
    @Override
    public final boolean              equals (Object obj) {
        // Must be the same as ConstantInstrumentKey.equals()

        if (!(obj instanceof InstrumentIdentity))
            return false;

        return (equals (this, (InstrumentIdentity) obj));
    }

    /** Warning: this class is mutable and cannot be used as java.util.Map key. Use {@link ConstantInstrumentKey} instead. */
    @Override
    public final int                  hashCode () {
        // Must be the same as ConstantInstrumentKey.hashCode()
        return (hashCode (this));
    }

    @Override
    public String               toString () {
        return (toString (this));
    }

    public int                  compareTo (InstrumentIdentity o) {
        return (compare (this, o));
    }

    /// Serializable

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(instrumentType);
        if (symbol instanceof Serializable) {
            oos.writeObject(symbol);
        } else {
            oos.writeObject(symbol.toString());
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        instrumentType = (InstrumentType) ois.readObject();
        symbol = (CharSequence) ois.readObject();
    }

    /**
    *   Creates InstrumentKey from String representation.
    *   @param value    string in format Symbol:InstrumentType
    *   @return         new InstrumentKey.
    **/
    public static InstrumentKey               valueOf(String value) {
        InstrumentKey key = new InstrumentKey();
        int colon = value.indexOf(':');
        if (colon < 0)
            throw new IllegalArgumentException("Cannot parse " + value + ". Expecting Symbol:InstrumentType format");

        key.instrumentType = InstrumentType.valueOf(value.substring(colon+1));
        key.symbol = value.substring(0, colon);

        return key;
    }
}
