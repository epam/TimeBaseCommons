package deltix.util.swing;

import java.awt.*;
import javax.swing.JTextPane;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 */
public class HTMLPane extends JTextPane {
    public HTMLPane () {
        setContentType ("text/html");
    }

    public void insert (String msg) {
        HTMLDocument            doc = (HTMLDocument) getDocument ();
        HTMLEditorKit           ek = (HTMLEditorKit) getEditorKit ();

        try {
            ek.insertHTML (doc, doc.getLength (), msg, 0, 0, null);
        } catch (Exception unexp) {
            throw new RuntimeException (unexp);
        }
    }

    public void setFontAndColor (Font font, Color c) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, c);

        // Retrieve the pane's document object
        StyledDocument doc = getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }

}
