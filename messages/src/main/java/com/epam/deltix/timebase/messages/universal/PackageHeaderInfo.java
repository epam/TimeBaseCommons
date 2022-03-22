package com.epam.deltix.timebase.messages.universal;

import com.epam.deltix.timebase.messages.MarketMessageInfo;
import com.epam.deltix.util.collections.generated.ObjectList;

/**
 * Represents market data package.
 */
public interface PackageHeaderInfo extends MarketMessageInfo {
  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @return Entries
   */
  ObjectList<BaseEntryInfo> getEntries();

  /**
   * Message package content. Array of individual entries.
   * Typical entries classes are L1Entry, L2Entry, L3Entry, TradeEntry.
   * @return true if Entries is not null
   */
  boolean hasEntries();

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @return Package Type
   */
  PackageType getPackageType();

  /**
   * Package type needs to distinguish between incremental changes and different types of snapshot.
   * @return true if Package Type is not null
   */
  boolean hasPackageType();

  /**
   * Method copies state to a given instance
   */
  @Override
  PackageHeaderInfo clone();
}
