package deltix.qsrv.ui.util;

import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

import com.jidesoft.icons.*;

public abstract class Images {

    public static final String SYSTEM_PROP_TEMP_DIR = "java.io.tmpdir";

    public static ImageIcon getImageIcon(String name) {
        if   (name != null)
            return IconsFactory.getImageIcon(Images.class, name);
        else
            return null;
    }

    public static ImageIcon getImageIcon(Class<?> c, String name) {
        if   (name != null)
            return IconsFactory.getImageIcon(c, name);
        else
            return null;
    }

    public static Image getImage(Class<?> c, String name) {
        ImageIcon icon = getImageIcon(c, name);
        return icon.getImage();
    }

    private static String getTempDir() {
        return System.getProperty(SYSTEM_PROP_TEMP_DIR);
    }

    public static void setImageCashingMode(){
        //by default image cache mode is switch on
        try {
            File tmpDir = new File(getTempDir());
            if (!tmpDir.exists()) {
                tmpDir.mkdirs(); //successfully create temp folder and continue to use cache
            }
        }catch (Exception t) {
            ImageIO.setUseCache(false);//switch off
        }

    }


}
