package deltix.qsrv.hf.pub;

import deltix.util.lang.Util;
import deltix.util.memory.EstimatorUtils;

/**
 *
 */
public class InstrumentMessage
    extends AbstractMessage
    implements InstrumentMessageInterface, InstrumentIdentity, Comparable<InstrumentMessage>
{
    //4.3//@Title ("Type") @PrimaryKey
    public InstrumentType instrumentType = InstrumentType.CUSTOM;

    //4.3//@Title ("Symbol") @PrimaryKey
    public CharSequence         symbol = "";

    /**
     * Instrument name.
     * @return Symbol
     */
    public CharSequence         getSymbol () {
      return symbol;
    }

    /**
     * Instrument name.
     * @param value - Symbol
     */
    public void setSymbol(CharSequence value) {
      this.symbol = value;
    }

    /**
     * Instrument name.
     * @return true if Symbolis not null
     */
    public boolean hasSymbol() {
      return symbol != null;
    }

    /**
     * Instrument name.
     */
    public void nullifySymbol() {
      this.symbol = null;
    }
    /**
     * Instrument type.
     * @return Instrument Type
     */
    public InstrumentType getInstrumentType() {
      return instrumentType;
    }

    /**
     * Instrument type.
     * @param value - Instrument Type
     */
    public void setInstrumentType(InstrumentType value) {
      this.instrumentType = value;
    }

    /**
     * Instrument type.
     * @return true if Instrument Typeis not null
     */
    public boolean hasInstrumentType() {
        return instrumentType != null;
    }

    /**
     * Instrument type.
     */
    public void nullifyInstrumentType() {
      this.instrumentType = null;
    }
    /**
     * Method copies state of given template into this object
     * @param deep if true performs deep copy of mutable properties
     */
    @Override
    public void                 copy (Object template, boolean deep) {
        super.copy (template, deep);

        if (template instanceof InstrumentIdentity) {
            InstrumentIdentity   sm = (InstrumentIdentity) template;

            instrumentType = sm.getInstrumentType ();
            symbol = sm.getSymbol ();
            if (deep && symbol != null)
                symbol = symbol.toString ();
        }
    }

    @Override
    public long                 getSizeInMemory () {
        long        size = super.getSizeInMemory () + 2 * SIZE_OF_POINTER;

        // Err on the conservative side... Assume the string is not shared.
        if (symbol != null && symbol.getClass () == String.class)
            size += EstimatorUtils.getSizeInMemory ((String) symbol);
        else
            size += SIZE_OF_POINTER;

        return (size);
    }

    public int                  compareTime(TimeStamp time) {
        // Do not change. JIT compile this implementation into highly optimized branch free code with 4 cmovnl instructions.
        // If nanos does not belong to 0..999999 range or timestamp does not belong to 0..2^53
        // then timestamp comparison result is Undefined
        long nanos1 = timestamp * TimeStamp.NANOS_PER_MS + nanoTime;
        long nanos2 = time.timestamp * TimeStamp.NANOS_PER_MS + time.nanosComponent;
        nanos1 -= nanos2;
        return (nanos1 > 0 ? 1 : 0) - (nanos1 < 0 ? 1 : 0);
    }
    
    @Override
    public InstrumentMessage    copy (boolean deep) {
        return ((InstrumentMessage) super.copy (deep));
    }

    @Override
    public int                  compareTo(InstrumentMessage o) {
        if (timestamp == o.timestamp) {
            if (nanoTime == o.nanoTime) {
                int r = Util.fastCompare(symbol, o.symbol);
                return r != 0 ? r : instrumentType.compareTo(o.instrumentType);
            }

            return nanoTime > o.nanoTime ? 1 : -1;
        }

        return timestamp > o.timestamp ? 1 : -1;
    }

    public String               toString () {
        return (
            String.format (
                "%s,%s,%s,%s",
                getClass ().getSimpleName (),
                instrumentType != null ? instrumentType.name () : "<null>",
                symbol != null ? symbol.toString() : "<null>",
                getTimeString()
            )
        );
    }

    /**
     * Reset all instance field to their default states.
     * @return this.
     */
    public RecordInterface reset() {
        super.reset();
        instrumentType = InstrumentType.CUSTOM;
        symbol = "";
        return this;
    }

    /**
     * Set null to all fields of this instance.
     * @return this.
     */
    public RecordInterface nullify() {
        super.nullify();
        nullifyInstrumentType();
        nullifySymbol();
        return this;
    }

    /**
     * Deep copies content of this instance to destination instance.
     * @param dst destination for copy.
     */
    public void copyTo(RecordInterface dst) {
        super.copyTo(dst);
        InstrumentMessageInterface dstCasted = (InstrumentMessageInterface)dst;
        dstCasted.setSymbol(symbol);
        dstCasted.setInstrumentType(instrumentType);
    }
}
