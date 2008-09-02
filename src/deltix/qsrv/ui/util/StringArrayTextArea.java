package deltix.qsrv.ui.util;

import java.util.*;

import javax.swing.*;

/**
 */
public class StringArrayTextArea extends JTextArea {

    private static final long serialVersionUID = 1L;
    
    public static final String DIVIDER = ";";

    public StringArrayTextArea(int rows, int columns) {
        super(rows, columns);
        setLineWrap(true);
    }

    public String[] getData() {
        String value = getText();
        
        if (value.equals ("N/A"))
            return (null);
        
        java.util.List<String> fill = new ArrayList<String>();
        String[] values = value.split(DIVIDER);
        for (int idx = 0; idx < values.length; idx++) {
            String s = values[idx];
            if (!s.isEmpty())
                fill.add(s.trim());
        }
        return fill.toArray(new String[fill.size()]);
    }

    public void setData(String[] values) {
        String value = "";
        
        if (values == null)
            value = "N/A";
        else
            for (int idx = 0; idx < values.length; idx++) {
                String s = values[idx];
                value += s;
                if (idx != values.length - 1)
                    value += ";";
            }
        
        setText(value);
    }
}
