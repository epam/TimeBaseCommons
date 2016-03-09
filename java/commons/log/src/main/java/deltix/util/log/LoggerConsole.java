package deltix.util.log;

import java.awt.*;
import java.util.logging.*;
import java.util.logging.Formatter;

import javax.swing.*;
import javax.swing.text.*;

import deltix.util.collections.generated.*;
import deltix.util.jide.*;

public class LoggerConsole extends JTextPane {

    public    static final String  WARNING        = "warning";
    public    static final String  ERROR          = "error";
    public    static final String  NORMAL         = "normal";

    protected        final HandlerImpl consoleHandler;

    public LoggerConsole () {
        super ();
        setEditable (false);
        final StyledDocument doc = getStyledDocument ();
        addStylesToDocument (doc);

        consoleHandler = createConsoleHandler ();
    }

    public void reinit(){
        setText ("");
        consoleHandler.sizes.clear ();
    }

    protected HandlerImpl createConsoleHandler () {
        return new HandlerImpl (getStyledDocument ());
    }

    protected void addStylesToDocument (final StyledDocument doc) {

        final Style normal = doc.addStyle (NORMAL,
                                           null);
        StyleConstants.setFontFamily (normal,
                                      "Tahoma");
        StyleConstants.setFontSize (normal,
                                    10);
        StyleConstants.setForeground (normal,
                                      Color.DARK_GRAY);

        final Style warning = doc.addStyle (WARNING,
                                            null);
        StyleConstants.setFontFamily (warning,
                                      "Tahoma");
        StyleConstants.setFontSize (warning,
                                    10);
        StyleConstants.setForeground (warning,
                                      Color.BLUE);

        final Style error = doc.addStyle (ERROR,
                                          null);
        StyleConstants.setFontFamily (error,
                                      "Tahoma");
        StyleConstants.setFontSize (error,
                                    10);
        StyleConstants.setForeground (error,
                                      Color.RED);

    }

    public final void addLogger (final Logger logger) {
        logger.addHandler (consoleHandler);
    }

    public final void removeLogger (final Logger logger) {
        logger.removeHandler (consoleHandler);
    }

    public static class HandlerImpl extends Handler {

        public    static final int              MAX_RECORD_COUNT = 100;
        protected        final IntegerArrayList sizes;
        protected        final StyledDocument   doc;

        public HandlerImpl (StyledDocument doc) {
            super ();
            this.doc = doc;
            sizes = new IntegerArrayList ();
        }

        @Override
        public Formatter getFormatter () {
            if (super.getFormatter () == null)
                super.setFormatter (new TickFormatter ());

            return super.getFormatter ();
        }

        @Override
        public void publish (final LogRecord record) {
            JideUtil.asyncInvoke (new Runnable() {
                @Override
                public void run () {
                    Style style;
                    if (record.getLevel () == Level.SEVERE) {
                        style = doc.getStyle (ERROR);
                    } else if (record.getLevel () == Level.WARNING) {
                        style = doc.getStyle (WARNING);
                    } else {
                        style = doc.getStyle (NORMAL);
                    }
                    try {
                        final String str = getFormatter ().format (record);
                        doc.insertString (doc.getLength (),
                                          str,
                                          style);
                        sizes.add (str.length ());
                        truncateIfNeed();
                    } catch (final BadLocationException e) {
                        //
                    }
                }
            });
        }

        protected void truncateIfNeed () throws BadLocationException {
            if (sizes.size () > MAX_RECORD_COUNT) {
                final Integer len = sizes.remove (0);
                doc.remove (0, len);
            }
        }

        @Override
        public void flush () {
        }

        @Override
        public void close () throws SecurityException {
        }

    }
}
