package deltix.util.oauth.utils;

import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;

public class TokenUtils {

    public static String extractUserName(String token, String usernameClaim) {
        Object userNameObj = extractClaim(token, usernameClaim);
        if (userNameObj instanceof CharSequence) {
            return ((CharSequence) userNameObj).toString();
        }

        throw new RuntimeException("Can not extract username from token.");
    }

    public static Object extractClaim(String token, String claim) {
        try {
            return JWTParser.parse(token).getJWTClaimsSet().getClaim(claim);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to decode token", e);
        }
    }
}
