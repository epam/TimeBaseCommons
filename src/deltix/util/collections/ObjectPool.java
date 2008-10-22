package deltix.util.collections;

import java.util.*;

/**
 *  Pool of objects.
 */
public class ObjectPool <T> {
    private Map <Class <?>, ArrayList <T>>      pool =
        new HashMap <Class <?>, ArrayList <T>> ();
    
    public T            checkOut (Class <?> cls) {
        ArrayList <T>       clsPool = pool.get (cls);
        
        if (clsPool == null)
            return (null);
        
        int                 size = clsPool.size ();
        
        if (size == 0)
            return (null);
        
        return (clsPool.remove (size - 1));
    }
    
    public void         checkIn (T object) {
        Class <?>           cls = object.getClass ();
        ArrayList <T>       clsPool = pool.get (cls);
        
        if (clsPool == null) {
            clsPool = new ArrayList <T> ();
            pool.put (cls, clsPool);
        }
        
        clsPool.add (object);
    }
}
