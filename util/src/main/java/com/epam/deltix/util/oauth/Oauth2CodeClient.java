package com.epam.deltix.util.oauth;

import com.epam.deltix.util.oauth.authcode.Oauth2CodeClientImpl;

public interface Oauth2CodeClient extends Oauth2Client {
    static Oauth2CodeClient create(Oauth2CodeClientConfig config) {
        return Oauth2CodeClientImpl.of(config);
    }

    static Oauth2CodeClient create(Oauth2CodeClientConfig config, RefreshTokenListener listener) {
        return Oauth2CodeClientImpl.of(config, listener);
    }
}
