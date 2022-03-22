package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;

/**
 * Base class for market data entry to be included in package (PackageHeader).
 */
public interface BaseEntryInfo extends RecordInfo {
  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @return Exchange Code
   */
  long getExchangeId();

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @return true if Exchange Code is not null
   */
  boolean hasExchangeId();

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @return Is Implied
   */
  boolean isImplied();

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @return true if Is Implied is not null
   */
  boolean hasIsImplied();

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @return Contract ID
   */
  long getContractId();

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @return true if Contract ID is not null
   */
  boolean hasContractId();

  /**
   * Method copies state to a given instance
   */
  @Override
  BaseEntryInfo clone();
}
