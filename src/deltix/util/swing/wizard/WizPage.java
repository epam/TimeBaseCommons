package deltix.util.swing.wizard;

import javax.swing.*;

/**
 *
 */
public abstract class WizPage {
    static final class AbortTransitionException extends RuntimeException {    
    }
    
    private boolean                 allowClosing = true;
    private String                  header;
    private Icon                    icon;
    private WizPanel                wiz = null;
    private JComponent              ui = null;
    
    protected WizPage (String header, Icon icon) {
        this.header = header;
        this.icon = icon;

//        addPageListener (
//            new PageListener () {
//                public void     pageEventFired (PageEvent e) {
//                    //System.out.println ("pageEventFired: " + e.getID () + " from " + e.getSource ());
//                
//                    allowClosing = true;
//                    
//                    try {
//                        if (e.getID () == PageEvent.PAGE_CLOSING) {
//                            pageClosing ();
//
//                            if (e.getSource () instanceof JButton) {
//                                final JButton   btn = (JButton) e.getSource ();
//                                final String    name = btn.getName ();
//
//                                if (name.equals (ButtonNames.NEXT))
//                                    onNext ();
//                                else if (name.equals (ButtonNames.BACK))
//                                    onBack ();
//                                else if (name.equals (ButtonNames.FINISH))
//                                    onFinish ();
//                                else if (name.equals (ButtonNames.CANCEL))
//                                    onCancel ();
//                                else
//                                    System.out.println ("Unknown button name: " + name);
//                            }
//                        }
//                        else if (e.getID () == PageEvent.PAGE_OPENED) {
//                            if ((e.getSource () instanceof JButton) &&
//                                ((JButton) e.getSource ()).getName ().equals (ButtonNames.BACK))
//                                onOpenBacktrack ();
//                            else
//                                onOpenForward ();
//                        }   
//                    } catch (AbortTransitionException x) {
//                        allowClosing = false;
//                    }
//                }
//            }
//        );
    }

    public WizPanel             getWizardPanel () {
        return (wiz);
    }
    
    public String               getTitle () {
        return header;
    }

    public Icon                 getIcon () {
        return icon;
    }

    public void                 setTitle (String header) {
        this.header = header;
    }

    public void                 setIcon (Icon icon) {
        this.icon = icon;
    }
            
    JComponent                  getUI () {
        if (ui == null)
            ui = createWizardContent ();
                
        return (ui);
    }

    protected final void        abortTransition () {
        throw new AbortTransitionException ();
    }
    
    void                        setWizard (WizPanel wiz) {
        this.wiz = wiz;
    }
    
    public void                 pageClosing () {
    }

    public void                 onNext () {
    }

    public void                 onBack () {
    }

    public void                 onFinish () {
    }

    public void                 onCancel () {
    }

    public void                 onOpenForward () {
    }

    public void                 onOpenBacktrack () {
    }   

    public abstract JComponent  createWizardContent ();
    
    public void             setupWizardButtons () {
        boolean         isFirst = isFirstPage ();        
        boolean         isLast = isLastPage ();
        
        setBackEnabled (!isFirst);
        setBackShown (!isFirst);
        
        setNextEnabled (!isLast);
        setNextShown (!isLast);
    }

    public final boolean    isFirstPage () {
        return (wiz.isFirstPage (this));
    }
    
    public final boolean    isLastPage () {
        return (wiz.isLastPage (this));
    }
    
    public final void    setNextEnabled (boolean b) {
        wiz.setNextEnabled (b);
    }
    
    public final void    setBackEnabled (boolean b) {
        wiz.setBackEnabled (b);
    }
    
    public final void    setFinishEnabled (boolean b) {
        wiz.setFinishEnabled (b);
    }
    
    public final void    setCancelEnabled (boolean b) {
        wiz.setCancelEnabled (b);
    }
    
    public final void    setNextShown (boolean b) {
        wiz.setNextShown (b);
    }
    
    public final void    setBackShown (boolean b) {
        wiz.setBackShown (b);
    }
    
    public final void    setFinishShown (boolean b) {
        wiz.setFinishShown (b);
    }
    
    public final void    setCancelShown (boolean b) {
        wiz.setCancelShown (b);
    }    
}
