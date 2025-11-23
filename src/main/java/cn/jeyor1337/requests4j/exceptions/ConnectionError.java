package cn.jeyor1337.requests4j.exceptions;

/**
 * A connection error occurred.
 */
public class ConnectionError extends RequestException {
    public ConnectionError(String message) {
        super(message);
    }

    public ConnectionError(String message, Throwable cause) {
        super(message, cause);
    }
}
