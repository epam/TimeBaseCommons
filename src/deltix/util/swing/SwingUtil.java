package deltix.util.swing;

import deltix.util.concurrent.UncheckedInterruptedException;
import deltix.util.lang.Util;
import deltix.util.io.StreamPump;

import deltix.util.io.UncheckedIOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Colleciton of static utilities
 */
public abstract class SwingUtil { 
    public static final Icon       SAVE_ICON = loadIcon ("deltix/util/swing/save.gif");
    public static final Icon       NEW_ICON = loadIcon ("deltix/util/swing/new.gif");
    public static final Icon       OPEN_ICON = loadIcon ("deltix/util/swing/open.gif");
        
    /**
     *  Marks components that call setDeepEnabled from their own setEnabled method
     */
    public interface DeepEnabler { }
    
    
    /**
     * Marks components that using as selector  
     */
    public interface HorizontalFillOnlyField { }

    static final ResourceBundle     RB = ResourceBundle.getBundle ("deltix/util/swing/ui");
    
    public static final String  ERROR_TITLE =
        ResourceBundle.getBundle ("deltix/util/swing/exceptions").getString ("errorTitle");
    
    public static void          expandEntireTree (JTree tree) {
        for (int ii = 0; ii < tree.getRowCount(); ii++) {
            tree.expandRow (ii);
        }
    }

