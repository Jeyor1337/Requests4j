package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.exceptions.HTTPError;
import cn.jeyor1337.requests4j.exceptions.JSONDecodeError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The Response object, which contains a server's response to an HTTP request.
 */
public class Response {
    private int statusCode;
    private String reason;
    private Map<String, List<String>> headers;
    private byte[] content;
    private String encoding;
    private HttpURLConnection connection;
    private Request request;
    private List<Response> history;

    private static final Gson gson = new Gson();

    public Response() {
        this.headers = new HashMap<>();
        this.encoding = "UTF-8";
        this.history = new ArrayList<>();
    }

    /**
     * Returns the status code of the response.
     */
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the textual reason of the response status code.
     */
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the response headers.
     */
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    /**
     * Returns a single header value (first occurrence).
     */
    public String getHeader(String name) {
        List<String> values = headers.get(name);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    /**
     * Returns the content of the response, in bytes.
     */
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Returns the content of the response, in text form.
     */
    public String getText() {
        if (content == null) {
            return "";
        }
        return new String(content, Charset.forName(encoding));
    }

    /**
     * Returns the encoding of the response.
     */
    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Returns the parsed JSON-encoded content of the response, if any.
     */
    public <T> T json(Class<T> classOfT) throws JSONDecodeError {
        try {
            String text = getText();
            return gson.fromJson(text, classOfT);
        } catch (JsonSyntaxException e) {
            throw new JSONDecodeError("Failed to decode JSON", e);
        }
    }

    /**
     * Returns the parsed JSON-encoded content as a Map.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> json() throws JSONDecodeError {
        return json(Map.class);
    }

    /**
     * Returns True if status code is less than 400, False otherwise.
     */
    public boolean isOk() {
        return statusCode < 400;
    }

    /**
     * Raises HTTPError if status code indicates an error (4xx or 5xx).
     */
    public Response raiseForStatus() throws HTTPError {
        if (statusCode >= 400) {
            throw new HTTPError(String.format("HTTP %d: %s", statusCode, reason));
        }
        return this;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Returns a list of Response objects from the history of the request (redirects).
     */
    public List<Response> getHistory() {
        return history;
    }

    public void setHistory(List<Response> history) {
        this.history = history;
    }

    public void addHistory(Response response) {
        this.history.add(response);
    }

    /**
     * Reads response content from input stream.
     */
    public void readContent(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }

        buffer.flush();
        this.content = buffer.toByteArray();
    }

    /**
     * Detects and sets encoding from Content-Type header.
     */
    public void detectEncoding() {
        String contentType = getHeader("Content-Type");
        if (contentType != null) {
            String[] parts = contentType.split(";");
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("charset=")) {
                    this.encoding = part.substring(8).trim();
                    return;
                }
            }
        }
        this.encoding = "UTF-8";
    }

    @Override
    public String toString() {
        return String.format("<Response [%d]>", statusCode);
    }
}
