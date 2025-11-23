package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.exceptions.RequestException;

import java.util.Map;

/**
 * Requests4J - HTTP for Humans
 *
 * This class provides simple, elegant methods for making HTTP requests,
 * inspired by Python's requests library.
 *
 * Example usage:
 * <pre>
 * Response response = Requests.get("https://httpbin.org/get");
 * System.out.println(response.getStatusCode());
 * System.out.println(response.getText());
 * </pre>
 */
public class Requests {

    /**
     * Sends a GET request.
     *
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response get(String url) throws RequestException {
        try (Session session = new Session()) {
            return session.get(url);
        }
    }

    /**
     * Sends a GET request with query parameters.
     *
     * @param url URL for the request
     * @param params query parameters
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response get(String url, Map<String, String> params) throws RequestException {
        try (Session session = new Session()) {
            return session.get(url, params);
        }
    }

    /**
     * Sends a POST request.
     *
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response post(String url) throws RequestException {
        try (Session session = new Session()) {
            return session.post(url);
        }
    }

    /**
     * Sends a POST request with data.
     *
     * @param url URL for the request
     * @param data data to send in the request body
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response post(String url, Object data) throws RequestException {
        try (Session session = new Session()) {
            return session.post(url, data);
        }
    }

    /**
     * Sends a POST request with JSON data.
     *
     * @param url URL for the request
     * @param json JSON object to send
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response postJson(String url, Object json) throws RequestException {
        try (Session session = new Session()) {
            return session.postJson(url, json);
        }
    }

    /**
     * Sends a PUT request.
     *
     * @param url URL for the request
     * @param data data to send in the request body
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response put(String url, Object data) throws RequestException {
        try (Session session = new Session()) {
            return session.put(url, data);
        }
    }

    /**
     * Sends a PATCH request.
     *
     * @param url URL for the request
     * @param data data to send in the request body
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response patch(String url, Object data) throws RequestException {
        try (Session session = new Session()) {
            return session.patch(url, data);
        }
    }

    /**
     * Sends a DELETE request.
     *
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response delete(String url) throws RequestException {
        try (Session session = new Session()) {
            return session.delete(url);
        }
    }

    /**
     * Sends an OPTIONS request.
     *
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response options(String url) throws RequestException {
        try (Session session = new Session()) {
            return session.options(url);
        }
    }

    /**
     * Sends a HEAD request.
     *
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response head(String url) throws RequestException {
        try (Session session = new Session()) {
            return session.head(url);
        }
    }

    /**
     * Constructs and sends a generic request.
     *
     * @param method HTTP method (GET, POST, PUT, DELETE, etc.)
     * @param url URL for the request
     * @return Response object
     * @throws RequestException if request fails
     */
    public static Response request(String method, String url) throws RequestException {
        try (Session session = new Session()) {
            return session.request(method, url);
        }
    }

    /**
     * Creates a new Session object for making requests with persistent settings.
     * Sessions allow you to persist cookies and other settings across requests.
     *
     * Example:
     * <pre>
     * try (Session session = Requests.session()) {
     *     Response r1 = session.get("https://httpbin.org/cookies/set?k1=v1");
     *     Response r2 = session.get("https://httpbin.org/cookies");
     *     // r2 will include the cookie set by r1
     * }
     * </pre>
     *
     * @return new Session object
     */
    public static Session session() {
        return new Session();
    }
}
