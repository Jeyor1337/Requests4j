package cn.jeyor1337.requests4j.exceptions;

/**
 * Too many redirects.
 */
public class TooManyRedirects extends RequestException {
    public TooManyRedirects(String message) {
        super(message);
    }

    public TooManyRedirects(String message, Throwable cause) {
        super(message, cause);
    }
}
