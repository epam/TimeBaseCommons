package deltix.util.jgoodies;

import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.common.base.Strings;
import deltix.util.swing.FileField;
import deltix.util.swing.ParsingException;
import deltix.util.swing.SwingUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class FileEditor extends CompositeEditor {

            static final ResourceBundle RB            = ResourceBundle.getBundle ("deltix/util/swing/ui");

    private        final Preferences    prefs;
    private static final String         RECENT_FOLDER = "recent_folder";

    public FileEditor (final FileField field,
                       final Preferences prefs) {
        super ();
        this.prefs = prefs;
        field.getPathField ().getDocument ().addDocumentListener (new ChangePathHandler ());
        field.addActionListener (new ActionHandler (field));
        if (this.prefs != null) {
            final String path = this.prefs.get (RECENT_FOLDER,
                                                null);
            if (!Strings.isBlank(path))
                field.fileChooser ().setCurrentDirectory (new File (path));
        }

        field.addActionListener (new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (FileField.ACTION_FILE_DIALOG == e.getID () && field.getValidationMode () == FileField.VALID_EXISTING_FILE) {
                    try {

                        String ext = extension ();
                        if (!Strings.isEmpty (ext)) {
                            String path = field.getPathField().getText ().trim ();
                            if (!Strings.isEmpty (path)) {
                                if (path.lastIndexOf (".") == -1) {
                                    path += "." + ext;
                                    setEditorValue (new File (path));
                                }
                            }
                        }

                    } catch (Throwable x) {
                        //nothing to do
                    }
                }
            }
        });

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
        return Strings.isBlank (path) ? null : new File (path.trim ());
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

        if (f != null && !f.exists ()) {
            final int status = JOptionPane.showOptionDialog (_ui,
                                                             MessageFormat.format (
                                                                     RB.getString ("confirmDirCreation"),
                                                                     f.getAbsolutePath ()),
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

    protected String extension () {
        JFileChooser fileChooser = ((FileField) _ui).fileChooser ();
        final FileFilter[] filters = fileChooser.getChoosableFileFilters ();

        for (FileFilter filter : filters) {
            if (filter instanceof FileNameExtensionFilter) {
                return ((FileNameExtensionFilter) filter).getExtensions ()[0];
            }
        }

        return null;

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
                    if (prefs != null) {
                        prefs.put (RECENT_FOLDER,
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
