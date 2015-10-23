package runner;
/**
 * 
 */


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;
import dbunit.worker.IWorker;
import exception.ConfigurationException;
import exception.FusionException;
import exception.UtilitaireException;
import utils.PropsUtils;

/**
 * Classe lançant les tests. Pour l'utiliser, Ajouter l'annotation @RunWith(Runner.class) à chaque suite de test.
 * @author Scandinave
 * @see org.junit.runner.RunWith
 */
public class Runner extends Cucumber {

    /**
     * @param clazz
     * @throws InitializationError
     * @throws IOException
     */
    public Runner(Class<?> clazz) throws InitializationError, IOException {
        super(clazz);
    }

    private final static Log LOGGER = LogFactory.getLog(Runner.class);
    private static final String TATAMI_WORKER = "tatami.worker";

    /*
     * (non-Javadoc)
     * @see cucumber.api.junit.Cucumber#run(org.junit.runner.notification.RunNotifier)
     */
    @Override
    public void run(RunNotifier notifier) {
    	IWorker worker = null;
        try {
            worker = getWorker();
            worker.start();

            LOGGER.info("Ouverture du driver.");
            ConnectionFactory.getDriver();
            
            super.run(notifier);
            
            LOGGER.info("Fermeture du driver.");
            ConnectionFactory.closeDriver();

            
        } catch (FusionException e) {
        	try {
        		worker.stop();
        	} catch(FusionException e2) {
        		throw new RuntimeException(e2);
        	}
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ConfigurationException e) {
			throw new RuntimeException("Configuration error", e);
		}
    }

    /**
     * Méthode getWorker.
     * @return
     * @throws ConfigurationException
     */
    private IWorker getWorker() throws FusionException {
        Class<?> clazz;
        try {
            clazz = getTatamiWorker();
        } catch (ConfigurationException e) {
            throw new FusionException(e);
        }

        IWorker worker = getInstance(clazz);
        return worker;
    }

    /**
     * Méthode getTatamiWorker.
     * @return
     * @throws ConfigurationException
     * @throws FusionException
     */
    private Class<?> getTatamiWorker() throws ConfigurationException, FusionException {
        Class<?> tatamiWorker = null;
        try {
            tatamiWorker = Class.forName(PropsUtils.getProperties().getProperty(TATAMI_WORKER));
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Problème de configuration pour " + TATAMI_WORKER, e);
        } catch (UtilitaireException e) {
            throw new FusionException(e);
        }
        return tatamiWorker;
    }

    /**
     * Méthode getConstructeur.
     * @param clazz
     * @return
     * @throws FusionException
     */
    private Constructor<?> getConstructeur(Class<?> clazz) throws FusionException {
        Constructor<?> cons;
        try {
            cons = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new FusionException(e);
        } catch (SecurityException e) {
            throw new FusionException(e);
        }
        return cons;
    }

    /**
     * Méthode getInstance.
     * @param clazz
     * @return
     * @throws FusionException
     */
    private IWorker getInstance(Class<?> clazz) throws FusionException {
        IWorker worker;
        try {
            Constructor<?> cons = getConstructeur(clazz);
            cons.setAccessible(true);
            worker = (IWorker) cons.newInstance();
            Method instance = clazz.getDeclaredMethod("getInstance", (Class<?>[]) null);
            worker = (IWorker) instance.invoke(worker, new Object[] {});
        } catch (Exception e) {
            throw new FusionException(e);
        }
        return worker;
    }
}
