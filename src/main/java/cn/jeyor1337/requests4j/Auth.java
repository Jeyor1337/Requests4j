package cn.jeyor1337.requests4j;

/**
 * Authentication interface for HTTP requests.
 */
public interface Auth {
    /**
     * Apply authentication to the request.
     */
    void apply(Request request);
}
