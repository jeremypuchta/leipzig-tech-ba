package com.baeldung.jwt;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationServerLiveTest {

    public static final String AUTHORIZE_CODE_URL = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/auth?response_type=code&client_id=jwtClient&scope=read&redirect_uri=";
    public static final String TOKEN_URL = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/token";
    public static final String INTROSPECT_URL = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/token/introspect";
    public static final String USER_INFO_URL = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/userinfo";

    public static final String JWT_CLIENT_SECRET = "jwtClientSecret";
    public static final String JWT_CLIENT = "jwtClient";
    public static final String REDIRECT_URL = "http://localhost:8084/";

    public static final String USERNAME = "john@test.com";
    public static final String PASSWORD = "123";
    public static final String OIDC_DISCOVERY_URL = "http://localhost:8083/auth/realms/baeldung/.well-known/openid-configuration";

    @Test
    public void givenAuthorizationCodeGrant_whenObtainAccessToken_thenSuccess() {
        String accessToken = obtainTokens().accessToken;

        assertThat(accessToken).isNotBlank();
    }

    @Test
    public void givenRefreshTokenGrantAndValidRefreshToken_whenObtainAccess_thenSuccess() {

        final String tokenUrl = "http://localhost:8083/auth/realms/baeldung/protocol/openid-connect/token";
        String refreshToken = obtainTokens().refreshToken;
        assertThat(refreshToken).isNotBlank();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", "jwtClient");
        params.put("client_secret", "jwtClientSecret");
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        Response response = RestAssured
          .given()
          .formParams(params)
          .post(tokenUrl);
        assertThat(response
          .jsonPath()
          .getString("access_token")).isNotBlank();
    }

    @Test
    public void givenPasswordGrant_whenObtainAccessToken_thenSuccess() {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", JWT_CLIENT);
        params.put("client_secret", JWT_CLIENT_SECRET);
        params.put("grant_type", "password");
        params.put("username", USERNAME);
        params.put("password", PASSWORD);
        Response response = RestAssured
          .given()
          .formParams(params)
          .post(TOKEN_URL);
        assertThat(response
          .jsonPath()
          .getString("access_token")).isNotBlank();
    }

    @Test
    public void givenAccessTokenWithProfileScope_whenGetUserProfile_thenUsernameIsMatched(){
        String accessToken = obtainTokens().accessToken;
        Response response = RestAssured
          .given()
          .header("Authorization", String.format("Bearer %s", accessToken))
          .get(USER_INFO_URL);
        assertThat(response
          .jsonPath()
          .getString("preferred_username")).isEqualTo(USERNAME);
    }

    @Test
    public void givenAccessToken_whenIntrospect_thenTokenIsActive(){
        String accessToken = obtainTokens().accessToken;
        Response response = RestAssured
          .given()
          .header("Authorization", String.format("Basic %s", new String(Base64.getEncoder().encode((JWT_CLIENT + ":" + JWT_CLIENT_SECRET).getBytes()))))
          .formParam("token", accessToken)
          .post(INTROSPECT_URL);
        assertThat(response
          .jsonPath()
          .getBoolean("active")).isTrue();
    }

    @Test
    public void whenServiceStartsAndLoadsRealmConfigurations_thenOidcDiscoveryEndpointIsAvailable() {
        Response response = RestAssured
          .given()
          .redirects()
          .follow(false)
          .get(OIDC_DISCOVERY_URL);

        assertThat(HttpStatus.OK.value()).isEqualTo(response.getStatusCode());
        System.out.println(response.asString());
        assertThat(response
          .jsonPath()
          .getMap("$.")).containsKeys("issuer", "authorization_endpoint", "token_endpoint", "userinfo_endpoint");
    }

    private Tokens obtainTokens() {
        final String authorizeUrl = AUTHORIZE_CODE_URL + REDIRECT_URL;
        // obtain authentication url with custom codes
        Response response = RestAssured
          .given()
          .redirects()
          .follow(false)
          .get(authorizeUrl);
        String authSessionId = response.getCookie("AUTH_SESSION_ID");
        String kcPostAuthenticationUrl = response
          .asString()
          .split("action=\"")[1].split("\"")[0].replace("&amp;", "&");

        // obtain authentication code and state
        response = RestAssured
          .given()
          .redirects()
          .follow(false)
          .cookie("AUTH_SESSION_ID", authSessionId)
          .formParams("username", USERNAME, "password", PASSWORD, "credentialId", "")
          .post(kcPostAuthenticationUrl);
        assertThat(HttpStatus.FOUND.value()).isEqualTo(response.getStatusCode());

        // extract authorization code
        String location = response.getHeader(HttpHeaders.LOCATION);
        String code = location.split("code=")[1].split("&")[0];

        // get access token
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", "jwtClient");
        params.put("redirect_uri", REDIRECT_URL);
        params.put("client_secret", "jwtClientSecret");
        response = RestAssured
          .given()
          .formParams(params)
          .post(TOKEN_URL);
        return new Tokens(response
          .jsonPath()
          .getString("access_token"), response
          .jsonPath()
          .getString("refresh_token"));
    }

    private static class Tokens {
        private final String accessToken;
        private final String refreshToken;

        public Tokens(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

}
