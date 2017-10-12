/**
 * 
 */
package info.scandi.fusion.database.worker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import info.scandi.fusion.exception.FusionException;

/**
 * Default implementation of the Oracle DBMS worker.
 * 
 * @author Scandinave
 */
@Named
@ApplicationScoped
public abstract class AbstractOracleWorker extends AbstractWorker {

	/**
	 * @throws FusionException
	 */
	protected AbstractOracleWorker() throws FusionException {
		throw new FusionException("Not yet implemented");
	}

}
