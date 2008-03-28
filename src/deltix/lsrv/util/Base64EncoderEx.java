package deltix.lsrv.util;

import deltix.util.codec.Base64Encoder;

import java.io.StringWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 */
public class Base64EncoderEx {

    public static String encode(byte[] bytes) throws IOException {
        StringWriter stringWriter = new StringWriter();
        Base64Encoder encoder = new Base64Encoder(stringWriter);
        encoder.write(bytes);
        encoder.flush();
        return stringWriter.getBuffer().toString();
    }

    public static void encode(byte[] bytes, OutputStream outputStream) throws IOException {
        StringWriter stringWriter = new StringWriter();
        Base64Encoder encoder = new Base64Encoder(stringWriter);
        encoder.write(bytes);
        encoder.flush();
        outputStream.write(stringWriter.getBuffer().toString().getBytes());
    }
}
