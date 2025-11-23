package cn.jeyor1337.requests4j.exceptions;

/**
 * The request timed out.
 * Catching this error will catch both ConnectTimeout and ReadTimeout errors.
 */
public class Timeout extends RequestException {
    public Timeout(String message) {
        super(message);
    }

    public Timeout(String message, Throwable cause) {
        super(message, cause);
    }
}
