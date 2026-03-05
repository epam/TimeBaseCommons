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
package com.epam.deltix.util.buffer;


public interface AtomicBuffer extends MutableBuffer {

    void verifyAlignment();

    long getLongVolatile(int index);

    void putLongVolatile(int index, long value);

    void putLongOrdered(int index, long value);

    boolean compareAndSetLong(int index, long expectedValue, long updateValue);

    long getAndSetLong(int index, long value);

    long getAndAddLong(int index, long delta);

    int getIntVolatile(int index);

    void putIntVolatile(int index, int value);

    void putIntOrdered(int index, int value);

    boolean compareAndSetInt(int index, int expectedValue, int updateValue);

    int getAndSetInt(int index, int value);

    int getAndAddInt(int index, int delta);

    short getShortVolatile(int index);

    void putShortVolatile(int index, short value);

    byte getByteVolatile(int index);

    void putByteVolatile(int index, byte value);

    char getCharVolatile(int index);

    void putCharVolatile(int index, char value);

}