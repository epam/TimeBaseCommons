package deltix.util.jgoodies;

import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.event.*;

import com.jgoodies.binding.*;
import com.jgoodies.binding.value.*;

import deltix.util.swing.*;

public class FileEditor extends CompositeEditor {

    static final ResourceBundle RB            = ResourceBundle.getBundle ("deltix/util/swing/ui");

    private final Preferences   _prefs;
    private static final String RECENT_FOLDER = "recent_folder";

    public FileEditor (final FileField field,
                       final Preferences prefs) {
        super ();
        _prefs = prefs;
        field.getPathField ().getDocument ().addDocumentListener (new ChangePathHandler ());
        field.addActionListener (new ActionHandler (field));
        if (_prefs != null) {
            final String path = _prefs.get (RECENT_FOLDER,
                                            null);
            if (!BindingUtils.isBlank (path))
                field.fileChooser ().setCurrentDirectory (new File (path));
        }

        _ui = field;

    }

    public FileEditor (final FileField field) {
        this (field,
              null);
    }

    public FileField getFileField () {
        return (FileField) _ui;
    }

    @Override
    public JComponent createUI () {
        return _ui;
    }

    @Override
    public Object getEditorValue () {
        final JTextField pathField = ((FileField) _ui).getPathField ();
        final String path = pathField.getText ();
        return BindingUtils.isBlank (path) ? null : new File (path.trim ());
    }

    @Override
    public void setEditorValue (final Object value) {
        getFileField ().setFile (value instanceof File ? (File) value : null);
    }

    public void prepare (final ValueModel model,
                         final String extension) {

        File f = (File) getEditorValue ();
        if (f == null)
            return;

        if (!f.isDirectory ()) {
            if (!f.exists ()) {
                final String filename = f.getName ();
                final String ext = (filename.lastIndexOf (".") == -1)
                                                                     ? null
                                                                     : filename.substring (filename.lastIndexOf (".") + 1,
                                                                                           filename.length ());

                if (ext == null) {
                    model.setValue (new File (f.getPath () + "." + extension));
                }
            }
            f = f.getParentFile ();
        }

        if (!f.exists ()) {
            final int status = JOptionPane.showOptionDialog (_ui,
                                                             MessageFormat.format (RB.getString ("confirmDirCreation"),
                                                                                   new Object[]
                                                                                {
                                                                                    f.getAbsolutePath ()
                                                                                }),
                                                             RB.getString ("confirmation"),
                                                             JOptionPane.YES_NO_OPTION,
                                                             JOptionPane.QUESTION_MESSAGE,
                                                             null,
                                                             null,
                                                             null);

            if (status == JOptionPane.YES_OPTION) {
                if (!f.mkdirs ())
                    JOptionPane.showMessageDialog (_ui,
                                                   new ParsingException ("notDir",
                                                                         f.getAbsolutePath ()).getLocalizedMessage (),
                                                   SwingUtil.ERROR_TITLE,
                                                   JOptionPane.ERROR_MESSAGE);

            }

        }

    }

    // Event Handling *********************************************************

    private final class ActionHandler implements ActionListener {
        private final FileField _field;

        private ActionHandler (final FileField field) {
            _field = field;
        }

        @Override
        public void actionPerformed (final ActionEvent e) {
            if (e.getID () == FileField.ACTION_FILE_DIALOG) {
                File f = new File (e.getActionCommand ());
                if (!f.isDirectory ())
                    f = f.getParentFile ();

                if (f != null && f.exists ()) {
                    _field.fileChooser ().setCurrentDirectory (f);
                    if (_prefs != null) {
                        _prefs.put (RECENT_FOLDER,
                                    f.getAbsolutePath ());
                    }
                }
                fireStateChanged ();
            }
        }
    }

    private final class ChangePathHandler implements DocumentListener {
        @Override
        public void removeUpdate (final DocumentEvent e) {
            fireStateChanged ();
        }

        @Override
        public void insertUpdate (final DocumentEvent e) {
            fireStateChanged ();
        }

        @Override
        public void changedUpdate (final DocumentEvent e) {
        }
    }
}
