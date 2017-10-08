package info.scandi.fusion.core;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.weld.environment.se.WeldContainer;

import cucumber.api.java.ObjectFactory;

/**
 * Class that provide CDI capacities to Cucumber.
 * 
 * @author Scandinave
 */
public class CucumberFactory implements ObjectFactory {

	private WeldContainer weld;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cucumber.api.java.ObjectFactory#start()
	 */
	@Override
	public void start() {
		weld = Runner.getWeldContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cucumber.api.java.ObjectFactory#stop()
	 */
	@Override
	public void stop() {
	}

	@Override
	public boolean addClass(Class<?> clazz) {
		return true;
	}

	@Override
	public <T> T getInstance(Class<T> type) {
		return weld.select(type, new AnnotationLiteral<info.scandi.fusion.utils.Tester>() {
			private static final long serialVersionUID = -4039773026320876671L;
		}).get();
	}

}
