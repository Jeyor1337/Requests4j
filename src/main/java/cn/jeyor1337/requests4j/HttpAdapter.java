package cn.jeyor1337.requests4j;

import cn.jeyor1337.requests4j.exceptions.*;
import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP adapter that handles the actual network communication.
 */
public class HttpAdapter {
    private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds
    private static final int MAX_REDIRECTS = 30;
    private static final Gson gson = new Gson();

    private int connectTimeout = DEFAULT_TIMEOUT;
    private int readTimeout = DEFAULT_TIMEOUT;
    private boolean followRedirects = true;
    private boolean verifySSL = true;

    /**
     * Send a request and return a Response.
     */
    public Response send(Request request) throws RequestException {
        try {
            String urlString = buildUrlWithParams(request.getUrl(), request.getParams());
            URL url = new URL(urlString);

            Response response = executeRequest(url, request, 0);
            return response;

        } catch (SocketTimeoutException e) {
            throw new ConnectTimeout("Connection timeout", e);
        } catch (IOException e) {
            throw new ConnectionError("Connection error: " + e.getMessage(), e);
        }
    }

    private Response executeRequest(URL url, Request request, int redirectCount)
            throws RequestException, IOException {

        if (redirectCount > MAX_REDIRECTS) {
            throw new TooManyRedirects("Exceeded maximum redirects: " + MAX_REDIRECTS);
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configure connection
        connection.setRequestMethod(request.getMethod().toUpperCase());
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        connection.setInstanceFollowRedirects(false); // Handle redirects manually

        // Apply authentication
        if (request.getAuth() != null) {
            request.getAuth().apply(request);
        }

        // Set headers
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        // Set cookies
        if (!request.getCookies().isEmpty()) {
            StringBuilder cookieHeader = new StringBuilder();
            for (Map.Entry<String, String> cookie : request.getCookies().entrySet()) {
                if (cookieHeader.length() > 0) {
                    cookieHeader.append("; ");
                }
                cookieHeader.append(cookie.getKey()).append("=").append(cookie.getValue());
            }
            connection.setRequestProperty("Cookie", cookieHeader.toString());
        }

        // Handle request body
        if (request.getData() != null || request.getJson() != null) {
            connection.setDoOutput(true);

            byte[] bodyData;
            if (request.getJson() != null) {
                // Send JSON
                connection.setRequestProperty("Content-Type", "application/json");
                String jsonString = gson.toJson(request.getJson());
                bodyData = jsonString.getBytes(StandardCharsets.UTF_8);
            } else {
                // Send form data
                Object data = request.getData();
                if (data instanceof String) {
                    bodyData = ((String) data).getBytes(StandardCharsets.UTF_8);
                } else if (data instanceof Map) {
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    @SuppressWarnings("unchecked")
                    String formData = encodeFormData((Map<String, String>) data);
                    bodyData = formData.getBytes(StandardCharsets.UTF_8);
                } else {
                    bodyData = data.toString().getBytes(StandardCharsets.UTF_8);
                }
            }

            try (OutputStream os = connection.getOutputStream()) {
                os.write(bodyData);
            }
        }

        // Get response
        Response response = new Response();
        response.setRequest(request);
        response.setConnection(connection);
        response.setStatusCode(connection.getResponseCode());
        response.setReason(connection.getResponseMessage());
        response.setHeaders(connection.getHeaderFields());

        // Handle redirects
        int statusCode = response.getStatusCode();
        if (followRedirects && (statusCode == 301 || statusCode == 302 ||
                                 statusCode == 303 || statusCode == 307 ||
                                 statusCode == 308)) {
            String location = connection.getHeaderField("Location");
            if (location != null) {
                // Store this response in history
                Response historyResponse = new Response();
                historyResponse.setStatusCode(statusCode);
                historyResponse.setReason(response.getReason());
                historyResponse.setHeaders(response.getHeaders());

                // Follow redirect
                URL redirectUrl = new URL(url, location);
                Response finalResponse = executeRequest(redirectUrl, request, redirectCount + 1);
                finalResponse.addHistory(historyResponse);
                return finalResponse;
            }
        }

        // Read response content
        try {
            InputStream inputStream = (statusCode >= 400)
                ? connection.getErrorStream()
                : connection.getInputStream();

            if (inputStream != null) {
                response.readContent(inputStream);
                response.detectEncoding();
            }
        } catch (IOException e) {
            // Ignore errors when reading error stream
        }

        return response;
    }

    private String buildUrlWithParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        boolean hasQuery = url.contains("?");

        for (Map.Entry<String, String> param : params.entrySet()) {
            if (!hasQuery) {
                urlBuilder.append("?");
                hasQuery = true;
            } else {
                urlBuilder.append("&");
            }

            try {
                urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"))
                         .append("=")
                         .append(URLEncoder.encode(param.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return urlBuilder.toString();
    }

    private String encodeFormData(Map<String, String> data) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!first) {
                result.append("&");
            }

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                      .append("=")
                      .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            first = false;
        }

        return result.toString();
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public boolean isVerifySSL() {
        return verifySSL;
    }

    public void setVerifySSL(boolean verifySSL) {
        this.verifySSL = verifySSL;
    }
}
