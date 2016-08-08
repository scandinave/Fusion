/**
 * 
 */
package info.scandi.fusion.exception;

/**
 * Exception that occur when a problem is detected by Fusion.
 * @author Scandinave
 */
@SuppressWarnings("serial")
public class FusionException extends Exception {

    /**
     * @param e
     */
    public FusionException(Exception e) {
        super(e);
    }

    /**
     * @param message
     */
    public FusionException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public FusionException(String message, Exception e) {
        super(message, e);
    }
}
