package deltix.util.jgoodies;

import java.awt.*;

import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jidesoft.swing.*;

public class JideComponentFactory2 extends DefaultComponentFactory {

    /**
     * Holds the single instance of this class.
     */
    private static final JideComponentFactory2 INSTANCE = new JideComponentFactory2 ();

    public static JideComponentFactory2 getInstance () {
        return INSTANCE;
    }

    @Override
    public JComponent createSeparator (final JLabel label) {
        return new TitledSeparator (label,
                                    TitledSeparator.TYPE_PARTIAL_ETCHED,
                                    SwingConstants.LEFT);
    }

    @Override
    public JComponent createSeparator (final String textWithMnemonic,
                                       final int alignment) {
        final StyledLabel label = new StyledLabel ();
        DefaultComponentFactory.setTextAndMnemonic (label,
                                                    textWithMnemonic);

        label.setHorizontalAlignment (alignment);
        label.addStyleRange (new StyleRange (Font.BOLD,
                                             Color.BLACK));

        return new TitledSeparator (label,
                                    TitledSeparator.TYPE_PARTIAL_ETCHED,
                                    SwingConstants.LEFT);
    }

    @Override
    public JComponent createSeparator (final String textWithMnemonic) {
        return createSeparator (textWithMnemonic,
                                SwingConstants.LEFT);
    }

    @Override
    public JLabel createLabel (final String textWithMnemonic) {
        final StyledLabel label = new StyledLabel ("Start:");
        label.addStyleRange (new StyleRange (Font.BOLD,
                                               Color.BLACK));
        setTextAndMnemonic (label,
                            textWithMnemonic);
        return label;
    }

}
