package com.epam.deltix.timebase.messages;

import java.lang.CharSequence;

/**
 */
public interface IdentityKeyInterface extends IdentityKey {
  /**
   * Instrument name.
   * @return Symbol
   */
  CharSequence getSymbol();

  /**
   * Instrument name.
   * @return true if Symbol is not null
   */
  boolean hasSymbol();

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
