/**
 * 
 */
package exception;

/**
 * @author Nonorc
 */
@SuppressWarnings("serial")
public class ConfigurationException extends Exception {

    private static String message = "Mauvaise configuration";

    /**
     * @param e
     */
    public ConfigurationException(Exception e) {
        super(message, e);
    }

    /**
     * @param message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param e
     */
    public ConfigurationException(String message, Exception e) {
        super(message, e);
    }
}