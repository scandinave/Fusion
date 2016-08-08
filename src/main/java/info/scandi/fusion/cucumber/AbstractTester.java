package info.scandi.fusion.cucumber;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.inject.Inject;

import info.scandi.fusion.selenium.driver.IDriver;
import info.scandi.fusion.utils.Driver;

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
    
    @Inject
    protected Logger logger;
    
    @Inject
    @Driver
    protected IDriver driver;

}
