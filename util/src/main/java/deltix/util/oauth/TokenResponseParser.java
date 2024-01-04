package deltix.util.oauth;

interface TokenResponseParser {

    TokenInfo parse(String response);

}
