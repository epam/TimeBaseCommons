package deltix.util.oauth;

public interface RefreshTokenListener {

    void refreshed(AuthResult authResult);

    default void refreshFailed(Throwable t) { }

}
