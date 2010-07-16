package deltix.qsrv.hf.tickdb.ui.administrator.panels;

import javax.swing.*;

import deltix.qsrv.hf.tickdb.ui.administrator.util.control.*;
import deltix.qsrv.hf.tickdb.ui.administrator.util.control.beans.*;

public abstract class AbstractUIBean implements UIBean {

    protected JComponent _ui;

    @Override
    public JComponent getUI () {
        if (_ui == null)
            _ui = createUI ();
        return _ui;
    }

}