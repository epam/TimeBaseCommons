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
package com.epam.deltix.util.collections;

/**
 * Similar to ByteArrayList but not resizable (although supports flyweight pattern)
 */
public final class ByteArray {

    private byte[] array;
    private int offset;
    private int length;

    public ByteArray() {
        //array = new byte[0];
    }

    public ByteArray(byte [] array) {
        this.array = array;
        offset = 0;
        length = array.length;
    }

    public ByteArray(byte [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public ByteArray(int length) {
        array = new byte[length];
        offset = 0;
        this.length = length;
    }


    public void setArray(byte [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public byte get(int index) {
        if (index > length)
            throw new ArrayIndexOutOfBoundsException(index);
        return array[index + offset];
    }

    public void put(int index, int b) {
        if (index > length)
            throw new ArrayIndexOutOfBoundsException(index);
        array[index + offset] = (byte) b;
    }

    public int         getOffset(int local) {
        return offset + local;
    }

    public byte[] getArray() {
        return array;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isIdentical(ByteArray other) {
        if (length != other.length)
            return false;

        for (int i=0; i < length; i++) {
            if (get(i) != other.get(i))
                return false;
        }
        return true;
    }

    public void copyTo (int srcPos, byte [] dest, int destPos, int length) {
        System.arraycopy(array, offset + srcPos, dest, destPos, length);
    }

    public void copyFrom (byte [] src, int srcPos, int destPos, int length) {
        System.arraycopy(src, srcPos, array, offset + destPos, length);
    }

    public static void arraycopy(ByteArray src, int srcPos, ByteArray dest, int destPos, int length) {
        System.arraycopy(src.getArray(), src.getOffset(srcPos), dest.getArray(), dest.getOffset(destPos), length);
    }

    public void fill(byte b) {
        for (int i=0; i < length; i++)
            array[i + offset] = b;
    }
}