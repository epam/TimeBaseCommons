package deltix.util.oauth.authcode;

import java.net.URI;

public class AuthorizationBrowserOptions {

    private String successMessage;

    private String errorMessage;

    private URI successRedirectUri;

    private URI errorRedirectUri;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public URI getSuccessRedirectUri() {
        return successRedirectUri;
    }

    public void setSuccessRedirectUri(URI successRedirectUri) {
        this.successRedirectUri = successRedirectUri;
    }

    public URI getErrorRedirectUri() {
        return errorRedirectUri;
    }

    public void setErrorRedirectUri(URI errorRedirectUri) {
        this.errorRedirectUri = errorRedirectUri;
    }
}
