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
import exception.TatamiException;
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
        try {
            IWorker worker = getWorker();
            worker.start();

            LOGGER.info("Ouverture du driver.");
            ConnectionFactory.getDriver();
            
            super.run(notifier);
            
            LOGGER.info("Fermeture du driver.");
            ConnectionFactory.closeDriver();

            worker.stop();
        } catch (TatamiException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode getWorker.
     * @return
     * @throws ConfigurationException
     */
    private IWorker getWorker() throws TatamiException {
        Class<?> clazz;
        try {
            clazz = getTatamiWorker();
        } catch (ConfigurationException e) {
            throw new TatamiException(e);
        }

        IWorker worker = getInstance(clazz);
        return worker;
    }

    /**
     * Méthode getTatamiWorker.
     * @return
     * @throws ConfigurationException
     * @throws TatamiException
     */
    private Class<?> getTatamiWorker() throws ConfigurationException, TatamiException {
        Class<?> tatamiWorker = null;
        try {
            tatamiWorker = Class.forName(PropsUtils.getProperties().getProperty(TATAMI_WORKER));
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("Problème de configuration pour " + TATAMI_WORKER, e);
        } catch (UtilitaireException e) {
            throw new TatamiException(e);
        }
        return tatamiWorker;
    }

    /**
     * Méthode getConstructeur.
     * @param clazz
     * @return
     * @throws TatamiException
     */
    private Constructor<?> getConstructeur(Class<?> clazz) throws TatamiException {
        Constructor<?> cons;
        try {
            cons = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new TatamiException(e);
        } catch (SecurityException e) {
            throw new TatamiException(e);
        }
        return cons;
    }

    /**
     * Méthode getInstance.
     * @param clazz
     * @return
     * @throws TatamiException
     */
    private IWorker getInstance(Class<?> clazz) throws TatamiException {
        IWorker worker;
        try {
            Constructor<?> cons = getConstructeur(clazz);
            cons.setAccessible(true);
            worker = (IWorker) cons.newInstance();
            Method instance = clazz.getDeclaredMethod("getInstance", (Class<?>[]) null);
            worker = (IWorker) instance.invoke(worker, new Object[] {});
        } catch (Exception e) {
            throw new TatamiException(e);
        }
        return worker;
    }
}