    public static void          collapseEntireTree (JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    
    public static ResourceBundle    getBundle (String name) {
        try {
            return (ResourceBundle.getBundle (name));
        } catch (MissingResourceException mrx) {
            staticHandle (mrx);
            System.exit (1);
            return (null);
        }
    }

    public static ImageIcon	    loadIcon (String relPath) {
            Image       img = loadImage (relPath);
        
        return (img == null ? null : new ImageIcon (img));
    }

    public static Image			loadImage (String relPath) {
        return (
            loadImageAndCloseStream (
                Util.class.getClassLoader ().getResourceAsStream (relPath),
                relPath
            )
        );
    }
    
	public static Image			loadImage (Class <?> cls, String relPath) {
        return (
            loadImageAndCloseStream (
                cls.getResourceAsStream (relPath),
                cls + "/" + relPath
            )
        );
    }

	public static Image			loadImage (File file) throws IOException {
		InputStream			is = new FileInputStream (file);

		try {
			return (loadImageAndCloseStream (is, file.getPath ()));
		} catch (Throwable x) {
			Util.LOGGER.log (
                Level.WARNING, 
                "Failed to read image file " + file,
                x
            );
			return (null);
		} finally {
			Util.close (is);
		}        
    }
    
	public static Image			loadImageAndCloseStream (InputStream is, String diag) {
        if (is == null)
            throw new UncheckedIOException (diag);
        
    	ByteArrayOutputStream	baos = new ByteArrayOutputStream (100000);

        try {
            try {
                StreamPump.pump (is, baos);
            } catch (IOException x) {
                throw new UncheckedIOException (x);
            } finally {
                Util.close (is);
            }

            return (loadImage (baos.toByteArray ()));
        } catch (InterruptedException x) {
            throw new UncheckedInterruptedException (x);
        }
	}

    public static void          invisibleDisable (JComponent c) {
        for (MouseListener l : c.getMouseListeners ())
            c.removeMouseListener (l);

        for (MouseMotionListener l : c.getMouseMotionListeners ())
            c.removeMouseMotionListener (l);

        for (MouseWheelListener l : c.getMouseWheelListeners ())
            c.removeMouseWheelListener (l);

        for (KeyListener l : c.getKeyListeners ())
            c.removeKeyListener (l);
    }

	public static Image			loadImage (byte [] bytes)
		throws InterruptedException
	{
		Toolkit			tk = Toolkit.getDefaultToolkit ();
		Image			img = tk.createImage (bytes);

		ensureImageIsLoaded (img);

		return (img);
	}

	public static Image			ensureImageIsLoaded (Image img)
		throws InterruptedException
	{
		Toolkit			tk = Toolkit.getDefaultToolkit ();

		while (!tk.prepareImage (img, -1, -1, null))
			Thread.sleep (1);

		return (img);
	}
    
	protected SwingUtil () {
	}

	public static String		getMsg (Throwable x) {
		String		msg = x.getLocalizedMessage ();

		if (msg == null || msg.length () == 0)
			return ("Exception: " + x.getClass ().getName ());

		return (msg);
	}

    public static void		    staticHandle (
        Component                   parent, 
        Throwable                   x,
        Logger                      logger,
        Level                       logLevel
    ) 
    {
        x = Util.unwrap (x);
		
        if (logger != null)
            logger.log (logLevel, "Uncaught Exception", x);

        new StdExceptionDialog (parent, x).setVisible (true);    	
    }
        
    public static void		    staticHandle (
        Component                   parent, 
        Throwable                   x,
        Level                       logLevel        
    )
    {
        staticHandle (parent, x, Util.LOGGER, logLevel);
    }
    
    public static void		    staticHandle (Throwable x) {
        staticHandle (null, x, Level.SEVERE);
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
        final Level                 level
    ) 
    {
        asyncHandle (parent, x, Util.LOGGER, level);
    }
    
    public static void          asyncHandle (
        final Component             parent, 
        final Throwable             x,
        final Logger                logger,
        final Level                 level
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

    public static void          setWindowsLookAndFeel () {
        try {
            UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable e) {
        } 
    }
            
    public static JButton       newZeroMarginButton (Action action) {
        JButton btn = new JButton (action);
        
        btn.setMargin (new Insets (0, 0, 0, 0));
        
        return (btn);
    }

    public static JButton       newZeroMarginNoTextButton (Action action) {
        JButton btn = newZeroMarginButton (action);
        
        btn.setText ("");
        
        return (btn);
    }

    public static void setEscapeHandler(JDialog dlg, Action escapeAction) {
	  KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
	  dlg.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
	  dlg.getRootPane().getActionMap().put("ESCAPE", escapeAction);
	}

	public static void setEscapeHandler(JDialog dialog, final ActionListener escapeAction) {
	  setEscapeHandler(dialog, new AbstractAction() {
		 public void actionPerformed(ActionEvent e) {
			escapeAction.actionPerformed(e);
		 }
	  });
	}
    
    public static void          setChildrenDeepEnabled (Container c, boolean b) {
        Component []    comps = c.getComponents();
        
        if (comps != null)
            for (Component comp : comps) 
                setDeepEnabled (comp, b);
    }
    
    public static void          setDeepEnabled (Component c, boolean b) {
        if (c instanceof DeepEnabler) {
            c.setEnabled (b);
            return;
        }
        
        if ((c instanceof JLabel || 
              c instanceof JToolBar ||
              c instanceof JTabbedPane ||
              c instanceof JScrollBar))
            c.setEnabled (true);
        else if (c instanceof JTextField) {
            JTextField      tf = (JTextField) c;
            tf.setEnabled (true);
            tf.setEditable (b);
        }            
        else if (c instanceof JTextArea) {
            JTextArea      tf = (JTextArea) c;
            tf.setEnabled (true);
            tf.setEditable (b);
        }
        else
            c.setEnabled (b);

        if (c instanceof Container)
            setChildrenDeepEnabled ((Container) c, b);
    }

    public static void setDeepEditable(Component c, boolean b) {
        if (c instanceof JEditorPane) {
            ((JEditorPane) c).setEditable(b);
        } else if (c instanceof JTextField) {
            ((JTextField) c).setEditable(b);
        } else if (c instanceof JTextArea) {
            ((JTextArea) c).setEditable(b);
        } else if (c instanceof JComboBox) {
            c.setEnabled(b);
        } else if (c instanceof JCheckBox) {
            c.setEnabled(b);
        } if (c instanceof JPanel) {
            c.setEnabled(b);
        }

        if (c instanceof Container)
            setChildrenDeepEditable((Container) c, b);
    }

    public static void          setChildrenDeepEditable (Container c, boolean b) {
        Component []    comps = c.getComponents();

        if (comps != null)
            for (Component comp : comps)
                setDeepEditable (comp, b);
    }

    /**
     * Creates default titled border.
     *
     * @param title border title
     * @return Border object
     */
    public static Border createDefaultBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), title);
    }

    /**
     * Creates the transparent panel with given title and layout.
     *
     * @param title  panel title
     * @param layout panel layout
     * @return JPanel object
     */
    public static JPanel createPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        if (layout != null)
            panel.setLayout(layout);
        if (title != null)
            panel.setBorder(createDefaultBorder(title));
        return panel;
    }

    /**
     * Creates the transparent panel.
     *
     * @return JPanel object
     */
    public static JPanel createPanel() {
        return createPanel(null, null);
    }

    /**
     * Creates the transparent panel with given title.
     *
     * @return JPanel object
     */
    public static JPanel createPanel(String title) {
        return createPanel(title, null);
    }

    /**
     * Creates the transparent panel with given layout.
     *
     * @return JPanel object
     */
    public static JPanel createPanel(LayoutManager layout) {
        return createPanel(null, layout);
  }
    /**
    * Creates button.
    * The preferred button size is 110x30.
    * @param action button action
    * @return JButton object
    */
   public static JButton createJButton(Action action) {
     final JButton button = new JButton(action);
     button.setHorizontalAlignment(SwingConstants.CENTER);
     button.setPreferredSize(new Dimension(80, 25));
     return button;
   }

   public static String       getText (Component comp) {
       if (comp instanceof JTextField)
           return (((JTextField) comp).getText ()); 
       else if (comp instanceof JLabel)
           return (((JLabel) comp).getText ());
       else if (comp instanceof JCheckBox)
           return (((JCheckBox) comp).isSelected () ? "true" : "false");
       else if (comp instanceof JTextArea)
           return (((JTextArea) comp).getText ());
       else if (comp instanceof JScrollPane)
           return (getText (((JScrollPane) comp).getViewport ().getView ()));
       else if (comp instanceof JComboBox)
           return (((JComboBox) comp).getSelectedItem ().toString ());
       else
           throw new IllegalArgumentException (comp.toString ());
   }
      
   public static void       setText (Component comp, String text) {
       if (comp instanceof JTextField)
            ((JTextField) comp).setText (text); 
       else if (comp instanceof JLabel)
            ((JLabel) comp).setText (text);
       else if (comp instanceof JCheckBox)
            ((JCheckBox) comp).setSelected (Boolean.parseBoolean (text));
       else if (comp instanceof JTextArea)
           ((JTextArea) comp).setText (text);
       else if (comp instanceof JScrollPane)
           setText (((JScrollPane) comp).getViewport ().getView (), text);
       else if (comp instanceof JComboBox)
            ((JComboBox) comp).setSelectedItem (text);
       else
           throw new IllegalArgumentException (comp.toString ());
   }
   
   @SuppressWarnings ("unchecked")
   public static <T extends Container> T findParent (Component c, Class <? extends T> cls) {
       for (;;) {
           Container        parent = c.getParent ();
           
           if (parent == null)
               return (null);
           
           if (cls.isAssignableFrom (parent.getClass ()))
               return ((T) parent);
           
           c = parent;
       }
   }
   
    /**
     * Centers <CODE>wind</CODE> within its parent. If it has no parent then
     * center within the screen. If centering would cause the title bar to go
     * above the parent (I.E. cannot see the titlebar and so cannot move the
     * window) then move the window down.
     * 
     * @param wind
     *            The Window to be centered.
     * 
     * @throws IllegalArgumentException
     *             If <TT>wind</TT> is <TT>null</TT>.
     */
    public static void centerWithinParent (Window wind) {
        if (wind == null) {
            throw new IllegalArgumentException ("null Window passed");
        }
        final Container parent = wind.getParent ();
        if (parent != null && parent.isVisible ()) {
            center (wind,
                    new Rectangle (parent.getLocationOnScreen (),
                                   parent.getSize ()));
        } else {
            centerWithinScreen (wind);
        }
    }

    /**
     * Centers passed internal frame within its desktop area. If centering would
     * cause the title bar to go off the top of the screen then move the window
     * down.
     * 
     * @param frame
     *            The internal frame to be centered.
     * 
     * @throws IllegalArgumentException
     *             If <TT>frame</TT> is <TT>null</TT>.
     */
    public static void centerWithinDesktop (JInternalFrame frame) {
        if (frame == null) {
            throw new IllegalArgumentException ("null JInternalFrame passed");
        }
        final Container parent = frame.getDesktopPane ();
        if (parent != null && parent.isVisible ()) {
            center (frame,
                    new Rectangle (new Point (0,
                                              0),
                                   parent.getSize ()));
        }
    }

    /**
     * Centers <CODE>wind</CODE> within the screen. If centering would cause the
     * title bar to go off the top of the screen then move the window down.
     * 
     * @param wind
     *            The Window to be centered.
     * 
     * @throws IllegalArgumentException
     *             If <TT>wind</TT> is <TT>null</TT>.
     */
    public static void centerWithinScreen (Window wind) {
        if (wind == null) {
            throw new IllegalArgumentException ("null Window passed");
        }
        final Toolkit toolKit = Toolkit.getDefaultToolkit ();
        final Rectangle rcScreen = new Rectangle (toolKit.getScreenSize ());
        final Dimension windSize = wind.getSize ();
        final Dimension parentSize = new Dimension (rcScreen.width,
                                                    rcScreen.height);
        if (windSize.height > parentSize.height) {
            windSize.height = parentSize.height;
        }
        if (windSize.width > parentSize.width) {
            windSize.width = parentSize.width;
        }
        center (wind,
                rcScreen);
    }

    /**
     * Centers <CODE>wind</CODE> within the passed rectangle.
     * 
     * @param wind
     *            The Window to be centered.
     * @param rect
     *            The rectangle (in screen coords) to center <CODE>wind</CODE>
     *            within.
     * 
     * @throws IllegalArgumentException
     *             If <TT>Window</TT> or <TT>Rectangle</TT> is <TT>null</TT>.
     */
    private static void center (Component wind,
                                Rectangle rect) {
        if (wind == null || rect == null) {
            throw new IllegalArgumentException ("null Window or Rectangle passed");
        }
        Dimension windSize = wind.getSize ();
        int x = ((rect.width - windSize.width) / 2) + rect.x;
        int y = ((rect.height - windSize.height) / 2) + rect.y;
        if (y < rect.y) {
            y = rect.y;
        }
        wind.setLocation (x,
                          y);
    }
}
