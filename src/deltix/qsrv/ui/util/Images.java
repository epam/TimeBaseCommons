package deltix.qsrv.ui.util;

import java.awt.*;

import javax.swing.*;

import com.jidesoft.icons.*;

public abstract class Images {
    
    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(Images.class, name);
        else
            return null;
    }
    
    
    public static final Image    LOGO =
        getImageIcon ("logo.png").getImage();

}
