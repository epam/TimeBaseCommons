package com.epam.deltix.util.vsocket;

import com.epam.deltix.util.vsocket.util.SocketTestUtilities;
import com.epam.deltix.util.vsocket.util.TestServerSocketFactory;
import com.epam.deltix.util.io.IOUtil;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 */
public class Test_SocketEcho {
    public static void main (String [] args) throws Exception {
        int port = SocketTestUtilities.parsePort(args);

        TestServerSocketFactory.ServerThread server = TestServerSocketFactory.createEchoServerSocket(port);
        server.start ();

        client ("localhost", server.getLocalPort());
    }

    public static void  client (String host, int port)
        throws IOException {
        String utfString = "Hello world";

        Socket s = null;
        try {
            s = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(s.getOutputStream());
            DataInputStream is = new DataInputStream(s.getInputStream());

            os.writeUTF(utfString);
            os.flush();

            String readStr = is.readUTF();
            if (!readStr.equals(utfString))
                throw new AssertionError(readStr + " != " + utfString);

        } catch (Throwable x) {
            x.printStackTrace();
            throw x;
        } finally {
            IOUtil.close(s);
        }
    }

    @Test
    public void TestSocket() throws Exception {
        Test_SocketEcho.main(new String[0]);
    }
}
