/**
 * 
 */
package exception;

/**
 * Exception that occur on a request problem.
 * @author Scandinave
 */
@SuppressWarnings("serial")
public class RequestException extends Exception {

    private static String message = "A problem occur durign the request execution";

    /**
     * @param e
     */
    public RequestException(Exception e) {
        super(message, e);
    }

    /**
     * @param message
     */
    public RequestException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public RequestException(String message, Exception e) {
        super(message, e);
    }
}
