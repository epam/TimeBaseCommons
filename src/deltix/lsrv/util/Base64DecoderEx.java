package deltix.lsrv.util;

import deltix.util.codec.Base64Decoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 */
public class Base64DecoderEx {

    public static byte[] decodeBuffer(String s) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Decoder decoder = new Base64Decoder(outputStream);
        decoder.write(s);
        decoder.flush();
        return outputStream.toByteArray();
    }

}
