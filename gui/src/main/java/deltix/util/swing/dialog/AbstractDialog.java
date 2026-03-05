package deltix.util.swing.dialog;

import deltix.util.swing.SwingUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * User: turskiys
 * Date: 7/2/12
 */
public abstract class AbstractDialog extends JDialog implements ICloseDialog {
    static final ResourceBundle RB              = ResourceBundle.getBundle("deltix/util/swing/ui");
    public static final String         CANCEL_BTN_TEXT = RB.getString("form.abstract.dialog.btn.cancel");

    protected static int       MIN_WIDTH        = 300;
    protected static int       MIN_HEIGHT       = 200;
    protected static int       PREFFERED_WIDTH  = 450;
    protected static int       PREFFERED_HEIGHT = 300;

    protected        Component ui;

    protected java.util.List<ChangeListener> listenerList = new ArrayList<ChangeListener>();

    protected       AbstractAction mainAction;

    protected final AbstractAction cancelAction = new AbstractAction(CANCEL_BTN_TEXT) {
        @Override
        public void actionPerformed(final ActionEvent e) {
            dispose();
        }
    };

    public AbstractDialog(final Window owner, String name, String title){
        this(owner, name, title, RB.getString("form.abstract.dialog.btn.ok"));
    }

    public AbstractDialog(final Window owner, String name, String title, String actionButtonTitle){
        super(owner, title);
        mainAction = new AbstractAction(actionButtonTitle) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                processMainAction();
            }
        };
        setName(name);
    }


    protected void initializeGUI() {
        //layout setting
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        getContentPane().setLayout(new GridBagLayout());
        add(ui = createUI(), gc);

        //button bar
        gc.gridx++;
        gc.weightx = 1;
        gc.weighty = 0;
        gc.insets = new Insets(10, 5, 10, 5);
        gc.fill = GridBagConstraints.CENTER;
        add(createButtonPanel(), gc);

        //key stroke for buttons Enter and Escape to close window
/*        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enter, "ENTER");
        getRootPane().getActionMap().put("ENTER", mainAction);*/
        SwingUtil.setEscapeHandler(this, cancelAction);

        setMinimumSize(new Dimension(MIN_WIDTH,
                MIN_HEIGHT));
        setPreferredSize(new Dimension(PREFFERED_WIDTH,
                PREFFERED_HEIGHT));
    }


    public void addChangeListener(ChangeListener listener)
    {
        listenerList.add(listener);
    }

    protected void notifyListeners()
    {
        ChangeEvent ce = new ChangeEvent(this);
        for (ChangeListener listener : listenerList)
        {
            listener.stateChanged(ce);
        }
    }

    public void  processMainAction(){
        boolean successful = process();
        if (successful){
            notifyListeners();
            dispose();
        }
    }

    @Override
    public void close() {
        dispose();
    }

    public abstract boolean process();

    public abstract JPanel createButtonPanel();

    public abstract Component createUI();

}
