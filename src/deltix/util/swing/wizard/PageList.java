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
        
        if (wiz != null) {
            page.setWizard (wiz);         
            wiz.pageInserted (pageList.size () - 1);
        }
    }
    
    public int          indexOf (WizPage p) {
        return (pageList.indexOf (p));
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
        
        idx++;
        
        pageList.add (idx, newPage);
        
        if (wiz != null) {
            newPage.setWizard (wiz);         
            wiz.pageInserted (idx);
        }
    }
    
    public void         remove (WizPage page) {
        int     idx = pageList.indexOf (page);
        
        if (idx < 0)
            return;
        
        pageList.remove (idx);
        
        if (wiz != null)
            wiz.pageRemoved (idx);
    }
    
    void                setWizard (WizPanel wiz) {
        this.wiz = wiz;
        
        for (WizPage p : pageList) 
            p.setWizard (wiz);                    
    }
}
