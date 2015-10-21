/**
 * 
 */
package dbunit.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import exception.TatamiException;

/**
 * Implémentation d'un worker dédié à Oracle. Les projets utilisant Oracle comme base de données devrait étendre de cette classe.
 * @author Scandinave
 */
public abstract class AbstractOracleWorker extends AbstractWorker {

    /**
     * @throws TatamiException
     */
    protected AbstractOracleWorker() throws TatamiException {
        super();
    }

    /**
     * Logger de la classe.
     */
    private Log LOGGER = LogFactory.getLog(AbstractOracleWorker.class);

}
