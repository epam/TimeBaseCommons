package deltix.util.swing;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 */
public class DefaultDragGestureRecognizer extends MouseAdapter implements MouseMotionListener {

    private MouseEvent firstMouseEvent = null;

    private static int getMotionThreshold() {
        return DragSource.getDragThreshold();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        firstMouseEvent = e;
        e.consume();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        firstMouseEvent = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (firstMouseEvent != null) {
            e.consume();

            int action = mapDragOperationFromModifiers(e);

            if (action == TransferHandler.NONE) {
                return;
            }

            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            if ((dx > getMotionThreshold()) || (dy > getMotionThreshold())) {
                // start transfer... shouldn't be a click at this point
                JComponent c = getComponent(e);
                TransferHandler th = c.getTransferHandler();
                th.exportAsDrag(c, firstMouseEvent, action);
                firstMouseEvent = null;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }


    //from javax.swing.plaf.basic.BasicDragGestureRecognizer
    protected int mapDragOperationFromModifiers(MouseEvent e) {
        int mods = e.getModifiersEx();

        if ((mods & InputEvent.BUTTON1_DOWN_MASK) != InputEvent.BUTTON1_DOWN_MASK) {
            return TransferHandler.NONE;
        }

        JComponent c = getComponent(e);
        TransferHandler th = c.getTransferHandler();
        return convertModifiersToDropAction(mods, th.getSourceActions(c));
    }

    protected JComponent getComponent(MouseEvent e) {
        Object src = e.getSource();
        if (src instanceof JComponent) {
            return (JComponent) src;
        }
        return null;
    }

    private static int convertModifiersToDropAction(int modifiers,
               int sourceActions)
       {
            int k = 0;
            switch (modifiers & (InputEvent.SHIFT_DOWN_MASK |
                    InputEvent.CTRL_DOWN_MASK)) {
                 case InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK:
                      k = DnDConstants.ACTION_LINK;
                      break;
                 case InputEvent.CTRL_DOWN_MASK:
                      k = DnDConstants.ACTION_COPY;
                      break;
                 case InputEvent.SHIFT_DOWN_MASK:
                      k = DnDConstants.ACTION_MOVE;
                      break;
                 // without a modifier
                 default:
                      if ((sourceActions & DnDConstants.ACTION_MOVE) != 0) {
                           k = DnDConstants.ACTION_MOVE;
                           break;
                      }
                      else if ((sourceActions & DnDConstants.ACTION_COPY) != 0) {
                           k = DnDConstants.ACTION_COPY;
                           break;
                      }
                      else if ((sourceActions & DnDConstants.ACTION_LINK) != 0)
                           k = DnDConstants.ACTION_LINK;


            }
            return (k & sourceActions);
       }

}
