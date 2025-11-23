package cn.jeyor1337.requests4j.exceptions;

/**
 * The URL provided was somehow invalid.
 */
public class InvalidURL extends RequestException {
    public InvalidURL(String message) {
        super(message);
    }

    public InvalidURL(String message, Throwable cause) {
        super(message, cause);
    }
}
