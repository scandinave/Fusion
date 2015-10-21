/**
 * 
 */
package annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Permet l'activiation et la configuration de liquibase sur le projet.
 * @author Scandinave
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Liquibase {

    /**
     * <p>
     * L'emplacement du fichier xml utilisé par liquibase.
     * </p>
     * <p>
     * Par default la valeur est <i>{rootPath}/flatXmlDataSet/init/liquibase.xml</i>
     * </p>
     * @return l'emplacement du fichier xml utilisé par liquibase.
     */
    String xmlFilepath() default "flatXmlDataSet/init/liquibase.xml";

    /**
     * <p>
     * Le schema utilisé par liquibase pour fonctionner
     * </p>
     * <p>
     * Par default la valeur est <i>liquibase</i>
     * </p>
     * @return le schema utilisé par liquibase pour fonctionner
     */
    String schema() default "liquibase";

    /**
     * <p>
     * Le nom de la table utilisé par liquibase pour fonctionner.
     * </p>
     * <p>
     * Par default la valeur est <i>databasechangelog</i>
     * </p>
     * @return
     */
    String changelogName() default "databasechangelog";
}
