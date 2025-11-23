package cn.jeyor1337.requests4j.exceptions;

/**
 * The server did not send any data in the allotted amount of time.
 */
public class ReadTimeout extends Timeout {
    public ReadTimeout(String message) {
        super(message);
    }

    public ReadTimeout(String message, Throwable cause) {
        super(message, cause);
    }
}
