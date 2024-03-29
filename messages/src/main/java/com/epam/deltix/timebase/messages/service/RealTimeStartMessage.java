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
 * Special transient message that is inserted into MessageSource to signal transition from historic to real-time data.
 * Message indicates that RealTimeMessageSource message source switched to read-time mode.
 * Can be received only when see "SelectionOptions.RealTimeNotification" = true.
 */
@OldElementName("deltix.qsrv.hf.pub.RealTimeStartMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.RealTimeStartMessage",
    title = "Real Time Start Message"
)
@SchemaGuid(RealTimeStartMessage.DESCRIPTOR_GUID)
public class RealTimeStartMessage extends SystemMessage implements RecordInterface {
  public static final String CLASS_NAME = RealTimeStartMessage.class.getName();

  /**
   */
  public static final String DESCRIPTOR_GUID = "SYS:RealTimeStartMessage:1";

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected RealTimeStartMessage createInstance() {
    return new RealTimeStartMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public RealTimeStartMessage nullify() {
    super.nullify();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public RealTimeStartMessage reset() {
    super.reset();
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public RealTimeStartMessage clone() {
    RealTimeStartMessage t = createInstance();
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
    if (!(obj instanceof RealTimeStartMessage)) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public RealTimeStartMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
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
    str.append("{ \"$type\":  \"RealTimeStartMessage\"");
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
