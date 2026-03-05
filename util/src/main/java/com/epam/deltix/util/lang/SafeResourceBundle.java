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
package com.epam.deltix.util.lang;

import com.epam.deltix.util.collections.EmptyEnumeration;
import java.util.*;

/**
 *
 */
public class SafeResourceBundle {
    public static ResourceBundle    getBundle (Class cls, String rbname) {
        String      s = cls.getName ();
        String      n = cls.getSimpleName ();

        assert s.endsWith (n);

        return (getBundle (s.substring (0, s.length () - n.length ()) + rbname));
    }

    public static ResourceBundle    getBundle (String baseName) {
        try {
            return (ResourceBundle.getBundle (baseName));
        } catch (MissingResourceException x) {
            return (
                new ResourceBundle () {

                    @Override
                    public Enumeration <String> getKeys () {
                        return (new EmptyEnumeration <String> ());
                    }

                    @Override
                    protected Object handleGetObject (String key) {
                        return (key);
                    }                
                }
            );
        }
    }

    public static String            getString (ResourceBundle rb, String key) {
        if (rb == null)
            return (key);
        else
            try {
                return (rb.getString (key));
            } catch (MissingResourceException x) {
                return (key);
            }
    }
}