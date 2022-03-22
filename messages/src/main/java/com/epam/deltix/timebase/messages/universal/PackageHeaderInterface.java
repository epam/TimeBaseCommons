package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.MarketMessageInterface;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.util.collections.generated.ObjectArrayList;

/**
 * Represents market data package.
 */
public interface PackageHeaderInterface extends PackageHeaderInfo, MarketMessageInterface {
  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @param value - Entries
   */
  void setEntries(ObjectArrayList<BaseEntryInfo> value);

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   */
  void nullifyEntries();

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @param value - Package Type
   */
  void setPackageType(PackageType value);

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   */
  void nullifyPackageType();

  /**
   * Method nullifies all instance properties
   */
  @Override
  PackageHeaderInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  PackageHeaderInterface reset();

  @Override
  PackageHeaderInterface copyFrom(RecordInfo template);
}
