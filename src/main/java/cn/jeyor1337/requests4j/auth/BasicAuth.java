package cn.jeyor1337.requests4j.auth;

import cn.jeyor1337.requests4j.Auth;
import cn.jeyor1337.requests4j.Request;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * HTTP Basic Authentication.
 */
public class BasicAuth implements Auth {
    private final String username;
    private final String password;

    public BasicAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void apply(Request request) {
        String credentials = username + ":" + password;
        String encoded = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        request.addHeader("Authorization", "Basic " + encoded);
    }

    public static BasicAuth of(String username, String password) {
        return new BasicAuth(username, password);
    }
}
