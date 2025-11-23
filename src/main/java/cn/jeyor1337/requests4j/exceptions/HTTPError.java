package cn.jeyor1337.requests4j.exceptions;

/**
 * An HTTP error occurred.
 */
public class HTTPError extends RequestException {
    public HTTPError(String message) {
        super(message);
    }

    public HTTPError(String message, Throwable cause) {
        super(message, cause);
    }
}
