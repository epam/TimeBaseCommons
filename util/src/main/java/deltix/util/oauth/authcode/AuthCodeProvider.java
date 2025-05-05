package deltix.util.oauth.authcode;

interface AuthCodeProvider extends AutoCloseable {

    AuthCodeResult requestCode(String authorizationUrl);

    int redirectPort();

}
