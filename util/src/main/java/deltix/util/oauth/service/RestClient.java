package deltix.util.oauth.service;

import java.io.IOException;

interface RestClient {

    String postForm(TokenQuery query) throws IOException;

}
