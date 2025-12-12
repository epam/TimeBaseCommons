package com.epam.deltix.util.oauth;

public class AuthResult {

    private final String userName;

    private final String accessToken;

    private final String refreshToken;

    private final long expiresInSec;

    public AuthResult(String userName, String accessToken, long expiresInSec) {
        this(userName, accessToken, null, expiresInSec);
    }

    public AuthResult(String userName, String accessToken, String refreshToken, long expiresInSec) {
        this.userName = userName;
        this.accessToken = accessToken;
        this.expiresInSec = expiresInSec;
        this.refreshToken = refreshToken;
    }

    public String userName() {
        return userName;
    }

    public String accessToken() {
        return accessToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public long expiresInSec() {
        return expiresInSec;
    }
}
