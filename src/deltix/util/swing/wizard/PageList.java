package deltix.util.swing.wizard;

import java.util.*;

/**
 *
 */
public class PageList {
    private WizPanel                    wiz;
    private ArrayList <WizPage>         pageList = new ArrayList <WizPage> ();
    
    public void         append (WizPage page) {
        pageList.add (page);
    }
    
    public int          size () {
        return (pageList.size ());
    }
    
    public WizPage      get (int idx) {
        return (pageList.get (idx));
    }
    
    public void         insertAfter (WizPage newPage, WizPage after) {
        int     idx = pageList.indexOf (after);
        
        if (idx < 0)
            throw new IllegalArgumentException (after + " not in list");
        
        pageList.add (idx + 1, newPage);
        newPage.setWizard (wiz);  
    }
    
    public void         remove (WizPage page) {
        int     idx = pageList.indexOf (page);
        
        if (idx < 0)
            return;
        
        pageList.remove (idx);
    }
    
    void                setWizard (WizPanel wiz) {
        this.wiz = wiz;
        
        for (WizPage p : pageList) 
            p.setWizard (wiz);                    
    }
}
