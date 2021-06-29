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
package com.epam.deltix.util.json;

import com.epam.deltix.util.lang.*;

import java.io.*;
import java.lang.reflect.*;

/**
 *
 */
public class ReflectJSON {
    public static void      format (Object obj, PrintStream ps) {
        StringBuilder           sb = new StringBuilder ();
        
        format (obj, sb);
        
        ps.print (sb);
    }
    
    public static void      format (Object obj, Writer wr) throws IOException {
        StringBuilder           text = new StringBuilder ();
        
        format (obj, text);
        
        int             len = text.length ();
        
        for (int ii = 0; ii < len; ii++)
            wr.write (text.charAt (ii));
    }
    
    public static void      format (Object obj, StringBuilder sb) {
        try {
            formatX (obj, sb);
        } catch (IllegalAccessException x) {
            throw new RuntimeException (x);
        }
    }
    
    private static void         hex (int n, StringBuilder out) {
        n = n & 0xF;

        if (n < 10)
            out.append ('0' + n);
        else
            out.append ('A' + n);
    }

    private static void     formatString (CharSequence s, StringBuilder sb) {
        sb.append ('"');
        
        int len = s.length ();
        
        for (int ii = 0; ii < len; ii++) {
            char    ch = s.charAt (ii);
            
            switch (ch) {
                case '\n':  sb.append ("\\n"); break;
                case '\t':  sb.append ("\\t"); break;
                case '\r':  sb.append ("\\r"); break;
                case '"':   sb.append ("\\\""); break;
                case '\\':  sb.append ("\\\\"); break;
                default:
                    if (ch >= 32 && ch <= 0x7F)
                        sb.append (ch);
                    else {
                        sb.append("\\u");
                        hex (ch >>> 12, sb);
                        hex (ch >>> 8, sb);
                        hex (ch >>> 4, sb);
                        hex (ch, sb);
                    }
                    break;
            }
        }
            
        sb.append ('"');
    }
    
    public static void      formatX (Object obj, StringBuilder sb) 
        throws IllegalAccessException 
    {
        if (obj == null) {
            sb.append ("null");
            return;
        }
            
        Class <?>       cls = obj.getClass ();
        
        if (cls.isPrimitive () ||
            obj instanceof Number ||
            obj instanceof Boolean)
        {
            sb.append (obj);
        }        
        else if (obj instanceof CharSequence || cls.isEnum ()) {
            formatString (obj.toString (), sb);
        }
        else if (cls.isArray ()) {
            sb.append ("[ ");
            
            int         len = Array.getLength (obj);
            
            for (int ii = 0; ii < len; ii++) {
                if (ii > 0) 
                    sb.append (',');
                
                formatX (Array.get (obj, ii), sb);
            }
            
            sb.append (" ]");
        }
        else if (obj instanceof Iterable) {
            sb.append ("[ ");
            
            boolean     first = true;
            
            for (Object elem : (Iterable) obj) {
                if (first)
                    first = false;
                else
                    sb.append (',');
                
                formatX (elem, sb);
            }
            
            sb.append (" ]");
        }
        else {
            sb.append ("{ ");

            boolean         first = true;

            for (Field f : obj.getClass ().getFields ()) {
                if ((f.getModifiers () & Modifier.STATIC) != 0)
                    continue;

                if (first)
                    first = false;
                else
                    sb.append (',');

                sb.append ('"');
                StringUtils.escapeJavaString (f.getName (), sb);
                sb.append ("\":");
                formatX (f.get (obj), sb);                
            }

            sb.append (" }");
        }
    }
}