package deltix.qsrv.hf.pub;

import java.lang.CharSequence;

/**
 * Base class for all messages that could be written in Timebase.
 */
public interface InstrumentMessageInfo extends AbstractMessageInfo {
  /**
   * Instrument type.
   * @return Instrument Type
   */
  InstrumentType getInstrumentType();

  /**
   * Instrument type.
   * @return true if Instrument Typeis not null
   */
  boolean hasInstrumentType();

  /**
   * Instrument name.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Instrument name.
   * @return true if Symbolis not null
   */
  boolean hasSymbol();
}
