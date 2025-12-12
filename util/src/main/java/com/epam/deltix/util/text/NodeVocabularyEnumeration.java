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
