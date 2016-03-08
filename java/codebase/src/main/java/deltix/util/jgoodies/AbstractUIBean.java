package deltix.util.jgoodies;

import javax.swing.*;

public abstract class AbstractUIBean implements UIBean {

    protected JComponent _ui;

    @Override
    public JComponent getUI () {
        if (_ui == null)
            _ui = createUI ();
        return _ui;
    }

}