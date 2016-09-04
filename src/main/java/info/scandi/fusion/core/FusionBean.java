package info.scandi.fusion.core;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.PassivationCapable;

import org.apache.commons.lang3.ClassUtils;

public class FusionBean<T> implements Bean<T>, Serializable, PassivationCapable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InjectionTarget<T> it;
	private Class<T> bean;
	private BeanManager bm;

	public FusionBean(Class<T> workerClass, BeanManager bm) {
		this.bm = bm;
		bean = workerClass;
		AnnotatedType<T> at = bm.createAnnotatedType(bean);
		// use this to instantiate the class and inject dependencies
		it = bm.createInjectionTarget(at);
	}

	@Override
	public T create(CreationalContext<T> creationalContext) {
		T instance = it.produce(creationalContext);
		it.inject(instance, creationalContext);
		it.postConstruct(instance);
		return instance;
	}

	@Override
	public void destroy(T instance, CreationalContext<T> creationalContext) {
		it.preDestroy(instance);
		it.dispose(instance);
		creationalContext.release();
	}

	@Override
	public Set<Type> getTypes() {
		Set<Type> types = new HashSet<>();
		types.addAll(ClassUtils.getAllInterfaces(bean));
		types.addAll(ClassUtils.getAllSuperclasses(bean));
		return types;
	}

	@Override
	public Set<Annotation> getQualifiers() {
		Set<Annotation> annotations = new HashSet<>();
		for (int i = 0; i < bean.getAnnotations().length; i++) {
			Class<? extends Annotation> possibleQualifier = bean.getAnnotations()[i].annotationType();
			if (bm.isQualifier(possibleQualifier)) {
				annotations.add(bean.getAnnotations()[i]);
			}
		}
		return annotations;
	}

	@Override
	public Class<? extends Annotation> getScope() {
		for (int i = 0; i < bean.getAnnotations().length; i++) {
			Class<? extends Annotation> possibleScope = bean.getAnnotations()[i].annotationType();
			if (bm.isStereotype(possibleScope)) {
				for (Annotation annotation : possibleScope.getAnnotations()) {
					if (bm.isScope(annotation.annotationType())) {
						return annotation.annotationType();
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return bean.getName();
	}

	@Override
	public Set<Class<? extends Annotation>> getStereotypes() {
		Set<Class<? extends Annotation>> stereotypes = new HashSet<>();
		for (int i = 0; i < bean.getAnnotations().length; i++) {
			Class<? extends Annotation> possibleStereotype = bean.getAnnotations()[i].annotationType();
			if (bm.isStereotype(possibleStereotype)) {
				stereotypes.add(possibleStereotype);
			}
		}
		return stereotypes;
	}

	@Override
	public boolean isAlternative() {
		for (int i = 0; i < bean.getAnnotations().length; i++) {
			if (bean.getAnnotations()[i].equals(Alternative.class)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?> getBeanClass() {
		return bean.getClass();
	}

	@Override
	public Set<InjectionPoint> getInjectionPoints() {
		return it.getInjectionPoints();
	}

	@Override
	public boolean isNullable() {
		return false;
	}

	@Override
	public String getId() {
		return UUID.randomUUID().toString();
	}

}