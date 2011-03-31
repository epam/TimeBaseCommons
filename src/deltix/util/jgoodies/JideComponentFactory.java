package deltix.util.jgoodies;

import com.jgoodies.common.swing.*;
import com.jgoodies.forms.factories.*;
import com.jidesoft.swing.*;

import javax.swing.*;
import java.awt.*;

public class JideComponentFactory extends DefaultComponentFactory {

    /**
     * Holds the single instance of this class.
     */
    private static final JideComponentFactory INSTANCE = new JideComponentFactory ();

    public static JideComponentFactory getInstance () {
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
        MnemonicUtils.configure(label,
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
    public JLabel createTitle (String textWithMnemonic) {
        return super.createTitle (textWithMnemonic);
    }

}
