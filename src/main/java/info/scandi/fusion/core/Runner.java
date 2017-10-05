package info.scandi.fusion.core;
/**
 * 
 */

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;
import info.scandi.fusion.dbunit.worker.IWorker;
import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.FusionException;

/**
 * Entry point to launch Fsion test. This runner must be use with the JUnit
 * RunWith annotation.
 * 
 * @author Scandinave
 * @see org.junit.runner.RunWith
 */
public class Runner extends Cucumber {

	private Logger LOGGER;
	private static WeldContainer weldContainer;

	/**
	 * @param clazz
	 * @throws InitializationError
	 * @throws IOException
	 */
	public Runner(Class<?> clazz) throws InitializationError, IOException {
		super(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cucumber.api.junit.Cucumber#run(org.junit.runner.notification.
	 * RunNotifier)
	 */
	@Override
	public void run(RunNotifier notifier) {
		Weld weld = new Weld();
		weldContainer = weld.initialize();

		IWorker worker = lookup(IWorker.class, new AnnotationLiteral<info.scandi.fusion.utils.Worker>() {
		});
		ConfigurationManager conf = lookup(ConfigurationManager.class, new AnnotationLiteral<Any>() {
		});
		LOGGER = lookup(Logger.class);
		if (conf.getDatabase().isEnabled()) {
			if (worker == null) {
				throw new RuntimeException(
						"Configuration problem. Missing @Worker on the class you want to see acting as worker");
			}
			try {
				worker.init();
			} catch (ConfigurationException | FusionException e) {
				throw new RuntimeException("Configuration problem : " + e);
			}
			try {
				worker.save();
			} catch (FusionException e) {
				throw new RuntimeException("Configuration problem : " + e);
			}
			try {
				worker.start();
			} catch (FusionException e) {
				e.printStackTrace();
				LOGGER.info("Can't start worker !");
				throw new RuntimeException(e);
			}
		}
		super.run(notifier);
		if (conf.getDatabase().isEnabled()) {
			try {
				worker.restore();
			} catch (FusionException e) {
				LOGGER.severe("A problem occurred during restoration of database");
				LOGGER.fine(e.getMessage());
				throw new RuntimeException(e);
			}
			try {
				LOGGER.info("Closing worker...");
				worker.stop();
				LOGGER.info("Worker closed");
			} catch (FusionException e) {
				LOGGER.severe("Can't stop the worker");
				LOGGER.fine(e.getMessage());
				throw new RuntimeException(e);
			}
		}
		weld.shutdown();
	}

	/**
	 * Return the weld container instance created by the runner.
	 * 
	 * @return the weld container instance created by the runner.
	 */
	public static WeldContainer getWeldContainer() {
		return weldContainer;

	}

	@SuppressWarnings("unchecked")
	public static <T> T lookup(Class<T> cls, Annotation... annotations) {
		BeanManager beanManager = CDI.current().getBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(cls, annotations);
		Bean<?> bean = beanManager.resolve(beans);
		CreationalContext<?> context = beanManager.createCreationalContext(bean);
		if (bean != null) {
			return (T) beanManager.getReference(bean, cls, context);
		} else {
			return null;
		}
	}
}
