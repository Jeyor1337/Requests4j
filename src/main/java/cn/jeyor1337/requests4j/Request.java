package cn.jeyor1337.requests4j;

import java.util.HashMap;
import java.util.Map;

/**
 * A user-created Request object.
 */
public class Request {
    private String method;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Object data;
    private Object json;
    private Auth auth;
    private Map<String, String> cookies;

    public Request() {
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.cookies = new HashMap<>();
    }

    public Request(String method, String url) {
        this();
        this.method = method;
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Request addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Request setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Request addParam(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public Request setData(Object data) {
        this.data = data;
        return this;
    }

    public Object getJson() {
        return json;
    }

    public Request setJson(Object json) {
        this.json = json;
        return this;
    }

    public Auth getAuth() {
        return auth;
    }

    public Request setAuth(Auth auth) {
        this.auth = auth;
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Request setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Request addCookie(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return String.format("<Request [%s]>", method);
    }
}
