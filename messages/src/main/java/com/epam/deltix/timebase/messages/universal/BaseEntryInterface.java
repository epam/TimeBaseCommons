package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;

/**
 * Base class for market data entry to be included in package (PackageHeader).
 */
public interface BaseEntryInterface extends BaseEntryInfo, RecordInterface {
  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   * @param value - Exchange Code
   */
  void setExchangeId(long value);

  /**
   * Exchange code compressed to long using ALPHANUMERIC(10) encoding.
   * see #getExchange()
   */
  void nullifyExchangeId();

  /**
   * True, if quote (or trade) comes from an implied Order book.
   * @param value - Is Implied
   */
  void setIsImplied(boolean value);

  /**
   * True, if quote (or trade) comes from an implied Order book.
   */
  void nullifyIsImplied();

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   * @param value - Contract ID
   */
  void setContractId(long value);

  /**
   * Special field designed to store multiple derivative instruments' updates
   * into single package. Most of the time should be static null.
   */
  void nullifyContractId();

  /**
   * Method nullifies all instance properties
   */
  @Override
  BaseEntryInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  BaseEntryInterface reset();

  @Override
  BaseEntryInterface copyFrom(RecordInfo template);
}
