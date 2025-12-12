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
package com.epam.deltix.util.os;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

@SuppressWarnings("unused")
public class WindowsRegistry {
    // inspired by
    // http://javabyexample.wisdomplug.com/java-concepts/34-core-java/62-java-registry-wrapper.html
    // http://www.snipcode.org/java/1-java/23-java-class-for-accessing-reading-and-writing-from-windows-registry.html
    // http://snipplr.com/view/6620/accessing-windows-registry-in-java/
    public static final int                     HKEY_CURRENT_USER  = 0x80000001;
    public static final int                     HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int                     REG_SUCCESS        = 0;
    public static final int                     REG_NOTFOUND       = 2;
    public static final int                     REG_ACCESSDENIED   = 5;

    private static final int                    KEY_ALL_ACCESS     = 0xf003f;
    private static final int                    KEY_READ           = 0x20019;
    private static final Preferences                  userRoot     = Preferences.userRoot ( );
    private static final Preferences                  systemRoot   = Preferences.systemRoot ( );
    private static final Class<? extends Preferences> userClass    = userRoot.getClass ( );
    private static Method                       regOpenKey         = null;
    private static Method                       regCloseKey        = null;
    private static Method                       regQueryValueEx    = null;
    private static Method                       regEnumValue       = null;
    private static Method                       regQueryInfoKey    = null;
    private static Method                       regEnumKeyEx       = null;
    private static Method                       regCreateKeyEx     = null;
    private static Method                       regSetValueEx      = null;
    private static Method                       regDeleteKey       = null;
    private static Method                       regDeleteValue     = null;

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod ( "WindowsRegOpenKey",
                    int.class,
                    byte[].class,
                    int.class);
            regOpenKey.setAccessible ( true );
            regCloseKey = userClass.getDeclaredMethod ( "WindowsRegCloseKey",
                    int.class);
            regCloseKey.setAccessible ( true );
            regQueryValueEx = userClass.getDeclaredMethod ( "WindowsRegQueryValueEx",
                    int.class,
                    byte[].class);
            regQueryValueEx.setAccessible ( true );
            regEnumValue = userClass.getDeclaredMethod ( "WindowsRegEnumValue",
                    int.class,
                    int.class,
                    int.class);
            regEnumValue.setAccessible ( true );
            regQueryInfoKey = userClass.getDeclaredMethod ( "WindowsRegQueryInfoKey1",
                    int.class);
            regQueryInfoKey.setAccessible ( true );
            regEnumKeyEx = userClass.getDeclaredMethod (
                                                         "WindowsRegEnumKeyEx",
                    int.class,
                    int.class,
                    int.class);
            regEnumKeyEx.setAccessible ( true );
            regCreateKeyEx = userClass.getDeclaredMethod (
                                                           "WindowsRegCreateKeyEx",
                    int.class,
                    byte[].class);
            regCreateKeyEx.setAccessible ( true );
            regSetValueEx = userClass.getDeclaredMethod (
                                                          "WindowsRegSetValueEx",
                    int.class,
                    byte[].class,
                    byte[].class);
            regSetValueEx.setAccessible ( true );
            regDeleteValue = userClass.getDeclaredMethod (
                                                           "WindowsRegDeleteValue",
                    int.class,
                    byte[].class);
            regDeleteValue.setAccessible ( true );
            regDeleteKey = userClass.getDeclaredMethod (
                                                         "WindowsRegDeleteKey",
                    int.class,
                    byte[].class);
            regDeleteKey.setAccessible ( true );
        } catch (final Exception e) {
            e.printStackTrace ( );
        }
    }

    private WindowsRegistry ( ) {
    }

    /**
     * Read a value from key and value name
     */
    public static String readString ( final int hkey,
                                      final String key,
                                      final String valueName )
                                                              throws IllegalArgumentException,
                                                              IllegalAccessException,
                                                              InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readString ( systemRoot,
                                hkey,
                                key,
                                valueName );
        } else if (hkey == HKEY_CURRENT_USER) {
            return readString ( userRoot,
                                hkey,
                                key,
                                valueName );
        } else {
            throw new IllegalArgumentException ( "hkey=" + hkey );
        }
    }

    /**
     * Read value(s) and value name(s) form given key
     */
    public static Map<String, String> readStringValues ( final int hkey,
                                                         final String key )
                                                                           throws IllegalArgumentException,
                                                                           IllegalAccessException,
                                                                           InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringValues ( systemRoot,
                                      hkey,
                                      key );
        } else if (hkey == HKEY_CURRENT_USER) {
            return readStringValues ( userRoot,
                                      hkey,
                                      key );
        } else {
            throw new IllegalArgumentException ( "hkey=" + hkey );
        }
    }

    /**
     * Read the value name(s) from a given key
     */
    public static List<String> readStringSubKeys ( final int hkey,
                                                   final String key )
                                                                     throws IllegalArgumentException,
                                                                     IllegalAccessException,
                                                                     InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            return readStringSubKeys ( systemRoot,
                                       hkey,
                                       key );
        } else if (hkey == HKEY_CURRENT_USER) {
            return readStringSubKeys ( userRoot,
                                       hkey,
                                       key );
        } else {
            throw new IllegalArgumentException ( "hkey=" + hkey );
        }
    }

    /**
     * Create a key
     */
    public static void createKey ( final int hkey,
                                   final String key )
                                                     throws IllegalArgumentException,
                                                     IllegalAccessException,
                                                     InvocationTargetException {
        int[] ret;
        if (hkey == HKEY_LOCAL_MACHINE) {
            ret = createKey ( systemRoot,
                              hkey,
                              key );
            regCloseKey.invoke ( systemRoot, ret[0]);
        } else if (hkey == HKEY_CURRENT_USER) {
            ret = createKey ( userRoot,
                              hkey,
                              key );
            regCloseKey.invoke ( userRoot, ret[0]);
        } else {
            throw new IllegalArgumentException ( "hkey=" + hkey );
        }
        if (ret[1] != REG_SUCCESS) {
            throw new IllegalArgumentException ( "rc=" + ret[1] + "  key=" + key );
        }
    }

    /**
     * Write a value in a given key/value name
      */
    public static void writeStringValue ( final int hkey,
                                          final String key,
                                          final String valueName,
                                          final String value )
                                                              throws IllegalArgumentException,
                                                              IllegalAccessException,
                                                              InvocationTargetException {
        if (hkey == HKEY_LOCAL_MACHINE) {
            writeStringValue ( systemRoot,
                               hkey,
                               key,
                               valueName,
                               value );
        } else if (hkey == HKEY_CURRENT_USER) {
            writeStringValue ( userRoot,
                               hkey,
                               key,
                               valueName,
                               value );
        } else {
            throw new IllegalArgumentException ( "hkey=" + hkey );
        }
    }

    /**
     * Delete a given key
     */
    public static void deleteKey ( final int hkey,
                                   final String key )
                                                     throws IllegalArgumentException,
                                                     IllegalAccessException,
                                                     InvocationTargetException {
        int rc = -1;
        if (hkey == HKEY_LOCAL_MACHINE) {
            rc = deleteKey ( systemRoot,
                             hkey,
                             key );
        } else if (hkey == HKEY_CURRENT_USER) {
            rc = deleteKey ( userRoot,
                             hkey,
                             key );
        }
        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException ( "rc=" + rc + "  key=" + key );
        }
    }

    /**
     * delete a value from a given key/value name
     */
    public static void deleteValue ( final int hkey,
                                     final String key,
                                     final String value )
                                                         throws IllegalArgumentException,
                                                         IllegalAccessException,
                                                         InvocationTargetException {
        int rc = -1;
        if (hkey == HKEY_LOCAL_MACHINE) {
            rc = deleteValue ( systemRoot,
                               hkey,
                               key,
                               value );
        } else if (hkey == HKEY_CURRENT_USER) {
            rc = deleteValue ( userRoot,
                               hkey,
                               key,
                               value );
        }
        if (rc != REG_SUCCESS) {
            throw new IllegalArgumentException ( "rc=" + rc + "  key=" + key + "  value=" + value );
        }
    }

    // =====================

    private static int deleteValue ( final Preferences root,
                                     final int hkey,
                                     final String key,
                                     final String value )
                                                         throws IllegalArgumentException,
                                                         IllegalAccessException,
                                                         InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke ( root,
                                                          new Object[]
                                                          {
                                                                  hkey,
                                                                  toCstr ( key ),
                                                                  KEY_ALL_ACCESS
                                                          } );
        if (handles[1] != REG_SUCCESS) {
            return handles[1]; // can be REG_NOTFOUND, REG_ACCESSDENIED
        }
        final int rc = (Integer) regDeleteValue.invoke(root,
                new Object[]
                        {
                                handles[0],
                                toCstr(value)
                        });
        regCloseKey.invoke ( root,
                handles[0]);
        return rc;
    }

    private static int deleteKey ( final Preferences root,
                                   final int hkey,
                                   final String key )
                                                     throws IllegalArgumentException,
                                                     IllegalAccessException,
                                                     InvocationTargetException {
        return (int) (Integer) regDeleteKey.invoke(root, new Object[]
                {
                        hkey,
                        toCstr(key)
                }); // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    }

    private static String readString ( final Preferences root,
                                       final int hkey,
                                       final String key,
                                       final String value )
                                                           throws IllegalArgumentException,
                                                           IllegalAccessException,
                                                           InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke ( root,
                                                          new Object[]
                                                          {
                                                                  hkey,
                                                                  toCstr ( key ),
                                                                  KEY_READ
                                                          } );
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        final byte[] valb = (byte[]) regQueryValueEx.invoke ( root,
                                                              new Object[]
                                                              {
                                                                      handles[0],
                                                                      toCstr ( value )
                                                              } );
        regCloseKey.invoke ( root,
                handles[0]);
        return (valb != null ? new String ( valb ).trim ( ) : null);
    }

    private static Map<String, String> readStringValues ( final Preferences root,
                                                          final int hkey,
                                                          final String key )
                                                                            throws IllegalArgumentException,
                                                                            IllegalAccessException,
                                                                            InvocationTargetException {
        final HashMap<String, String> results = new HashMap<>();
        final int[] handles = (int[]) regOpenKey.invoke ( root,
                                                          new Object[]
                                                          {
                                                                  hkey,
                                                                  toCstr ( key ),
                                                                  KEY_READ
                                                          } );
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        final int[] info = (int[]) regQueryInfoKey.invoke ( root,
                                                            new Object[]
                                                            {
                                                                    handles[0]
                                                            } );

        final int count = info[2]; // count
        final int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            final byte[] name = (byte[]) regEnumValue.invoke ( root,
                                                               new Object[]
                                                               {
                                                                       handles[0],
                                                                       index,
                                                                       maxlen + 1
                                                               } );
            final String value = readString ( hkey,
                                              key,
                                              new String ( name ) );
            results.put ( new String ( name ).trim ( ),
                          value );
        }
        regCloseKey.invoke ( root,
                handles[0]);
        return results;
    }

    private static List<String> readStringSubKeys ( final Preferences root,
                                                    final int hkey,
                                                    final String key )
                                                                      throws IllegalArgumentException,
                                                                      IllegalAccessException,
                                                                      InvocationTargetException {
        final List<String> results = new ArrayList<>();
        final int[] handles = (int[]) regOpenKey.invoke ( root,
                                                          new Object[]
                                                          {
                                                                  hkey,
                                                                  toCstr ( key ),
                                                                  KEY_READ
                                                          } );
        if (handles[1] != REG_SUCCESS) {
            return null;
        }
        final int[] info = (int[]) regQueryInfoKey.invoke ( root,
                                                            new Object[]
                                                            {
                                                                    handles[0]
                                                            } );

        final int count = info[2]; // count
        final int maxlen = info[3]; // value length max
        for (int index = 0; index < count; index++) {
            final byte[] name = (byte[]) regEnumKeyEx.invoke ( root,
                                                               new Object[]
                                                               {
                                                                       handles[0],
                                                                       index,
                                                                       maxlen + 1
                                                               } );
            results.add ( new String ( name ).trim ( ) );
        }
        regCloseKey.invoke ( root,
                handles[0]);
        return results;
    }

    private static int[] createKey ( final Preferences root,
                                     final int hkey,
                                     final String key )
                                                       throws IllegalArgumentException,
                                                       IllegalAccessException,
                                                       InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke ( root,
                                               new Object[]
                                               {
                                                       hkey,
                                                       toCstr ( key )
                                               } );
    }

    private static void writeStringValue ( final Preferences root,
                                           final int hkey,
                                           final String key,
                                           final String valueName,
                                           final String value )
                                                               throws IllegalArgumentException,
                                                               IllegalAccessException,
                                                               InvocationTargetException {
        final int[] handles = (int[]) regOpenKey.invoke ( root,
                                                          new Object[]
                                                          {
                                                                  hkey,
                                                                  toCstr ( key ),
                                                                  KEY_ALL_ACCESS
                                                          } );

        regSetValueEx.invoke ( root,
                handles[0],
                toCstr ( valueName ),
                toCstr ( value ));
        regCloseKey.invoke ( root,
                handles[0]);
    }

    // utility
    private static byte[] toCstr ( final String str ) {
        final byte[] result = new byte[str.length ( ) + 1];

        for (int i = 0; i < str.length ( ); i++) {
            result[i] = (byte) str.charAt ( i );
        }
        result[str.length ( )] = 0;
        return result;
    }

    public static void main ( final String[] args) throws IllegalArgumentException,
                                                   IllegalAccessException,
                                                   InvocationTargetException {
        System.out.println ( WindowsRegistry.readString ( HKEY_LOCAL_MACHINE,
                                                          "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders",
                                                          "Common Desktop" ) );
        System.out.println ( WindowsRegistry.readString ( HKEY_LOCAL_MACHINE,
                                                          "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders",
                                                          "Common Start Menu" ) );
    }
}
