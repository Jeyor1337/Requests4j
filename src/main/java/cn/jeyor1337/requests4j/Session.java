package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.exceptions.RequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * A Session object allows you to persist certain parameters across requests.
 * It also persists cookies across all requests made from the Session instance.
 */
public class Session implements AutoCloseable {
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Auth auth;
    private HttpAdapter adapter;

    public Session() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.adapter = new HttpAdapter();

        // Set default headers
        this.headers.put("User-Agent", "Requests4J/1.0.0");
        this.headers.put("Accept", "*/*");
        this.headers.put("Accept-Encoding", "gzip, deflate");
        this.headers.put("Connection", "keep-alive");
    }

    /**
     * Constructs a Request, prepares it and sends it.
     * Returns Response object.
     */
    public Response request(String method, String url) throws RequestException {
        Request request = new Request(method, url);
        return send(request);
    }

    /**
     * Sends a GET request.
     */
    public Response get(String url) throws RequestException {
        return request("GET", url);
    }

    /**
     * Sends a GET request with parameters.
     */
    public Response get(String url, Map<String, String> params) throws RequestException {
        Request request = new Request("GET", url);
        request.setParams(params);
        return send(request);
    }

    /**
     * Sends a POST request.
     */
    public Response post(String url) throws RequestException {
        return request("POST", url);
    }

    /**
     * Sends a POST request with data.
     */
    public Response post(String url, Object data) throws RequestException {
        Request request = new Request("POST", url);
        request.setData(data);
        return send(request);
    }

    /**
     * Sends a POST request with JSON data.
     */
    public Response postJson(String url, Object json) throws RequestException {
        Request request = new Request("POST", url);
        request.setJson(json);
        return send(request);
    }

    /**
     * Sends a PUT request.
     */
    public Response put(String url, Object data) throws RequestException {
        Request request = new Request("PUT", url);
        request.setData(data);
        return send(request);
    }

    /**
     * Sends a PATCH request.
     */
    public Response patch(String url, Object data) throws RequestException {
        Request request = new Request("PATCH", url);
        request.setData(data);
        return send(request);
    }

    /**
     * Sends a DELETE request.
     */
    public Response delete(String url) throws RequestException {
        return request("DELETE", url);
    }

    /**
     * Sends an OPTIONS request.
     */
    public Response options(String url) throws RequestException {
        return request("OPTIONS", url);
    }

    /**
     * Sends a HEAD request.
     */
    public Response head(String url) throws RequestException {
        return request("HEAD", url);
    }

    /**
     * Sends the request after merging session settings.
     */
    public Response send(Request request) throws RequestException {
        // Merge session headers
        Map<String, String> mergedHeaders = new HashMap<>(this.headers);
        mergedHeaders.putAll(request.getHeaders());
        request.setHeaders(mergedHeaders);

        // Merge session cookies
        Map<String, String> mergedCookies = new HashMap<>(this.cookies);
        mergedCookies.putAll(request.getCookies());
        request.setCookies(mergedCookies);

        // Apply session auth if request doesn't have its own
        if (request.getAuth() == null && this.auth != null) {
            request.setAuth(this.auth);
        }

        // Send request
        Response response = adapter.send(request);

        // Update session cookies from response
        updateCookiesFromResponse(response);

        return response;
    }

    private void updateCookiesFromResponse(Response response) {
        Map<String, java.util.List<String>> headers = response.getHeaders();
        java.util.List<String> setCookieHeaders = headers.get("Set-Cookie");

        if (setCookieHeaders != null) {
            for (String setCookie : setCookieHeaders) {
                parseCookie(setCookie);
            }
        }
    }

    private void parseCookie(String setCookie) {
        // Simple cookie parsing (name=value; ...)
        String[] parts = setCookie.split(";");
        if (parts.length > 0) {
            String[] nameValue = parts[0].split("=", 2);
            if (nameValue.length == 2) {
                this.cookies.put(nameValue[0].trim(), nameValue[1].trim());
            }
        }
    }

    /**
     * Returns session headers.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets session headers.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Adds a header to the session.
     */
    public Session addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Returns session cookies.
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * Sets session cookies.
     */
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    /**
     * Adds a cookie to the session.
     */
    public Session addCookie(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    /**
     * Returns session authentication.
     */
    public Auth getAuth() {
        return auth;
    }

    /**
     * Sets session authentication.
     */
    public Session setAuth(Auth auth) {
        this.auth = auth;
        return this;
    }

    /**
     * Returns the HTTP adapter.
     */
    public HttpAdapter getAdapter() {
        return adapter;
    }

    /**
     * Sets the HTTP adapter.
     */
    public void setAdapter(HttpAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Sets connection timeout in milliseconds.
     */
    public Session setTimeout(int timeout) {
        this.adapter.setConnectTimeout(timeout);
        this.adapter.setReadTimeout(timeout);
        return this;
    }

    @Override
    public void close() {
        // Clean up resources if needed
        this.headers.clear();
        this.cookies.clear();
    }
}
