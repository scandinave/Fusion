package info.scandi.fusion.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;
import javax.inject.Qualifier;

/**
 * Defined a class as a Fusion Driver
 * @author Scandinave
 *
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Named
@ApplicationScoped
@Stereotype
public @interface Driver {

}
