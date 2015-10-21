/**
 * 
 */
package exception;

/**
 * @author Scandinave
 */
@SuppressWarnings("serial")
public class RequeteException extends Exception {

    private static String message = "Un problème est survenu lors de l'execution de la requête";

    /**
     * @param e
     */
    public RequeteException(Exception e) {
        super(message, e);
    }

    /**
     * @param message
     */
    public RequeteException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public RequeteException(String message, Exception e) {
        super(message, e);
    }
}
