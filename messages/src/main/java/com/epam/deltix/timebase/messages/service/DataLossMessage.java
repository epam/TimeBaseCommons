/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import com.epam.deltix.timebase.messages.SchemaGuid;
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Message class, which describes data loss for 'lossy' Transient streams. See see "BufferOptions.Lossless".
 * Such message can be received from see "ITickCursor", when message loss is occurred.
 * TickStream should have see "DataLossMessage" Class Descriptor defined in stream schema. See
 * see "Descriptor" .
 */
@OldElementName("deltix.qsrv.hf.pub.DataLossMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.DataLossMessage",
    title = "Data Loss Message"
)
@SchemaGuid(DataLossMessage.DESCRIPTOR_GUID)
public class DataLossMessage extends SystemMessage implements RecordInterface {
  public static final String CLASS_NAME = DataLossMessage.class.getName();

  /**
   */
  public static final String DESCRIPTOR_GUID = "SYS:DataLossMessage:2";

  /**
   * Number of the lost bytes.
   */
  protected long bytes = TypeConstants.INT64_NULL;

  /**
   * The last reported message time before byte loss occurs. Precision - milliseconds.
   */
  protected long fromTime = TypeConstants.TIMESTAMP_UNKNOWN;

  /**
   * Number of the lost bytes.
   * @return Bytes
   */
  @SchemaElement(
      name = "bytes",
      title = "Bytes"
  )
  public long getBytes() {
    return bytes;
  }

  /**
   * Number of the lost bytes.
   * @param value - Bytes
   */
  public void setBytes(long value) {
    this.bytes = value;
  }

  /**
   * Number of the lost bytes.
   * @return true if Bytes is not null
   */
  public boolean hasBytes() {
    return bytes != com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
  }

  /**
   * Number of the lost bytes.
   */
  public void nullifyBytes() {
    this.bytes = com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
  }

  /**
   * The last reported message time before byte loss occurs. Precision - milliseconds.
   * @return From Time
   */
  @SchemaElement(
      name = "fromTime",
      title = "From Time"
  )
  public long getFromTime() {
    return fromTime;
  }

  /**
   * The last reported message time before byte loss occurs. Precision - milliseconds.
   * @param value - From Time
   */
  public void setFromTime(long value) {
    this.fromTime = value;
  }

  /**
   * The last reported message time before byte loss occurs. Precision - milliseconds.
   * @return true if From Time is not null
   */
  public boolean hasFromTime() {
    return fromTime != com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * The last reported message time before byte loss occurs. Precision - milliseconds.
   */
  public void nullifyFromTime() {
    this.fromTime = com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected DataLossMessage createInstance() {
    return new DataLossMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public DataLossMessage nullify() {
    super.nullify();
    nullifyBytes();
    nullifyFromTime();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public DataLossMessage reset() {
    super.reset();
    bytes = com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
    fromTime = com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public DataLossMessage clone() {
    DataLossMessage t = createInstance();
    t.copyFrom(this);
    return t;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    boolean superEquals = super.equals(obj);
    if (!superEquals) return false;
    if (!(obj instanceof DataLossMessage)) return false;
    DataLossMessage other =(DataLossMessage)obj;
    if (hasBytes() != other.hasBytes()) return false;
    if (hasBytes() && getBytes() != other.getBytes()) return false;
    if (hasFromTime() != other.hasFromTime()) return false;
    if (hasFromTime() && getFromTime() != other.getFromTime()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasBytes()) {
      hash = hash * 31 + ((int)(getBytes() ^ (getBytes() >>> 32)));
    }
    if (hasFromTime()) {
      hash = hash * 31 + ((int)(getFromTime() ^ (getFromTime() >>> 32)));
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public DataLossMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof DataLossMessage) {
      DataLossMessage t = (DataLossMessage)template;
      if (t.hasBytes()) {
        setBytes(t.getBytes());
      } else {
        nullifyBytes();
      }
      if (t.hasFromTime()) {
        setFromTime(t.getFromTime());
      } else {
        nullifyFromTime();
      }
    }
    return this;
  }

  /**
   * @return a string representation of this class object.
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    return toString(str).toString();
  }

  /**
   * @return a string representation of this class object.
   */
  @Override
  public StringBuilder toString(StringBuilder str) {
    str.append("{ \"$type\":  \"DataLossMessage\"");
    if (hasBytes()) {
      str.append(", \"bytes\": ").append(getBytes());
    }
    if (hasFromTime()) {
      str.append(", \"fromTime\": \"").append(getFromTime()).append("\"");
    }
    if (hasTimeStampMs()) {
      str.append(", \"timeStampMs\": \"").append(getTimeStampMs()).append("\"");
    }
    if (hasSymbol()) {
      str.append(", \"symbol\": \"").append(getSymbol()).append("\"");
    }
    str.append("}");
    return str;
  }
}