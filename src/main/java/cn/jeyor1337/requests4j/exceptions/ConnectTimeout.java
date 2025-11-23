package cn.jeyor1337.requests4j.exceptions;

/**
 * The request timed out while trying to connect to the remote server.
 * Requests that produced this error are safe to retry.
 */
public class ConnectTimeout extends Timeout {
    public ConnectTimeout(String message) {
        super(message);
    }

    public ConnectTimeout(String message, Throwable cause) {
        super(message, cause);
    }
}
