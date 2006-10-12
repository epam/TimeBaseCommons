package deltix.custom.statestreet.fxa.load;

import java.sql.*;

/**
 *
 */
public class IdentityFieldTrf extends OneToOneFieldTrf {
    public IdentityFieldTrf (String name) {
        this (name, name);
    }
    
    public IdentityFieldTrf (String inName, String outName) {
        super (inName, outName);
    }
    
    protected Object        transform (Object in) {
        return (in);
    }
}
