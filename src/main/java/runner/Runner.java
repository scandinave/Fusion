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
 * Entry point to launch Fsion test. This runner must be use with the JUnit RunWith annotation.
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
    private static final String FUSION_WORKER = "fusion.worker";

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
     * Return the project worker.
     * @return the project worker.
     * @throws ConfigurationException
     */
    private IWorker getWorker() throws FusionException, ConfigurationException {
        Class<?> clazz;
        try {
            clazz = Class.forName(PropsUtils.getProperties().getProperty(FUSION_WORKER));
        } catch (ClassNotFoundException e) {
        	throw new ConfigurationException("Problème de configuration pour " + FUSION_WORKER, e);
		} catch (UtilitaireException e) {
			throw new FusionException(e);
		}

        IWorker worker = getInstance(clazz);
        return worker;
    }

    /**
     * Return the class constructor
     * @param clazz The class from which get the constructor
     * @return the class constructor
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
     * Return the instance of singleton class.
     * @param clazz The class from which get the instance
     * @return the class instance.
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
