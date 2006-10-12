package deltix.custom.statestreet.fxa.load;

/**
 *
 */
public class UpperCaseStringFieldTrf extends StringFieldTrf {
    public UpperCaseStringFieldTrf (String name) {
        super (name);
    }
    
    public UpperCaseStringFieldTrf (String inName, String outName) {
        super (inName, outName);
    }

    protected String            transform (String in) {
        return (in.toUpperCase ());
    }
}
