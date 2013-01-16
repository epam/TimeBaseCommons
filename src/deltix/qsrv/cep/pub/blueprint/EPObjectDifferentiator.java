package deltix.qsrv.cep.pub.blueprint;

import deltix.util.collections.CursorFromIterator;

/**
 *  Utility for differentiating two sorted sets of objects.
 */
public class EPObjectDifferentiator <T extends EPObject> {
    public void         diffCollections (Iterable <T> from, Iterable <T> to) {
        CursorFromIterator <T>      ifrom = new CursorFromIterator <T> (from);
        CursorFromIterator <T>      ito = new CursorFromIterator <T> (to);
        
        ifrom.next ();
        ito.next ();
        
        for (;;) {
            if (ifrom.isAtEnd ()) 
                if (ito.isAtEnd ())
                    break;
                else {
                    objectAdded (ito.get ());
                    ito.next ();
                }
            else {
                T           vfrom = ifrom.get ();
                
                if (ito.isAtEnd ())  {
                    objectRemoved (vfrom);
                    ifrom.next ();
                }
                else {
                    T       vto = ito.get ();
                    
                    int     cmp = vfrom.getId ().compareTo (vto.getId ());
                    
                    if (cmp < 0) {
                        objectRemoved (vfrom);
                        ifrom.next ();
                    }
                    else if (cmp > 0) {
                        objectAdded (vto);
                        ito.next ();
                    }
                    else {
                        diffObject (vfrom, vto);
                        ifrom.next ();
                        ito.next ();
                    }
                }                                    
            }                                                                       
        }
    }

    public void         diffObject (T from, T to) {
        int d = from.diff (to);
        
        if (d != 0)
            objectChanged (from, to, d);
    }

    protected void      objectRemoved (T p) {
    }

    protected void      objectAdded (T p) {
    }

    protected void      objectChanged (T from, T to, int props) {
    }        
}
