package deltix.util.jide;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogLevel;
import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideScrollPane;
import deltix.util.io.IOUtil;
import deltix.util.swing.AppExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;

public class JideAbstractApp extends DefaultDockableBarDockableHolder 
    implements UncaughtExceptionHandler
{    
    private static final long serialVersionUID = 1L;

    public JideAbstractApp () {
        this (EXIT_ON_CLOSE);     
        
    }
    
    public JideAbstractApp (int defaultCloseOperation) {
        setDefaultCloseOperation (defaultCloseOperation);     
        getDockingManager().setOutlineMode(DockingManager.TRANSPARENT_OUTLINE_MODE);
        addWindowListener (
            new WindowAdapter () {
                @Override
                public void windowClosing (WindowEvent e) {
                    JideAbstractApp.this.windowClosing (e);
                }
            }
        );
        
        JideAppExceptionHandler.currentApp = this;
        
        LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
//                defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and 
                // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());
    }

    protected void          windowClosing (WindowEvent e) {        
    }
    
    /**
     *  Ask the user if he wants to exit, and call System.exit (0) if so.
     */
    public void             confirmSystemExit (String title, String msg) {
        int     ret =
            JOptionPane.showConfirmDialog (
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION
            );
        
        if (ret == JOptionPane.YES_OPTION)
            System.exit (0); 
    }    

    public void             showError (String title, String msg) {
        JOptionPane.showMessageDialog (
            this,
            msg,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public void             handle (Throwable x) {
        handle (x, LogLevel.ERROR);
    }
    
    public void             handle (
        Throwable                   x,
        LogLevel                       logLevel
    ) 
    {
        handle (x, JideUtil.LOGGER, logLevel);
    }
    
    public void             handle (
        Throwable                   x,
        Log                      logger,
        LogLevel                       logLevel
    ) 
    {
        JideUtil.staticHandle (this, x, logger, logLevel);
    }
    
    public void             asyncHandle (Throwable x) {
        asyncHandle (x, LogLevel.ERROR);
    }
    
    public void             asyncHandle (
        Throwable                   x,
        LogLevel                       logLevel
    ) 
    {
        asyncHandle (x, JideUtil.LOGGER, logLevel);
    }
    
    public void                 asyncHandle (
        Throwable                   x,
        Log logger,
        LogLevel logLevel
    ) 
    {
        JideUtil.asyncHandle (this, x, logger, logLevel);
    }
    
    public void                 syncInvoke (Runnable r)
        throws InterruptedException 
    {
        try {
            if (SwingUtilities.isEventDispatchThread ())
                r.run ();
            else
                SwingUtilities.invokeAndWait (r);
        } catch (java.lang.reflect.InvocationTargetException x) {
            handle (x);
        }
    }
    
    public void                 printUsage () throws IOException, InterruptedException  {
        String      cname = getClass ().getName ();
        int         dot = cname.lastIndexOf ('.');
        String      path;
        
        if (dot > 0)
            path = cname.substring (0, dot + 1).replace ('.', '/') + "usage.txt";
        else
            path = "usage.txt";
        
        IOUtil.copyResource (path, System.out);
    }
    
    public void                 uncaughtException (Thread t, Throwable e) {
        asyncHandle (e);
    }
    
    public void                 installExceptionHandler () {
        Thread.setDefaultUncaughtExceptionHandler (this);
        System.setProperty ("sun.awt.exception.handler", AppExceptionHandler.class.getName ());
    }
    
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane pane = new JideScrollPane(component);
        pane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }
    
    public static DockableFrame createDockableFrame(String key, Icon icon) {
        DockableFrame frame = new DockableFrame(key, icon);
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }
}
