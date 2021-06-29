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
import com.epam.deltix.timebase.messages.TypeConstants;
import java.lang.CharSequence;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import com.epam.deltix.containers.BinaryAsciiString;
import com.epam.deltix.containers.CharSequenceUtils;
import com.epam.deltix.containers.MutableString;

/**
 * Special transient message that signals active stream consumers that their stream has been truncated
 * Used for advanced stream monitoring.
 * see also "SelectionOptions.VersionTracking"
 */
@OldElementName("deltix.qsrv.hf.pub.StreamTruncatedMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.StreamTruncatedMessage",
    title = "Stream Truncated Message"
)
public class StreamTruncatedMessage extends SystemMessage implements RecordInterface {
  public static final String CLASS_NAME = StreamTruncatedMessage.class.getName();

  /**
   */
  protected CharSequence instruments = null;

  /**
   * Time of truncation in nanoseconds
   */
  protected long truncateTime = TypeConstants.TIMESTAMP_UNKNOWN;

  /**
   */
  protected long version = TypeConstants.INT64_NULL;

  /**
   * @return Instruments
   */
  @SchemaElement(
      name = "instruments",
      title = "Instruments"
  )
  public CharSequence getInstruments() {
    return instruments;
  }

  /**
   * @param value - Instruments
   */
  public void setInstruments(CharSequence value) {
    this.instruments = value;
  }

  /**
   * @return true if Instruments is not null
   */
  public boolean hasInstruments() {
    return instruments != null;
  }

  /**
   */
  public void nullifyInstruments() {
    this.instruments = null;
  }

  /**
   * Time of truncation in nanoseconds
   * @return Truncated Time
   */
  @SchemaElement(
      name = "truncateTime",
      title = "Truncated Time"
  )
  public long getTruncateTime() {
    return truncateTime;
  }

  /**
   * Time of truncation in nanoseconds
   * @param value - Truncated Time
   */
  public void setTruncateTime(long value) {
    this.truncateTime = value;
  }

  /**
   * Time of truncation in nanoseconds
   * @return true if Truncated Time is not null
   */
  public boolean hasTruncateTime() {
    return truncateTime != com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * Time of truncation in nanoseconds
   */
  public void nullifyTruncateTime() {
    this.truncateTime = com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
  }

  /**
   * @return Version
   */
  @SchemaElement(
      name = "version",
      title = "Version"
  )
  public long getVersion() {
    return version;
  }

  /**
   * @param value - Version
   */
  public void setVersion(long value) {
    this.version = value;
  }

  /**
   * @return true if Version is not null
   */
  public boolean hasVersion() {
    return version != com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
  }

  /**
   */
  public void nullifyVersion() {
    this.version = com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected StreamTruncatedMessage createInstance() {
    return new StreamTruncatedMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public StreamTruncatedMessage nullify() {
    super.nullify();
    nullifyInstruments();
    nullifyTruncateTime();
    nullifyVersion();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public StreamTruncatedMessage reset() {
    super.reset();
    instruments = null;
    truncateTime = com.epam.deltix.timebase.messages.TypeConstants.TIMESTAMP_UNKNOWN;
    version = com.epam.deltix.timebase.messages.TypeConstants.INT64_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public StreamTruncatedMessage clone() {
    StreamTruncatedMessage t = createInstance();
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
    if (!(obj instanceof StreamTruncatedMessage)) return false;
    StreamTruncatedMessage other =(StreamTruncatedMessage)obj;
    if (hasInstruments() != other.hasInstruments()) return false;
    if (hasInstruments()) {
      if (getInstruments().length() != other.getInstruments().length()) return false; else {
        CharSequence s1 = getInstruments();
        CharSequence s2 = other.getInstruments();
        if ((s1 instanceof MutableString && s2 instanceof MutableString) || (s1 instanceof String && s2 instanceof String) || (s1 instanceof BinaryAsciiString && s2 instanceof BinaryAsciiString)) {
          if (!s1.equals(s2)) return false;
        } else {
          if (!CharSequenceUtils.equals(s1, s2)) return false;
        }
      }
    }
    if (hasTruncateTime() != other.hasTruncateTime()) return false;
    if (hasTruncateTime() && getTruncateTime() != other.getTruncateTime()) return false;
    if (hasVersion() != other.hasVersion()) return false;
    if (hasVersion() && getVersion() != other.getVersion()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasInstruments()) {
      hash = hash * 31 + getInstruments().hashCode();
    }
    if (hasTruncateTime()) {
      hash = hash * 31 + ((int)(getTruncateTime() ^ (getTruncateTime() >>> 32)));
    }
    if (hasVersion()) {
      hash = hash * 31 + ((int)(getVersion() ^ (getVersion() >>> 32)));
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public StreamTruncatedMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof StreamTruncatedMessage) {
      StreamTruncatedMessage t = (StreamTruncatedMessage)template;
      if (t.hasInstruments()) {
        if (hasInstruments() && getInstruments() instanceof StringBuilder) {
          ((StringBuilder)getInstruments()).setLength(0);
        } else {
          setInstruments(new StringBuilder());
        }
        ((StringBuilder)getInstruments()).append(t.getInstruments());
      } else {
        nullifyInstruments();
      }
      if (t.hasTruncateTime()) {
        setTruncateTime(t.getTruncateTime());
      } else {
        nullifyTruncateTime();
      }
      if (t.hasVersion()) {
        setVersion(t.getVersion());
      } else {
        nullifyVersion();
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
    str.append("{ \"$type\":  \"StreamTruncatedMessage\"");
    if (hasInstruments()) {
      str.append(", \"instruments\": \"").append(getInstruments()).append("\"");
    }
    if (hasTruncateTime()) {
      str.append(", \"truncateTime\": \"").append(getTruncateTime()).append("\"");
    }
    if (hasVersion()) {
      str.append(", \"version\": ").append(getVersion());
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