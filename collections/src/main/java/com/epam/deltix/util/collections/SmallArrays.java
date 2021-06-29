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
 * Fast index getter for small arrays
 */
public class SmallArrays {

    public static<T> int indexOf(T obj, T[] elements) {
        return indexOf(obj, elements, elements.length);
    }

    public static<T> int indexOf(T obj, T[] elements, int length) {

        switch (length) {
            default:
                for (int code = length - 1; code >= 7; code--)
                    if (obj.equals(elements [code]))
                        return (code);

            case 7:     if (obj.equals(elements[6])) return (6);  // else fall-through
            case 6:     if (obj.equals(elements[5])) return (5);  // else fall-through
            case 5:     if (obj.equals(elements[4])) return (4);  // else fall-through
            case 4:     if (obj.equals(elements[3])) return (3);  // else fall-through
            case 3:     if (obj.equals(elements[2])) return (2);  // else fall-through
            case 2:     if (obj.equals(elements[1])) return (1);  // else fall-through
            case 1:     if (obj.equals(elements[0])) return (0);  // else fall-through
            case 0:     break;
        }

        return -1;
    }
}