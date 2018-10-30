package deltix.util.jide;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import com.jidesoft.dialog.*;
import com.jidesoft.swing.*;

import deltix.gflog.Log;
import deltix.gflog.LogFactory;
import deltix.gflog.LogLevel;
import deltix.util.lang.*;

/**
 *  Colleciton of static utilities
 */
public abstract class JideUtil {

    static final Log LOGGER = LogFactory.getLog(Util.LOGGER_NAME);

    public static JideButton newZeroMarginButton (Action action ) {
		JideButton btn = new JideButton ( action );

		btn.setMargin ( new Insets ( 0,
		                             0,
		                             0,
		                             0 ) );

		return (btn);
	}

	public static JideButton newZeroMarginNoTextButton ( Action action ) {
		JideButton btn = newZeroMarginButton ( action );

		btn.setText ( "" );

		return (btn);
	}

   public static void		    staticHandle (
        Component                   parent,
        Throwable                   x,
        Log logger,
        LogLevel logLevel
    )
    {
        x = Util.unwrap (x);

        if (logger != null)
            logger.log (logLevel).append("Uncaught Exception").append(x).commit();

        StringWriter swr = new StringWriter();
        PrintWriter trace = new PrintWriter(swr);

        x.printStackTrace(trace);
        trace.close();

        String details = swr.toString();

        String xClassName = x.getClass().getName();
        String msg = x.getLocalizedMessage();

        if (msg == null || msg.length() == 0)
            msg = ("Exception: " + xClassName);

        JideOptionPane optionPane = new JideOptionPane(
                "Click \"Details\" button to see more information ... ",
                JOptionPane.ERROR_MESSAGE,
                JideOptionPane.CLOSE_OPTION);
        optionPane.setTitle(msg);
        optionPane.setDetails(details);

        JDialog dialog = optionPane.createDialog(parent, xClassName);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void          showErrorMessage(
            Component                  parent,
            String                     caption,
            String                     title,
            String                     message) {

         JideOptionPane optionPane = new JideOptionPane(
                "Click \"Details\" button to see more information ... ",
                JOptionPane.ERROR_MESSAGE,
                JideOptionPane.CLOSE_OPTION);
        optionPane.setTitle(title);
        optionPane.setDetails(message);

        JDialog dialog = optionPane.createDialog(parent, caption);
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void		    staticHandle (
        Component                   parent,
        Throwable                   x,
        LogLevel                       logLevel
    )
    {
        staticHandle (parent, x, LOGGER, logLevel);
    }

    public static void		    staticHandle (Throwable x) {
        staticHandle (null, x, LogLevel.ERROR);
    }

    public static void          asyncInvoke (Runnable r) {
        if (SwingUtilities.isEventDispatchThread())
            r.run();
        else
            SwingUtilities.invokeLater (r);
    }

    public static void          asyncHandle (
        final Component             parent,
        final Throwable             x,
        final LogLevel                 level
    )
    {
        asyncHandle (parent, x, LOGGER, level);
    }

    public static void          asyncHandle (
        final Component             parent,
        final Throwable             x,
        final Log                logger,
        final LogLevel                 level
    )
    {
        if (SwingUtilities.isEventDispatchThread())
            staticHandle (parent, x, logger, level);
        else
            SwingUtilities.invokeLater (
                new Runnable () {
                    public void     run () {
                        staticHandle (parent, x, logger, level);
                    }
                }
            );
    }

    public static CompoundBorder createRoundCornerBorder (String title) {
        return BorderFactory.createCompoundBorder (BorderFactory.createTitledBorder (new PartialLineBorder (Color.gray,
                                                                                                            1,
                                                                                                            true),
                                                                                     title),
                                                   BorderFactory.createEmptyBorder (0,
                                                                                    6,
                                                                                    4,
                                                                                    6));
    }

}
