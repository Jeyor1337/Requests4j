package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.auth.BasicAuth;
import cn.jeyor1337.requests4j.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for Requests4J library.
 */
public class RequestsTest {

    @Test
    public void testSimpleGet() throws RequestException {
        Response response = Requests.get("https://httpbin.org/get");
        assertEquals(200, response.getStatusCode());
        assertTrue(response.isOk());
        assertNotNull(response.getText());
    }

    @Test
    public void testGetWithParams() throws RequestException {
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");

        Response response = Requests.get("https://httpbin.org/get", params);
        assertEquals(200, response.getStatusCode());

        Map<String, Object> json = response.json();
        assertNotNull(json.get("args"));
    }

    @Test
    public void testPostJson() throws RequestException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John Doe");
        data.put("age", 30);

        Response response = Requests.postJson("https://httpbin.org/post", data);
        assertEquals(200, response.getStatusCode());

        Map<String, Object> json = response.json();
        assertNotNull(json.get("json"));
    }

    @Test
    public void testSession() throws RequestException {
        try (Session session = Requests.session()) {
            session.addHeader("X-Test-Header", "test-value");

            Response response = session.get("https://httpbin.org/headers");
            assertEquals(200, response.getStatusCode());

            String text = response.getText();
            assertTrue(text.contains("X-Test-Header"));
        }
    }

    @Test
    public void testBasicAuth() throws RequestException {
        try (Session session = Requests.session()) {
            session.setAuth(BasicAuth.of("user", "passwd"));

            Response response = session.get("https://httpbin.org/basic-auth/user/passwd");
            assertEquals(200, response.getStatusCode());

            Map<String, Object> json = response.json();
            assertTrue((Boolean) json.get("authenticated"));
        }
    }

    @Test
    public void testHttpError() {
        assertThrows(HTTPError.class, () -> {
            Response response = Requests.get("https://httpbin.org/status/404");
            response.raiseForStatus();
        });
    }

    @Test
    public void testResponseProperties() throws RequestException {
        Response response = Requests.get("https://httpbin.org/get");

        assertNotNull(response.getStatusCode());
        assertNotNull(response.getReason());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getContent());
        assertNotNull(response.getText());
        assertNotNull(response.getEncoding());
    }

    @Test
    public void testRequestBuilder() {
        Request request = new Request("GET", "https://httpbin.org/get");
        request.addHeader("X-Custom", "value")
               .addParam("key", "value")
               .addCookie("session", "123");

        assertEquals("GET", request.getMethod());
        assertEquals("https://httpbin.org/get", request.getUrl());
        assertEquals("value", request.getHeaders().get("X-Custom"));
        assertEquals("value", request.getParams().get("key"));
        assertEquals("123", request.getCookies().get("session"));
    }
}
