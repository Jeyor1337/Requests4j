package cn.jeyor1337.requests4j.exceptions;

/**
 * Couldn't decode the text into JSON.
 */
public class JSONDecodeError extends RequestException {
    public JSONDecodeError(String message) {
        super(message);
    }

    public JSONDecodeError(String message, Throwable cause) {
        super(message, cause);
    }
}
