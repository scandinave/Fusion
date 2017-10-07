package info.scandi.fusion.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.apache.poi.ss.formula.functions.T;

import info.scandi.fusion.utils.Driver;
import info.scandi.fusion.utils.Tester;
import info.scandi.fusion.utils.Worker;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ConfigExtension implements Extension {

	private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@SuppressWarnings("unchecked")
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) throws IOException {
		LOGGER.info("Begin scanning class to find Tester, Driver ans Worker");

		List<Class<?>> types = new ArrayList<Class<?>>();
		FastClasspathScanner fcs = new FastClasspathScanner();
		fcs.matchClassesWithAnnotation(Tester.class, c -> types.add(c));
		LOGGER.fine("Found Tester");
		fcs.matchClassesWithAnnotation(Worker.class, c -> types.add(c));
		LOGGER.fine("Found Driver");
		fcs.matchClassesWithAnnotation(Driver.class, c -> types.add(c));
		LOGGER.fine("Found Worker");
		fcs.scan();
		Set<Class<?>> removable = new HashSet<>();
		types.forEach(type -> {
			if (type.getName().startsWith("info.scandi.fusion.") || type.getName().startsWith("src")) {
				removable.add(type);
			}
		});
		types.removeAll(removable);
		LOGGER.info("Scan finished");

		LOGGER.info("Adding beans to CDI container");
		types.forEach(type -> {
			abd.addBean(new FusionBean<T>((Class<T>) type, bm));
		});
	}
}
