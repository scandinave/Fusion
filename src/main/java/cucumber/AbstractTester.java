package cucumber;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import runner.ConnectionFactory;
import exception.FusionException;
import selenium.driver.IDriver;

/**
 * Abstract class that provides commons variables and functions for tester implementations.
 * @author Scandinave
 */
public abstract class AbstractTester implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 6220087950122012558L;
    /**
     * Default regex for cucumber steps definitions.
     * This regex accept string in the form "string" or &#60string&#62 
     */
    protected static final String REGTEXT = "(?:\\<|\")([^\"]*)(?:\\>|\")"; 
    protected final Log logger = LogFactory.getLog(this.getClass());
    /**
     * Driver instance use by the tester.
     */
    protected IDriver driver;

    /**
     * @throws FusionException if the driver instantiation failed.
     */
    public AbstractTester() throws FusionException {
        try {
            driver = ConnectionFactory.getDriver();
        } catch (Exception e) {
            throw new FusionException("The driver instantiation failed");
        }
    }

}
