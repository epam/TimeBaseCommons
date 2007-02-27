package deltix.qsrv.ui.util;

import sun.awt.dnd.SunDragSourceContextPeer;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.dnd.DragSource;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: PaharelauK
 * Date: Feb 27, 2007
 * Time: 4:15:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultDragGestureRecognizer extends MouseAdapter implements MouseMotionListener {

    private MouseEvent firstMouseEvent = null;

    private static int getMotionThreshold() {
        return DragSource.getDragThreshold();
    }

    public void mousePressed(MouseEvent e) {
        firstMouseEvent = e;
        e.consume();
    }

    public void mouseReleased(MouseEvent e) {
        firstMouseEvent = null;
    }

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

    public void mouseMoved(MouseEvent e) {
    }


    /**
     * from javax.swing.plaf.basic.BasicDragGestureRecognizer
     */
    protected int mapDragOperationFromModifiers(MouseEvent e) {
        int mods = e.getModifiersEx();

        if ((mods & InputEvent.BUTTON1_DOWN_MASK) != InputEvent.BUTTON1_DOWN_MASK) {
            return TransferHandler.NONE;
        }

        JComponent c = getComponent(e);
        TransferHandler th = c.getTransferHandler();
        return SunDragSourceContextPeer.convertModifiersToDropAction(mods, th.getSourceActions(c));
    }

    protected JComponent getComponent(MouseEvent e) {
        Object src = e.getSource();
        if (src instanceof JComponent) {
            JComponent c = (JComponent) src;
            return c;
        }
        return null;
    }
}
