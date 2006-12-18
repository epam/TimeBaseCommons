package deltix.custom.statestreet.fxa.load;

import deltix.util.jdbc.AccessConnectionFactory;
import java.io.*;
import java.sql.*;

import deltix.custom.statestreet.fxa.utils.*;

/**
 *  Usage: java deltix.custom.statestreet.fxa.load.MDDump &lt;mdb file&gt; &lt;table name&gt;
 */
public class MDDump {
    public static void main (String [] args) throws Exception {
        File                    mdb = new File (args [0]); 
        String                  tname = args [1];
        Connection              conn = AccessConnectionFactory.open (mdb);                        
        Statement               stmt = conn.createStatement ();            
        ResultSet               rs = stmt.executeQuery ("select * from [" + tname + "]");
        ResultSetMetaData       md = rs.getMetaData ();
        
        for (int col = 1; col <= md.getColumnCount (); col++) {
            String              jt = md.getColumnTypeName (col);
            String              cname = md.getColumnName (col);
            
            System.out.print ("\"");
            System.out.print (cname);
            System.out.print ("\"");
            
            for (int ii = cname.length (); ii < 46; ii++)
                System.out.print (" ");
            
            if (jt.equals ("REAL"))
                System.out.print ("FLOAT");
            else if (jt.equals ("DATETIME"))
                System.out.print ("TIMESTAMP(0)");
            else
                System.out.print ("VARCHAR2(255)");
            
            System.out.println (",");
        }
        
        conn.close ();           
    }
}
