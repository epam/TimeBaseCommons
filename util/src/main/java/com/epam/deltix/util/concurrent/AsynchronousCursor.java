package com.epam.deltix.util.concurrent;


/**
 *  A cursor supporting asynchronous data transfer. Note that
 *  the concept of "data being available" supported by AsynchronousDataSource
 *  means that a subsequent call to {@link AbstractCursor#next} will not block.
 *  It does not mean that a subsequent call to {@link AbstractCursor#next} will 
 *  return <code>true</code>.
 */
public interface AsynchronousCursor 
    extends AbstractCursor, AsynchronousDisposableDataSource 
{
}
