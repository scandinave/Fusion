package cucumber;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import runner.ConnectionFactory;
import exception.TatamiException;
import selenium.driver.IDriver;

/**
 * Classe définissant des tests cucumber générique pour l'ensemble des projets.
 * @author Scandinave
 */
public abstract class AbstractTester implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 6220087950122012558L;
    protected static final String REGTEXT = "(?:\\<|\")([^\"]*)(?:\\>|\")"; // Match
    protected final Log logger = LogFactory.getLog(this.getClass());
    // Todo injecter le driver
    protected IDriver driver;

    /**
     * @throws TatamiException
     */
    public AbstractTester() throws TatamiException {
        try {
            driver = ConnectionFactory.getDriver();
        } catch (Exception e) {
            throw new TatamiException("Impossible d'instancier le Driver");
        }
    }

}
