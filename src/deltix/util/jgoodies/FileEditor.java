package deltix.util.jgoodies;

import java.io.*;

import javax.swing.*;

import org.springframework.util.*;

import deltix.util.swing.*;

public class FileEditor extends CompositeEditor {

    public FileEditor (FileField f) {
        super ();
        _ui = f;
    }

    @Override
    public JComponent createUI () {
        return _ui;
    }

    @Override
    public Object getEditorValue () {
        JTextField mPathField = ((FileField) _ui).getPathField ();
        return new File (mPathField.getText ().trim ());
    }

    @Override
    public void setEditorValue (Object value) {
        Assert.isInstanceOf (File.class,
                             value);
        ((FileField) _ui).setFile ((File) value);
    }

}
