package deltix.util.oauth.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    public static String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Can't find resource " + fileName);
            }
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

}
