package cn.jeyor1337.requests4j.exceptions;

import cn.jeyor1337.requests4j.Request;
import cn.jeyor1337.requests4j.Response;

/**
 * Base exception for all Requests4J exceptions.
 * There was an ambiguous exception that occurred while handling your request.
 */
public class RequestException extends Exception {
    private Response response;
    private Request request;

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(String message, Response response, Request request) {
        super(message);
        this.response = response;
        this.request = request;
    }

    public RequestException(String message, Throwable cause, Response response, Request request) {
        super(message, cause);
        this.response = response;
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
