/**
 * 
 */
package exception;

/**
 * @author Scandinave
 */
@SuppressWarnings("serial")
public class TatamiException extends Exception {

    /**
     * @param e
     */
    public TatamiException(Exception e) {
        super(e);
    }

    /**
     * @param message
     */
    public TatamiException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public TatamiException(String message, Exception e) {
        super(message, e);
    }
}
