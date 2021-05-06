package com.epam.deltix.timebase.messages.service;

import com.epam.deltix.timebase.messages.OldElementName;
import com.epam.deltix.timebase.messages.SchemaElement;

/**
 */
@OldElementName("deltix.qsrv.hf.pub.EventMessage$EventType")
@SchemaElement(
    name = "com.epam.deltix.timebase.messages.service.EventMessageType",
    title = "Event Type"
)
public enum EventMessageType {
  /**
   */
  @SchemaElement(
      name = "STREAM_DELETED"
  )
  STREAM_DELETED(0),

  /**
   */
  @SchemaElement(
      name = "STREAM_CREATED"
  )
  STREAM_CREATED(1),

  /**
   */
  @SchemaElement(
      name = "READ_LOCK_ACQUIRED"
  )
  READ_LOCK_ACQUIRED(2),

  /**
   */
  @SchemaElement(
      name = "READ_LOCK_RELEASED"
  )
  READ_LOCK_RELEASED(3),

  /**
   */
  @SchemaElement(
      name = "WRITE_LOCK_ACQUIRED"
  )
  WRITE_LOCK_ACQUIRED(4),

  /**
   */
  @SchemaElement(
      name = "WRITE_LOCK_RELEASED"
  )
  WRITE_LOCK_RELEASED(5);

  private final int value;

  EventMessageType(int value) {
    this.value = value;
  }

  public int getNumber() {
    return this.value;
  }

  public static EventMessageType valueOf(int number) {
    switch (number) {
      case 0: return STREAM_DELETED;
      case 1: return STREAM_CREATED;
      case 2: return READ_LOCK_ACQUIRED;
      case 3: return READ_LOCK_RELEASED;
      case 4: return WRITE_LOCK_ACQUIRED;
      case 5: return WRITE_LOCK_RELEASED;
      default: return null;
    }
  }

  public static EventMessageType strictValueOf(int number) {
    final EventMessageType value = valueOf(number);
    if (value == null) {
      throw new IllegalArgumentException("Enumeration 'EventMessageType' does not have value corresponding to '" + number + "'.");
    }
    return value;
  }
}
