package com.epam.deltix.util.codec;

import com.epam.deltix.util.codec.Base64Encoder;
import com.epam.deltix.util.codec.Base64Decoder;

import java.io.StringWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
 */
public class Base64EncoderEx {
    public static final String            CHARSET    = "UTF-8";

    public static String encode(byte[] bytes) throws IOException {
        StringWriter stringWriter = new StringWriter();
        Base64Encoder encoder = new Base64Encoder(stringWriter);
        encoder.write(bytes);
        encoder.close();
        return stringWriter.getBuffer().toString();
    }

    public static void encode(byte[] bytes, OutputStream outputStream) throws IOException {
        StringWriter stringWriter = new StringWriter();
        Base64Encoder encoder = new Base64Encoder(stringWriter);
        encoder.write(bytes);
        encoder.close();
        outputStream.write(stringWriter.getBuffer().toString().getBytes(CHARSET));
    }

    public static void main(String[] args) throws Exception {

        byte[] bytesIn = new byte[7];

        for (int idx = 0; idx < "Tester".getBytes().length; idx++)
            bytesIn[idx] = (byte) "Tester".charAt(idx);
        bytesIn[6] = -14;

        StringWriter stringWriter = new StringWriter();
        Base64Encoder encoder = new Base64Encoder(stringWriter);
        encoder.write(bytesIn);
        encoder.close();

        String base64String = stringWriter.getBuffer().toString();

        System.out.println(base64String);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Decoder decoder = new Base64Decoder(outputStream);
        decoder.write(base64String);
        decoder.close();
        byte[] bytesOut = outputStream.toByteArray();

        System.out.printf("In array length is: %d, Out array length is: %d\n",
                bytesIn.length,
                bytesOut.length);
    }
}
