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
package com.epam.deltix.util.text;

import com.epam.deltix.util.lang.Util;
import java.util.Arrays;

/**
 *
 */
class Node {
    private static final int        INIT_CAPACITY = 16;
        
    boolean         endOk = false;
    char            base;
    short           length = 0;
    Node []         branches = null;

    void            dump (String indent) {
        if (endOk)
            System.out.println (indent + "<end>");

        if (length > 0) {
            String      indent2 = indent + " ";

            for (int ii = 0; ii < length; ii++) {
                Node    branch = branches [ii];

                if (branch != null) {
                    System.out.println (indent + "'" + (char) (base + ii) + "':");
                    branch.dump (indent2);
                }
            }
        }
    }

    private void    appendSpace (int num) {
        int             curArrayLength = branches.length;
        int             newLength = length + num;

        if (curArrayLength < newLength) {
            Node []     more = 
                new Node [Util.doubleUntilAtLeast (curArrayLength, newLength)];

            System.arraycopy (branches, 0, more, 0, length);
            branches = more;                
        }             

        length = (short) newLength;
    }

    private void    prependSpace (int num) {
        int             curArrayLength = branches.length;
        int             newLength = length + num;

        if (curArrayLength >= newLength) {
            System.arraycopy (branches, 0, branches, num, length);  
            Arrays.fill (branches, 0, num, null);
        }
        else {
            Node []     more = 
                new Node [Util.doubleUntilAtLeast (curArrayLength, newLength)];

            System.arraycopy (branches, 0, more, num, length);
            branches = more;                
        }                

        length = (short) newLength;
    }

    boolean         match (byte [] bytes, int offset, int len) {
        if (len == 0) 
            return (endOk);

        if (length == 0)
            return (false);

        int     idx = bytes [offset] - base;

        if (idx < 0 || idx >= length)
            return (false);

        Node        branch = branches [idx];
        
        if (branch == null)
            return (false);
        
        return (branch.match (bytes, offset + 1, len - 1));
    }

    boolean         match (CharSequence s, int pos, int len) {
        if (len == 0) 
            return (endOk);

        if (length == 0)
            return (false);

        int     idx = s.charAt (pos) - base;

        if (idx < 0 || idx >= length)
            return (false);

        Node        branch = branches [idx];
        
        if (branch == null)
            return (false);
        
        return (branch.match (s, pos + 1, len - 1));
    }

    void            add (CharSequence s, int pos) {
        if (pos == s.length ()) {
            endOk = true;
            return;
        }

        char        ch = s.charAt (pos);
        int         idx;

        if (length == 0) {
            branches = new Node [INIT_CAPACITY];
            base = ch;
            idx = 0;
            length = 1;
        }
        else {
            idx = ch - base;

            if (idx >= 0) {
                int     ext = idx + 1 - length;

                if (ext > 0)
                    appendSpace (ext);
            }
            else {
                base += idx;
                prependSpace (-idx);
                idx = 0;
            }
        }

        Node        branch = branches [idx];

        if (branch == null) 
            branches [idx] = branch = new Node ();

        branch.add (s, pos + 1);
    }

    int         getCodeSize () {
        int         size = 3 + length;

        for (int ii = 0; ii < length; ii++) {
            Node    branch = branches [ii];

            if (branch != null)
                size += branch.getCodeSize ();
        }

        return (size);
    }

    int         buildCode32 (int [] code, int offset) {
        code [offset++] = endOk ? 1 : 0;
        code [offset++] = base;
        code [offset++] = length;

        int         endOffset = offset + length;

        for (int ii = 0; ii < length; ii++) {
            Node    branch = branches [ii];

            if (branch != null) {
                code [offset++] = endOffset;
                endOffset = branch.buildCode32 (code, endOffset);
            }
            else
                code [offset++] = -1;
        }

        return (endOffset);
    }

    int         buildCode16 (short [] code, int offset) {
        code [offset++] = (short) (endOk ? 1 : 0);
        code [offset++] = (short) base;
        code [offset++] = length;

        int         endOffset = offset + length;

        for (int ii = 0; ii < length; ii++) {
            Node    branch = branches [ii];

            if (branch != null) {
                code [offset++] = (short) endOffset;
                endOffset = branch.buildCode16 (code, endOffset);
            }
            else
                code [offset++] = -1;
        }

        return (endOffset);
    }
}