package com.epam.deltix.timebase.messages.schema;

import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import java.lang.CharSequence;
import java.lang.Override;

/**
 * This is a base class for schema data types.
 */
public interface DataTypeInterface extends DataTypeInfo, RecordInterface {
  /**
   * Binary representation (encoding) of a field in database and protocol.
   * @param value - Encoding
   */
  void setEncoding(CharSequence value);

  /**
   * Binary representation (encoding) of a field in database and protocol.
   */
  void nullifyEncoding();

  /**
   * True, if schema design allows the field to be nullable.
   * @param value - Is Nullable
   */
  void setIsNullable(boolean value);

  /**
   * True, if schema design allows the field to be nullable.
   */
  void nullifyIsNullable();

  /**
   * Method nullifies all instance properties
   */
  @Override
  DataTypeInterface nullify();

  /**
   * Resets all instance properties to their default values
   */
  @Override
  DataTypeInterface reset();

  @Override
  DataTypeInterface copyFrom(RecordInfo template);
}
