package deltix.util.swing.treeedit;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import deltix.util.swing.*;

class NodeRenderer extends DefaultTreeCellRenderer {
    private Font            mDefaultFont = getFont ();
    
    public Component    getTreeCellRendererComponent (
        JTree               jTree, 
        Object              node, 
        boolean             selected,
        boolean             expanded,
        boolean             leaf,
        int                 row,
        boolean             hasFocus
    ) 
    {
        JLabel              label = (JLabel) 
            super.getTreeCellRendererComponent (
                jTree, 
                node, 
                selected,
                expanded,
                leaf,
                row,
                hasFocus
            );

        if (node instanceof NodeAdapter){

            String tootip = (((NodeAdapter)node).getUserNode()).getTooltip();
            if (tootip != null)
                setToolTipText(tootip);
        }
        
        /**
         *  Reset font because it may have been tweaked
         */
        label.setFont (mDefaultFont);
        
        NodeAdapter         anode = (NodeAdapter) node;
        
        return (anode.getUserNode ().render (selected, hasFocus, label));
    }    
}
