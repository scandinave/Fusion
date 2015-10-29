/**
 * 
 */
package exception;

/**
 * Exception that occur on a Fusion utility class.
 * @author Scandinave
 */
@SuppressWarnings("serial")
public class UtilitaireException extends Exception {

    /**
     * @param e
     */
    public UtilitaireException(Exception e) {
        super(e);
    }

    /**
     * @param message
     */
    public UtilitaireException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public UtilitaireException(String message, Exception e) {
        super(message, e);
    }
}
