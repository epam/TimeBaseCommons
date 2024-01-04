package deltix.util.oauth;

import deltix.util.lang.StringUtils;
import org.green.jelly.JsonNumber;
import org.green.jelly.JsonParser;
import org.green.jelly.JsonParserListener;

class GreenJellyTokenResponseParser implements TokenResponseParser {

    private final JsonParser parser = new JsonParser();
    private final Listener listener = new Listener();

    GreenJellyTokenResponseParser() {
        parser.setListener(listener);
    }

    @Override
    public synchronized TokenInfo parse(String response) {
        parser.parseAndEoj(response);
        if (listener.error != null) {
            throw new RuntimeException("Failed to parse response: " + listener.error);
        }

        return listener.token();
    }

    private static class TokenInfoDto implements TokenInfo {

        private final String token;
        private final long expiresInSec;

        public TokenInfoDto(String token, long expiresInSec) {
            this.token = token;
            this.expiresInSec = expiresInSec;
        }

        @Override
        public String accessToken() {
            return token;
        }

        @Override
        public long expiresInSec() {
            return expiresInSec;
        }
    }

    private static class Listener implements JsonParserListener {
        private final int STATE_UNKNOWN               = -1;
        private final int STATE_TOKEN                 = 0;
        private final int STATE_TOKEN_EXPIRATION      = 1;

        private int state = STATE_UNKNOWN;

        private String error;

        private String token;

        private long expiresInSec;

        public TokenInfoDto token() {
            return new TokenInfoDto(token, expiresInSec);
        }

        @Override
        public void onJsonStarted() {
            this.state = STATE_UNKNOWN;
            this.error = null;
            this.token = null;
            this.expiresInSec = 0;
        }

        @Override
        public void onError(String error, int position) {
            this.error = error;
            this.state = STATE_UNKNOWN;
        }

        @Override
        public void onJsonEnded() {
            this.state = STATE_UNKNOWN;
        }

        @Override
        public boolean onObjectStarted() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onObjectMember(CharSequence name) {
            if (StringUtils.equals("access_token", name)) {
                this.state = STATE_TOKEN;
            } else if (StringUtils.equals("expires_in", name)) {
                this.state = STATE_TOKEN_EXPIRATION;
            } else {
                this.state = STATE_UNKNOWN;
            }

            return true;
        }

        @Override
        public boolean onObjectEnded() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onArrayStarted() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onArrayEnded() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onStringValue(CharSequence data) {
            if (state == STATE_TOKEN) {
                token = data.toString();
            }

            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onNumberValue(JsonNumber number) {
            if (state == STATE_TOKEN_EXPIRATION) {
                expiresInSec = number.mantissa();
            }

            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onTrueValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onFalseValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }

        @Override
        public boolean onNullValue() {
            this.state = STATE_UNKNOWN;
            return true;
        }
    }
}
