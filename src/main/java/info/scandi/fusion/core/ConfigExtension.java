package info.scandi.fusion.core;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.apache.poi.ss.formula.functions.T;
import org.reflections.Reflections;

import info.scandi.fusion.utils.Driver;
import info.scandi.fusion.utils.Tester;
import info.scandi.fusion.utils.Worker;

public class ConfigExtension implements Extension {

	@SuppressWarnings("unchecked")
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		Reflections reflections = new Reflections();
		Set<Class<?>> types = reflections.getTypesAnnotatedWith(Tester.class);
		types.addAll(reflections.getTypesAnnotatedWith(Driver.class));
		types.addAll(reflections.getTypesAnnotatedWith(Worker.class));
		Set<Class<?>> removable = new HashSet<>();
		types.forEach(type -> {
			if (type.getName().startsWith("info.scandi.fusion.")) {
				removable.add(type);
			}
		});
		types.removeAll(removable);

		types.forEach(type -> {
			abd.addBean(new FusionBean<T>((Class<T>) type, bm));
		});
	}

}
