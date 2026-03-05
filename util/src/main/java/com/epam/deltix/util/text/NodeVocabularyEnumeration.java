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

import java.util.Enumeration;

class NodeVocabularyEnumeration 
    implements Enumeration<CharSequence> 
{
    private StringBuilder       mStringBuilder;
    private Node []             mPath;
    private int []              mPositions;
    private int                 mDepth;

    NodeVocabularyEnumeration (Node root, int maxLength) {
        mStringBuilder = new StringBuilder (maxLength);
        mPath = new Node [maxLength + 1];
        mPositions = new int [maxLength + 1];
        
        mPath [0] = root;
        mPositions [0] = -2;
        mDepth = 0;

        findNext ();
    }

    public boolean          hasMoreElements () {
        return mDepth >= 0;
    }

    public CharSequence     nextElement () {
        mStringBuilder.setLength (0);
        
        for (int ii = 0; ii < mDepth; ii++)
            mStringBuilder.append ((char) (mPath [ii].base + mPositions [ii]));
        
        findNext ();
        
        return (mStringBuilder);
    }

    private void            findNext () {
        for (;;) {
            Node        node = mPath [mDepth];
            int         pos = ++mPositions [mDepth];

            if (pos == -1) {
                if (node.endOk)
                    return;     // Got one
            }
            else if (pos == node.length) {   // Up
                mDepth--;

                if (mDepth == -1)
                    break;                
            }
            else {
                //  Down
                Node        downNode = node.branches [pos]; 
                
                if (downNode == null)
                    continue;
                
                mDepth++;
                mPath [mDepth] = downNode;
                mPositions [mDepth] = -2;
            }
        }
    }
}