package deltix.util.oauth;

import deltix.util.LangUtil;

public interface RefreshTokenListener {

    void refreshed(AuthResult authResult);

    default void refreshFailed(Throwable t) { LangUtil.propagateError(t); }

}
