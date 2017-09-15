package deltix.qsrv.hf.pub;

/**
 * Created by VavilauA on 5/12/2017.
 */
public interface InstrumentIdentityInterface extends InstrumentIdentity {
    void         setSymbol (CharSequence symbol);

    void       setInstrumentType(InstrumentType type);
}
