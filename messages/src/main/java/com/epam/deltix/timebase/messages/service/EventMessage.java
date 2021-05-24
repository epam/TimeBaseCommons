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

import com.epam.deltix.timebase.messages.InstrumentMessage;
import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.RecordInfo;
import com.epam.deltix.timebase.messages.RecordInterface;
import com.epam.deltix.timebase.messages.SchemaElement;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;

/**
 */
@OldElementName("deltix.qsrv.hf.pub.EventMessage")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.EventMessage",
    title = "Event Message"
)
public class EventMessage extends InstrumentMessage implements RecordInterface {
  public static final String CLASS_NAME = EventMessage.class.getName();

  /**
   */
  protected EventMessageType eventType = null;

  /**
   * @return Event Type
   */
  @SchemaElement(
      name = "eventType",
      title = "Event Type"
  )
  public EventMessageType getEventType() {
    return eventType;
  }

  /**
   * @param value - Event Type
   */
  public void setEventType(EventMessageType value) {
    this.eventType = value;
  }

  /**
   * @return true if Event Type is not null
   */
  public boolean hasEventType() {
    return eventType != null;
  }

  /**
   */
  public void nullifyEventType() {
    this.eventType = null;
  }

  /**
   * Creates new instance of this class.
   * @return new instance of this class.
   */
  @Override
  protected EventMessage createInstance() {
    return new EventMessage();
  }

  /**
   * Method nullifies all instance properties
   */
  @Override
  public EventMessage nullify() {
    super.nullify();
    nullifyEventType();
    return this;
  }

  /**
   * Resets all instance properties to their default values
   */
  @Override
  public EventMessage reset() {
    super.reset();
    eventType = null;
    return this;
  }

  /**
   * Method copies state to a given instance
   */
  @Override
  public EventMessage clone() {
    EventMessage t = createInstance();
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
    if (!(obj instanceof EventMessage)) return false;
    EventMessage other =(EventMessage)obj;
    if (hasEventType() != other.hasEventType()) return false;
    if (hasEventType() && getEventType() != other.getEventType()) return false;
    return true;
  }

  /**
   * Returns a hash code value for the object. This method is * supported for the benefit of hash tables such as those provided by.
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    if (hasEventType()) {
      hash = hash * 31 + getEventType().getNumber();
    }
    return hash;
  }

  /**
   * Method copies state to a given instance
   * @param template class instance that should be used as a copy source
   */
  @Override
  public EventMessage copyFrom(RecordInfo template) {
    super.copyFrom(template);
    if (template instanceof EventMessage) {
      EventMessage t = (EventMessage)template;
      if (t.hasEventType()) {
        setEventType(t.getEventType());
      } else {
        nullifyEventType();
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
    str.append("{ \"$type\":  \"EventMessage\"");
    if (hasEventType()) {
      str.append(", \"eventType\": \"").append(getEventType()).append("\"");
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