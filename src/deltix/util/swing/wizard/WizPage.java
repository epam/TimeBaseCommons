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
    private String                  help = null;
    private Icon                    icon;
    private WizPanel                wiz = null;
    private JComponent              ui = null;
    
    protected WizPage (String header, Icon icon) {
        this.header = header;
        this.icon = icon;
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

    public void                 setHelp (String help) {
        this.help = help;
    }

    public String               getHelp () {
        return help;
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
    
    public final void       goBackToPage (WizPage page) {
        wiz.goBackToPage (page);
    }
    
    public final void       setNextEnabled (boolean b) {
        wiz.setNextEnabled (b);
    }
    
    public final void       setBackEnabled (boolean b) {
        wiz.setBackEnabled (b);
    }
    
    public final void       setFinishEnabled (boolean b) {
        wiz.setFinishEnabled (b);
    }
    
    public final void       setCancelEnabled (boolean b) {
        wiz.setCancelEnabled (b);
    }
    
    public final void       setNextShown (boolean b) {
        wiz.setNextShown (b);
    }
    
    public final void       setBackShown (boolean b) {
        wiz.setBackShown (b);
    }
    
    public final void       setFinishShown (boolean b) {
        wiz.setFinishShown (b);
    }
    
    public final void       setCancelShown (boolean b) {
        wiz.setCancelShown (b);
    }    
}
