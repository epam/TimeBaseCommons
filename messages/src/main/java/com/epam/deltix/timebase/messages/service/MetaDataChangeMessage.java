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

import com.epam.deltix.timebase.messages.*;

/**
 * Special transient message that signals active stream consumers that their stream metadata has been changed
 * Used for advanced stream monitoring.
 * see also "SelectionOptions.VersionTracking"
 */
@OldElementName("deltix.qsrv.hf.pub.MetaDataChangeMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.MetaDataChangeMessage",
    title = "Meta Data Change Message"
)
public class MetaDataChangeMessage extends SystemMessage implements RecordInterface {
  public static final String CLASS_NAME = MetaDataChangeMessage.class.getName();

  /**
   * Indicates, that stream data was converted.
   */
  protected byte converted = TypeConstants.BOOLEAN_NULL;

  /**
   */
  protected long version = TypeConstants.INT64_NULL;

  /**
   * Indicates, that stream data was converted.
   * @return Converted
   */
  @SchemaElement(
      name = "converted",
      title = "Converted"
  )
  public boolean isConverted() {
    return converted == 1;
  }

  /**
   * Indicates, that stream data was converted.
   * @param value - Converted
   */
  public void setConverted(boolean value) {
    this.converted = (byte)(value ? 1 : 0);
  }

  /**
   * Indicates, that stream data was converted.
   * @return true if Converted is not null
   */
  public boolean hasConverted() {
    return converted != TypeConstants.BOOLEAN_NULL;
  }

  /**
   * Indicates, that stream data was converted.
   */
  public void nullifyConverted() {
    this.converted = TypeConstants.BOOLEAN_NULL;
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
    return version != TypeConstants.INT64_NULL;
  }

  /**
   */
  public void nullifyVersion() {
    this.version = TypeConstants.INT64_NULL;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected MetaDataChangeMessage createInstance() {
    return new MetaDataChangeMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public MetaDataChangeMessage nullify() {
    super.nullify();
    nullifyConverted();
    nullifyVersion();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public MetaDataChangeMessage reset() {
    super.reset();
    converted = TypeConstants.BOOLEAN_NULL;
    version = TypeConstants.INT64_NULL;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public MetaDataChangeMessage clone() {
    MetaDataChangeMessage t = createInstance();
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
    if (!(obj instanceof MetaDataChangeMessage)) return false;
    MetaDataChangeMessage other =(MetaDataChangeMessage)obj;
    if (hasConverted() != other.hasConverted()) return false;
    if (hasConverted() && isConverted() != other.isConverted()) return false;
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
    if (hasConverted()) {
      hash = hash * 31 + (isConverted() ? 1231 : 1237);
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
  public MetaDataChangeMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof MetaDataChangeMessage) {
      MetaDataChangeMessage t = (MetaDataChangeMessage)template;
      if (t.hasConverted()) {
        setConverted(t.isConverted());
      } else {
        nullifyConverted();
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
    str.append("{ \"$type\":  \"MetaDataChangeMessage\"");
    if (hasConverted()) {
      str.append(", \"converted\": ").append(isConverted());
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
