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