package com.epam.deltix.util.net.timer;


/**
 *
 */
public class TimerServer {   
    public static final int             DEF_PORT = 11111;
    public static final long            WARMUP_NUM = 10000;

    public static void main (String [] args) throws Exception {
        int                     port = 11111;
        
        if (args.length == 1)
            port = Integer.parseInt (args [0]);

        TimerServerThread       st = new TimerServerThread (port);
        
        st.start ();
   }
}
