package deltix.util.lang;

/**
 *  Loads a single specified class from the supplied byte array.
 */
public class SingleByteArrayClassLoader extends AbstractClassLoader {
    private String          mClassName;
    private byte []         mBytes;
    
    public SingleByteArrayClassLoader (String className, byte [] bytes) {
        mClassName = className;
        mBytes = bytes;
    }
    
    protected byte [] loadClassBytes (String name) throws ClassNotFoundException {
        if (!name.equals (mClassName))
            throw new ClassNotFoundException (name);
        
        return (mBytes);
    }
}
