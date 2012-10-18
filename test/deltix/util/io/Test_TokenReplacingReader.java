package deltix.util.io;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author Andy
 *         Date: 10/17/12 9:27 PM
 */
public class Test_TokenReplacingReader {

    private static TokenReplacingReader.ITokenResolver NULL_RESOLVER = new TokenReplacingReader.ITokenResolver() {
        public String resolveToken(String token) {
            return null;
        }
    };

    @Test
    public void testPartialRead () throws Exception {
        String text =
            "<jplugin minversion=\"1\">\n" +
            "  <getfrom svn=\"https://gw.deltixlab.com:444/svn/src/internal/plugins/trade/quickfix\">\n" +
            "    <src folder=\"src\"/>";

        Reader reader = new TokenReplacingReader(new StringReader(text), NULL_RESOLVER);
        char[] buffer = new char[512];
        int bytesRead = reader.read(buffer, 0, 64);
        bytesRead+= reader.read(buffer, bytesRead, buffer.length - bytesRead);

        String result = new String(buffer, 0, bytesRead);
        assertEquals(text, result);
    }
}
