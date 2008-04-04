package deltix.qsrv.ui.treeedit;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class StringArrayTextArea extends JTextArea {

    public static final String DIVIDER = ";";

    public StringArrayTextArea(int rows, int columns) {
        super(rows, columns);
    }

    public void getData(List<String> fill) {
        fill.clear();
        String value = getText();
        String [] values = value.split(DIVIDER);
        for (int idx = 0; idx < values.length; idx++) {
            String s = values[idx];
            if (!s.isEmpty())
                fill.add(s);
        }
    }

    public String[] getData() {
        java.util.List<String> fill = new ArrayList<String>();
        String value = getText();
        String[] values = value.split(DIVIDER);
        for (int idx = 0; idx < values.length; idx++) {
            String s = values[idx];
            if (!s.isEmpty())
                fill.add(s);
        }
        return fill.toArray(new String[fill.size()]);
    }


    public void setData(List<String> values) {
        String value = "";
        for (int idx = 0; idx < values.size(); idx++) {
            String s = values.get(idx);
            value += s;
            if (idx != values.size() - 1)
                value += ";";
        }
        setText(value);
    }

     public void setData(String[] values) {
        String value = "";
        for (int idx = 0; idx < values.length; idx++) {
            String s = values[idx];
            value += s;
            if (idx != values.length - 1)
                value += ";";
        }
        setText(value);
    }
}
