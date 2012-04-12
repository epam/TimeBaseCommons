package deltix.util.swing;

import deltix.util.swing.shapes.Line;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VerticalForm extends JPanel implements SwingUtil.DeepEnabler {
    public interface VerticalWeightComponent {
        public double       getVerticalWeight ();
    }
    
    protected GridBagConstraints mC                = new GridBagConstraints ( );
    protected Set<Component>     mSwitchComponents = new HashSet<Component> ( );
    protected int                mLabelAncor       = GridBagConstraints.WEST;
    private Font                labelFont = Font.decode ("Arial-BOLD-11");

//    public VerticalForm (int labelAnchor) {
//        this();
//
//        mLabelAncor = labelAnchor;
//    }

    public VerticalForm () {
        super (new GridBagLayout ());
        
        mC.gridy = 0;
        mC.insets.left = 4;
        mC.insets.right = 4;
        mC.insets.top = 4;
        mC.insets.bottom = 4;
        
        addContainerListener (
            new ContainerListener () {
                public void    componentAdded (ContainerEvent e) {  
                    Component   c = e.getChild ();
                    
                    if (mSwitchComponents.contains (c))
                        c.setEnabled (isEnabled ());
                }
                
                public void    componentRemoved (ContainerEvent e) {
                    mSwitchComponents.remove (e.getChild ());
                }
            }
        );
    }

    public Font         getLabelFont () {
        return labelFont;
    }

    public void         setLabelFont (Font labelFont) {
        this.labelFont = labelFont;
    }
    
    @Override
    public void         removeAll () {
        super.removeAll ();
        mC.gridy = 0;
    }    
    
    @Override
    public void         setEnabled (boolean b) {
        super.setEnabled (b);
        
        for (Component c : mSwitchComponents)
            SwingUtil.setDeepEnabled (c, b);
    }  
    
    /**
     *  Adds a spring with a 1.0 weight
     */
    public void         addSpring () {
        addSpring (1.0);
    }
    
    /**
     *  Adds a spring, i.e. expandable space. This is useful if the form
     *  consists of vertically non-expandable rows, which is the most
     *  frequent situation. The spring is where the space will be filled.
     */
    public void         addSpring (double weighty) {
        mC.gridwidth = 2;
        mC.gridx = 0;
        mC.fill = GridBagConstraints.VERTICAL;
        mC.weighty = weighty;
        add (new JLabel (), mC);
        mC.gridy++;
    }
    
    protected void      setWeightAndFill (JComponent comp) {
        if (comp instanceof FileField || 
            comp instanceof SwingUtil.HorizontalFillOnlyField) {
            mC.weightx = 1;
            mC.weighty = 0;
            mC.fill = GridBagConstraints.HORIZONTAL;
        }
        else if (comp instanceof JTextArea ||
            comp instanceof JTextPane ||
            comp instanceof JScrollPane ||
            comp instanceof JTabbedPane ||
            comp instanceof JPanel ||
            comp instanceof JTable) 
        {
            mC.weightx = 1;
            mC.weighty = 1;
            mC.fill = GridBagConstraints.BOTH;
        }
        else if (comp instanceof AbstractButton || comp instanceof JSpinner) {
            mC.weightx = 0;
            mC.weighty = 0;
            mC.fill = GridBagConstraints.NONE;
            mC.anchor = GridBagConstraints.WEST;
        }       
        else {
            mC.weightx = 1;
            mC.weighty = 0;
            mC.fill = GridBagConstraints.HORIZONTAL;
        }        
        
        if (comp instanceof VerticalWeightComponent)
            mC.weighty = ((VerticalWeightComponent) comp).getVerticalWeight ();
    }
    
    public void         addRow (JComponent comp) {
        addRow (comp, true);
    }
    
    public void         addRow (JComponent comp, boolean disableWithForm) {
        if (disableWithForm)
            mSwitchComponents.add (comp);
        
        mC.gridx = 0;
        mC.gridwidth = 2;
        
        setWeightAndFill (comp);
        add (comp, mC);
        
        mC.gridy++;
        
        if (disableWithForm)
            SwingUtil.setDeepEnabled (comp, isEnabled ());
    }
    
    public void         addRow (
        JComponent          comp, 
        double              weightx,
        double              weighty,
        int                 fill,
        int                 anchor,        
        boolean             disableWithForm
    )
    {
        if (disableWithForm)
            mSwitchComponents.add (comp);
        
        mC.gridx = 0;
        mC.gridwidth = 2;
        
        mC.weightx = weightx;
        mC.weighty = weighty;
        mC.fill = fill;
        mC.anchor = anchor;
        
        add (comp, mC);
        
        mC.gridy++;
        
        if (disableWithForm)
            SwingUtil.setDeepEnabled (comp, isEnabled ());
    }
    
    public JLabel       createLabel (String text) {
        JLabel      label = new JLabel (text);
        
        if (labelFont != null)
            label.setFont (labelFont);
        
        return (label);
    }
    
    public void         addLine () {
        addRow (new Line (Line.HORIZONTAL));
    }
    
    public void         addLine (Stroke stroke) {
        addRow (new Line (Line.HORIZONTAL, stroke));
    }
    
    public void         addLine (Color color) {
        addRow (new Line (Line.HORIZONTAL, new BasicStroke (1), color));
    }
    
    public void         addField (String label, JComponent comp) {
        addField (label, comp, !(comp instanceof JLabel));        
    }
    
    public void         addField (String label, JComponent comp, boolean disableWithForm) {
        addField (createLabel (label), comp, disableWithForm);
    }
    
    public void         addField (JLabel jl, JComponent comp) {
        addField (jl, comp, !(comp instanceof JLabel));
    }

    public void         addLabel (JLabel jl, int gridx) {
        mC.gridwidth = 1;
        mC.gridx = 0;
        mC.weightx = 0;
        mC.weighty = 0;
        mC.anchor = mLabelAncor;
        mC.gridx = gridx;

        add (jl, mC);

        mC.gridy++;
    }
    
    public void         addField (JLabel jl, JComponent comp, boolean disableWithForm) {                
        mC.gridwidth = 1;
        mC.gridx = 0;
        mC.weightx = 0;
        mC.weighty = 0;
        mC.anchor = mLabelAncor;
        
        add (jl, mC);
        
        if (comp != null) {
            if (disableWithForm)
                mSwitchComponents.add (comp);
            
            mC.gridx = 1;

            setWeightAndFill (comp);

            add (comp, mC);
            
            if (disableWithForm)
                SwingUtil.setDeepEnabled (comp, isEnabled ());
        }
        
        mC.gridy++;        
    }
    
    
    
}
