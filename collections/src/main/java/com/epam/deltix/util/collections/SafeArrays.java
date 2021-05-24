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
 * @author Jacob Alber
 */
public class SafeArrays {

//    @SafeVarargs
//    public static <Generic> Generic[] asArray(Generic... array) {
//        return array;
//    }

    public static <Generic> void safeArrayCopy(Generic[] src, int srcPos, Generic[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(short[] src, int srcPos, short[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(long[] src, int srcPos, long[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(double[] src, int srcPos, double[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(float[] src, int srcPos, float[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static void safeArrayCopy(boolean[] src, int srcPos, boolean[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }
}