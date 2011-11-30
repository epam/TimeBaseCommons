package deltix.util.swing.wizard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *
 */
public class WizPanel extends JPanel {
    private static final int            NO_JUMP_BACK = -1;
    
    private Font                        titleFont = Font.decode ("Arial-BOLD-14");
    private Font                        stepFont = Font.decode ("Arial-NORMAL-12");
    private Insets                      titleInsets = new Insets (12, 12, 12, 12);
    private Insets                      stepInsets = new Insets (4, 8, 4, 8);
    private Insets                      buttonInsets = new Insets (4, 4, 4, 4);

    private JButton                     back = new JButton ("< Back");
    private JButton                     next = new JButton ("Next >");
    private JButton                     finish = new JButton ("Finish");
    private JButton                     cancel = new JButton ("Cancel");
    
    private final JSplitPane            split = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
    private final GridBagConstraints    c = new GridBagConstraints ();
    private final JLabel                title = new JLabel (" ");
    private final JPanel                steps = new JPanel (new GridBagLayout ());    
    private final JPanel                buttonPanel = new JPanel (new GridBagLayout ());
    
    private int                         currentPageIdx = 0;
    private int                         jumpBackLimit = 0;
    private PageList                    pageList;
    private boolean                     stepsAreValid = false;
    
    public WizPanel () {
        super (new BorderLayout ());
                  
        back.addActionListener (
            new ActionListener () {
                public void     actionPerformed (ActionEvent e) {
                    back ();
                }
            }
        );
        
        next.addActionListener (
            new ActionListener () {
                public void     actionPerformed (ActionEvent e) {
                    next ();
                }
            }
        );
        
        finish.addActionListener (
            new ActionListener () {
                public void     actionPerformed (ActionEvent e) {
                    finish ();
                }
            }
        );
        
        cancel.addActionListener (
            new ActionListener () {
                public void     actionPerformed (ActionEvent e) {
                    cancel ();
                }
            }
        );
        
        c.insets = buttonInsets;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        buttonPanel.add (new JLabel (), c);
        c.gridx++;
        
        c.weightx = 0;
        buttonPanel.add (back, c);
        c.gridx++;
        buttonPanel.add (next, c);
        c.gridx++;
        buttonPanel.add (finish, c);
        c.gridx++;
        buttonPanel.add (cancel, c);
        c.gridx++;
        
        title.setBorder (
            BorderFactory.createEmptyBorder (
                titleInsets.top,
                titleInsets.left,
                titleInsets.bottom,
                titleInsets.right
            )
        );
        
        title.setFont (titleFont);
        
        split.setLeftComponent (new JScrollPane (steps));
        split.setRightComponent (new JTextArea ());
        
        add (title, BorderLayout.NORTH);
        add (split, BorderLayout.CENTER);
        add (buttonPanel, BorderLayout.SOUTH);
    }
    
    private void        invalidateSteps () {
        stepsAreValid = false;
    }
    
    public void         validateSteps () {
        if (stepsAreValid)
            return;
        
        c.insets = stepInsets;
        steps.removeAll ();
        
        int         n = pageList.size ();
        
        for (int stepIdx = 0; stepIdx < n; stepIdx++) {
            final WizPage   page = pageList.get (stepIdx);
            Font            f;
        
            if (stepIdx == currentPageIdx) {
                title.setText (page.getTitle ());
                f = stepFont.deriveFont (Font.BOLD);
            }
            else
                f = stepFont;

            c.gridy = stepIdx;
            c.weighty = 0;

            c.weightx = 0;
            c.gridx = 0;
            c.anchor = GridBagConstraints.EAST;

            JLabel          nl = new JLabel ((stepIdx + 1) + ".");

            nl.setFont (f);
            steps.add (nl, c);

            c.weightx = 1;
            c.gridx = 1;
            c.anchor = GridBagConstraints.WEST;

            JLabel      item = new JLabel ();
            
            if (jumpBackLimit != NO_JUMP_BACK &&
                stepIdx >= jumpBackLimit &&
                stepIdx < currentPageIdx)
            {
                item.setText ("<html><a href=''>" + page.getTitle () + "</a></html>");
                item.setForeground (Color.blue);
                item.addMouseListener (
                    new MouseAdapter () {
                        @Override
                        public void mouseClicked (MouseEvent e) {
                            goBackToPage (page);
                        }                        
                    }
                );
            }           
            else
                item.setText (page.getTitle ());
            
            item.setFont (f);
            
            steps.add (item, c);
        }
                
        c.gridy = n;
        c.weighty = 1;
        steps.add (new JLabel (" "), c);
        
        stepsAreValid = true;
    }
    
