package deltix.util.oauth;

interface TokenInfo {

    String accessToken();

    long expiresInSec();

}
