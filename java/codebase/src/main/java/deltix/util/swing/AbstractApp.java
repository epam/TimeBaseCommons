package deltix.util.swing;

import deltix.util.Version;

public class AbstractApp 
    extends SwingAbstractApp
{
    public AbstractApp() {
        super();
    }

    public AbstractApp(int defaultCloseOperation) {
        super(defaultCloseOperation);
    }

    @Override
    public void                 setTitle (String title) {
        if (title.indexOf (Version.VERSION_STRING) < 0)
            title = title + " - Version " + Version.VERSION_STRING;
        
        super.setTitle (title);
    }
}