    public void     setPageList (PageList pl) {
        pageList = pl;
        pageList.setWizard (this);        
        currentPageIdx = -1;
        next ();
    }
    
    public void     setNextEnabled (boolean b) {
        next.setEnabled (b);
    }
    
    public void     setBackEnabled (boolean b) {
        back.setEnabled (b);
    }
    
    public void     setFinishEnabled (boolean b) {
        finish.setEnabled (b);
    }
    
    public void     setCancelEnabled (boolean b) {
        cancel.setEnabled (b);
    }
    
    public void     setNextShown (boolean b) {
        next.setVisible (b);
    }
    
    public void     setBackShown (boolean b) {
        back.setVisible (b);
    }
    
    public void     setFinishShown (boolean b) {
        finish.setVisible (b);
    }
    
    public void     setCancelShown (boolean b) {
        cancel.setVisible (b);
    }
    
    public boolean  isFirstPage (WizPage page) {
        int     n = pageList.size ();
        return (n > 0 && page == pageList.get (0));
    }
    
    public boolean  isLastPage (WizPage page) {
        int     n = pageList.size ();
        return (n > 0 && page == pageList.get (n - 1));
    }
    
    public void             setJumpBackLimit (WizPage p) {
        if (p == null)
            jumpBackLimit = NO_JUMP_BACK;
        else
            jumpBackLimit = pageList.indexOf (p);
        
        invalidateSteps ();
    }
    //
    //  Actions
    //
    public void             goBackToPage (WizPage p) {
        int     idx = pageList.indexOf (p);
        
        if (idx < 0)
            throw new IllegalArgumentException (p + " is not in list");
        
        invalidateSteps ();
        
        while (currentPageIdx > idx) {
            try {
                pageList.get (currentPageIdx).onBack ();
            } catch (WizPage.AbortTransitionException x) {
                return;
            }
            
            currentPageIdx--;
        }
        
        setPageUI (p);
        
        p.setupWizardButtons ();
        p.onOpenBacktrack ();
        validateSteps ();
    }
    
    void                    pageRemoved (int idx) {
        if (idx <= currentPageIdx)
            currentPageIdx--;
        
        invalidateSteps ();
    }
        
    void                    pageInserted (int idx) {
        if (idx <= currentPageIdx)
            currentPageIdx++;
        
        invalidateSteps ();
    }
    
    private void            setPageUI (WizPage p) {
        int dl = split.getDividerLocation ();
        split.setRightComponent (p.getUI ());
        split.setDividerLocation (dl);
    }
    
    public void             next () {
        WizPage     p = pageList.get (currentPageIdx + 1);
        WizPage     current = (currentPageIdx != -1) ? pageList.get (currentPageIdx) : null;

        if (currentPageIdx != -1) {
            try {
                current.onNext ();
            } catch (WizPage.AbortTransitionException x) {
                return;
            }
        }

        if (pageList.contains(current))
            p = pageList.get(currentPageIdx + 1);

        currentPageIdx = pageList.indexOf (p);
        invalidateSteps ();
        
        setPageUI (p);
        
        p.setupWizardButtons ();
        p.onOpenForward ();  
        validateSteps ();
    }
    
    public void             back () {
        WizPage     p = pageList.get (currentPageIdx - 1);
        WizPage     current = pageList.get (currentPageIdx);

        try {
            current.onBack();
        } catch (WizPage.AbortTransitionException x) {
            return;
        }

        if (pageList.contains(current))
            p = pageList.get(currentPageIdx - 1);

        currentPageIdx = pageList.indexOf (p);
        invalidateSteps ();
        
        setPageUI (p);
        
        p.setupWizardButtons ();
        p.onOpenBacktrack ();
        validateSteps ();
    }
    
    public void             onFinish () {        
    }
    
    public void             onCancel () {        
    }
        
    public void             finish () {
        if (currentPageIdx != -1) {
            try {
                pageList.get (currentPageIdx).onFinish ();
            } catch (WizPage.AbortTransitionException x) {
                return;
            }
        }
        
        onFinish ();
    }
    
    public void             cancel () {
        if (currentPageIdx != -1) {
            try {
                pageList.get (currentPageIdx).onCancel ();
            } catch (WizPage.AbortTransitionException x) {
                return;
            }
        }
        
        onCancel ();
    }    
}
