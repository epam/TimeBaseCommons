package deltix.custom.statestreet.fxa.load;

/**
 *
 */
public abstract class StringFieldTrf extends OneToOneFieldTrf {
    public StringFieldTrf (String name) {
        super (name);
    }
    
    public StringFieldTrf (String inName, String outName) {
        super (inName, outName);
    }

    protected abstract String   transform (String in);
    
    protected final Object      transform (Object in) {
        return (transform ((String) in));
    }
}
