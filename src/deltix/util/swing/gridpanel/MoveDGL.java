package deltix.util.swing.gridpanel;

import deltix.util.swing.LocalObjectTransferable;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;

class MoveDGL extends DragSourceAdapter implements DragGestureListener {
    
    public MoveDGL () {
    }
/*
    @Override
    public void     dragDropEnd (DragSourceDropEvent e) {
        
        
    }

    @Override
    public void     dragOver (DragSourceDragEvent e) {    
        
        Point               p = e.getLocation ();
                
    }
    */
    
    @Override
    public void     dragExit (DragSourceEvent e) {
        e.getDragSourceContext ().setCursor (DragSource.DefaultMoveNoDrop); 
    }

    @Override
    public void     dragEnter (DragSourceDragEvent e) {
        e.getDragSourceContext ().setCursor (DragSource.DefaultMoveDrop);    
    }
        
    public void     dragGestureRecognized (DragGestureEvent e) {
        Transferable        transferable = 
            new LocalObjectTransferable <Component> (e.getComponent (), Component.class);
                
        e.startDrag (DragSource.DefaultMoveNoDrop, transferable, this);
    }
}
