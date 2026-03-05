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

import com.epam.deltix.util.collections.EmptyEnumeration;
import java.util.Enumeration;

/**
 *
 */
class WordMatcher16 implements WordMatcher {
    static class CodeTooBigException extends Exception { }
    
    private final short []          mCode;
    private final int               mMaxLength;
    
    private int                     getCodeSize (Node node) {
        final int       numBranches = node.length;
        final Node []   branches = node.branches;
        int             size = 3 + numBranches;

        for (int ii = 0; ii < numBranches; ii++) {
            Node    branch = branches [ii];

            if (branch != null)
                size += getCodeSize (branch);
        }

        return (size);
    }
    
    private int                     compile (Node node, int offset) {
        final int       numBranches = node.length;
        final Node []   branches = node.branches;
        
        mCode [offset++] = (short) (node.endOk ? 1 : 0);
        mCode [offset++] = (short) node.base;
        mCode [offset++] = node.length;

        int         endOffset = offset + numBranches;

        for (int ii = 0; ii < numBranches; ii++) {
            Node    branch = branches [ii];

            if (branch != null) {
                mCode [offset++] = (short) endOffset;
                endOffset = compile (branch, endOffset);
            }
            else
                mCode [offset++] = -1;
        }

        return (endOffset);
    }

    WordMatcher16 (Node root, int maxLength) throws CodeTooBigException {
        int         size = getCodeSize (root);
        
        if (size >= 0xFFFF)
            throw new CodeTooBigException ();
        
        mCode = new short [size];
        mMaxLength = maxLength;
        
        compile (root, 0);
    }
    
    public boolean  matches (final byte [] bytes, int offset, int len) {
        int             codeIdx = 0;
        
        for (;;) {
            if (len == 0)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1] & 0xFFFF;
            final int   jump = bytes [offset] - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2] & 0xFFFF;
            
            if (jump >= jtl)
                return (false);
            
            offset++;
            len--;
            
            codeIdx = mCode [codeIdx + 3 + jump];
            
            if (codeIdx == -1)
                return (false);
            
            codeIdx = codeIdx & 0xFFFF;
        }        
    }

    public boolean          matches (CharSequence s) {
        return (matches (s, 0, s.length ()));
    }
    
    public boolean          matches (CharSequence s, int offset, int len) {
        int             codeIdx = 0;
        
        for (;;) {
            if (len == 0)
                return (mCode [codeIdx] != 0);
            
            final int   base = mCode [codeIdx + 1] & 0xFFFF;
            final int   jump = s.charAt (offset) - base;
            
            if (jump < 0)
                return (false);
            
            final int   jtl = mCode [codeIdx + 2] & 0xFFFF;
            
            if (jump >= jtl)
                return (false);
            
            offset++;
            len--;
            
            codeIdx = mCode [codeIdx + 3 + jump];
            
            if (codeIdx == -1)
                return (false);
            
            codeIdx = codeIdx & 0xFFFF;
        }        
    }

    public Enumeration <CharSequence>     vocabulary () {
        if (mMaxLength < 0)
            return (new EmptyEnumeration <CharSequence> ());
        
        return (new VocabularyEnumeration16 (mCode, mMaxLength));
    }    
}