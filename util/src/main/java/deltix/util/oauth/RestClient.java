package deltix.util.oauth;

import java.io.IOException;
import java.util.Map;

interface RestClient {

    String postForm(Map<String, String> parameters) throws IOException;

}
