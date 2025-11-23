package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.auth.BasicAuth;
import cn.jeyor1337.requests4j.exceptions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Example usage of Requests4J library.
 */
public class Example {
    public static void main(String[] args) {
        System.out.println("=== Requests4J Examples ===\n");

        // Example 1: Simple GET request
        simpleGet();

        // Example 2: GET with parameters
        getWithParams();

        // Example 3: POST with JSON
        postWithJson();

        // Example 4: POST with form data
        postWithFormData();

        // Example 5: Using Sessions
        sessionExample();

        // Example 6: Authentication
        authenticationExample();

        // Example 7: Error handling
        errorHandlingExample();
    }

    private static void simpleGet() {
        System.out.println("1. Simple GET Request:");
        try {
            Response response = Requests.get("https://httpbin.org/get");
            System.out.println("   Status Code: " + response.getStatusCode());
            System.out.println("   Is OK: " + response.isOk());
            System.out.println("   Content-Type: " + response.getHeader("Content-Type"));
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void getWithParams() {
        System.out.println("2. GET Request with Parameters:");
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", "John Doe");
            params.put("age", "30");

            Response response = Requests.get("https://httpbin.org/get", params);
            System.out.println("   Status Code: " + response.getStatusCode());

            Map<String, Object> json = response.json();
            @SuppressWarnings("unchecked")
            Map<String, Object> args = (Map<String, Object>) json.get("args");
            System.out.println("   Parameters received: " + args);
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void postWithJson() {
        System.out.println("3. POST Request with JSON:");
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", "john_doe");
            data.put("email", "john@example.com");
            data.put("age", 30);

            Response response = Requests.postJson("https://httpbin.org/post", data);
            System.out.println("   Status Code: " + response.getStatusCode());

            Map<String, Object> json = response.json();
            System.out.println("   Response URL: " + json.get("url"));
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void postWithFormData() {
        System.out.println("4. POST Request with Form Data:");
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("username", "john_doe");
            formData.put("password", "secret123");

            Response response = Requests.post("https://httpbin.org/post", formData);
            System.out.println("   Status Code: " + response.getStatusCode());

            Map<String, Object> json = response.json();
            System.out.println("   Form data received: " + json.get("form"));
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void sessionExample() {
        System.out.println("5. Using Sessions (Cookie Persistence):");
        try (Session session = Requests.session()) {
            // Add custom header
            session.addHeader("X-Custom-Header", "MyValue");

            // First request sets a cookie
            Response r1 = session.get("https://httpbin.org/cookies/set?session_id=12345");
            System.out.println("   First request status: " + r1.getStatusCode());

            // Second request uses the cookie automatically
            Response r2 = session.get("https://httpbin.org/cookies");
            System.out.println("   Second request status: " + r2.getStatusCode());

            Map<String, Object> json = r2.json();
            System.out.println("   Cookies persisted: " + json.get("cookies"));
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void authenticationExample() {
        System.out.println("6. Basic Authentication:");
        try (Session session = Requests.session()) {
            session.setAuth(BasicAuth.of("user", "passwd"));

            Response response = session.get("https://httpbin.org/basic-auth/user/passwd");
            System.out.println("   Status Code: " + response.getStatusCode());
            System.out.println("   Authenticated: " + response.isOk());

            Map<String, Object> json = response.json();
            System.out.println("   Response: " + json);
            System.out.println();
        } catch (RequestException e) {
            System.err.println("   Error: " + e.getMessage());
        }
    }

    private static void errorHandlingExample() {
        System.out.println("7. Error Handling:");
        try {
            Response response = Requests.get("https://httpbin.org/status/404");
            System.out.println("   Status Code: " + response.getStatusCode());

            // This will throw an HTTPError
            response.raiseForStatus();

        } catch (HTTPError e) {
            System.out.println("   Caught HTTPError: " + e.getMessage());
        } catch (ConnectionError e) {
            System.out.println("   Caught ConnectionError: " + e.getMessage());
        } catch (Timeout e) {
            System.out.println("   Caught Timeout: " + e.getMessage());
        } catch (RequestException e) {
            System.out.println("   Caught RequestException: " + e.getMessage());
        }
        System.out.println();
    }
}
