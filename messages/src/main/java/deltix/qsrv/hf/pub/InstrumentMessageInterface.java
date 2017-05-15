package deltix.qsrv.hf.pub;

import java.lang.CharSequence;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface InstrumentMessageInterface extends InstrumentIdentity, InstrumentMessageInfo, AbstractMessageInterface {
  /**
   * Instrument type.
   * @param value - Instrument Type
   */
  void setInstrumentType(InstrumentType value);

  /**
   * Instrument type.
   */
  void nullifyInstrumentType();

  /**
   * Instrument name.
   * @param value - Symbol
   */
  void setSymbol(CharSequence value);

  /**
   * Instrument name.
   */
  void nullifySymbol();
}
