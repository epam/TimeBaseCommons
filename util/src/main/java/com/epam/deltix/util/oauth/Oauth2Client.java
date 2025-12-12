package com.epam.deltix.util.oauth;

import com.epam.deltix.util.lang.Disposable;
import com.epam.deltix.util.oauth.service.Oauth2ClientImpl;

public interface Oauth2Client extends Disposable {

    static Oauth2Client create(Oauth2ClientConfig config) {
        return Oauth2ClientImpl.of(config);
    }

    AuthResult login();

    default String clientId() {
        return login().userName();
    }

    default String token() {
        return login().accessToken();
    }

}
