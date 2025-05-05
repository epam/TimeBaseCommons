package deltix.util.oauth.authcode;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.httpclient.jdk.JDKHttpClientConfig;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.AuthorizationUrlBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;
import deltix.util.oauth.AuthResult;
import deltix.util.oauth.Oauth2CodeClientConfig;
import deltix.util.oauth.utils.TokenUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

class AuthCodeClient {

    private static final int AUTHORIZATION_TIMEOUT = Integer.getInteger("TimeBase.oauth2.browserAuthTimeoutSec", 60);

    private static class OpenIdConfigurationApi extends DefaultApi20 {

        private final String tokenEndpoint;

        private final String authorizationEndpoint;

        public OpenIdConfigurationApi(String tokenEndpoint, String authorizationEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
            this.authorizationEndpoint = authorizationEndpoint;
        }

        @Override
        public String getAccessTokenEndpoint() {
            return tokenEndpoint;
        }

        @Override
        protected String getAuthorizationBaseUrl() {
            return authorizationEndpoint;
        }

        @Override
        public ClientAuthentication getClientAuthentication() {
            return RequestBodyAuthenticationScheme.instance();
        }

    }

    private final Oauth2CodeClientConfig config;
    private final OpenIdConfigurationApi openIdConfig;

    AuthCodeClient(Oauth2CodeClientConfig config) {
        this.config = config;
        this.openIdConfig = new OpenIdConfigurationApi(config.getTokenEndpoint(), config.getAuthorizationEndpoint());
    }

    AuthResult requestToken() {
        try (AuthCodeProvider codeProvider =
                 new SystemBrowserAuthCodeProvider(config.getRedirectPort(), AUTHORIZATION_TIMEOUT);
             OAuth20Service service = openClient(codeProvider)) {

            String state = generateState();
            AuthorizationUrlBuilder authorizationUrlBuilder = service.createAuthorizationUrlBuilder();
            if (config.isValidateState()) {
                authorizationUrlBuilder = authorizationUrlBuilder.state(state);
            }
            if (config.isWithPkce()) {
                authorizationUrlBuilder = authorizationUrlBuilder.initPKCE();
            }
            if (config.getAdditionalParams() != null && !config.getAdditionalParams().isEmpty()) {
                authorizationUrlBuilder = authorizationUrlBuilder.additionalParams(config.getAdditionalParams());
            }

            String authorizationUrl = authorizationUrlBuilder.build();
            AuthCodeResult code = codeProvider.requestCode(authorizationUrl);
            if (config.isValidateState()) {
                if (!state.equals(code.state())) {
                    throw new RuntimeException("Invalid state received: " + code.state() + "; required: " + state);
                }
            }

            OAuth2AccessToken token;
            if (config.isWithPkce()) {
                token = service.getAccessToken(
                    AccessTokenRequestParams.create(code.code())
                        .pkceCodeVerifier(authorizationUrlBuilder.getPkce().getCodeVerifier())
                );
            } else {
                token = service.getAccessToken(code.code());
            }

            return new AuthResult(
                TokenUtils.extractUserName(token.getAccessToken(), config.getUsernameClaim()),
                token.getAccessToken(),
                token.getRefreshToken(),
                token.getExpiresIn()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String generateState() {
        return "state" + (long) (Math.random() * 1_000_000L);
    }

    AuthResult refreshToken(String refreshToken) {
        try (OAuth20Service service = openClient()) {

            OAuth2AccessToken token = service.refreshAccessToken(refreshToken);
            return new AuthResult(
                TokenUtils.extractUserName(token.getAccessToken(), config.getUsernameClaim()),
                token.getAccessToken(),
                token.getRefreshToken(),
                token.getExpiresIn()
            );
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private OAuth20Service openClient() {
        return new ServiceBuilder(config.getClientId())
            .defaultScope(config.getScope())
            .httpClientConfig(
                JDKHttpClientConfig.defaultConfig()
                    .withConnectTimeout(config.getConnectTimeoutMs())
                    .withReadTimeout(config.getReadTimeoutMs())
            )
            .build(openIdConfig);
    }

    private OAuth20Service openClient(AuthCodeProvider codeProvider) {
        return new ServiceBuilder(config.getClientId())
            .defaultScope(config.getScope())
            .callback("http://localhost:" + codeProvider.redirectPort())
            .httpClientConfig(
                JDKHttpClientConfig.defaultConfig()
                    .withConnectTimeout(config.getConnectTimeoutMs())
                    .withReadTimeout(config.getReadTimeoutMs())
            )
            .build(openIdConfig);
    }

}
