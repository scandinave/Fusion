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
 * <p>
 * Ajout la fonctionnalité de save and restore
 * </p>
 * <p>
 * Le répertoire par defaut de la sauvegarde est <i>{rootPath}/flatXmlDataSet/save</i>
 * </p>
 * @author Scandinave
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Save {

    /**
     * Le répertoire de sauvegarde de la base de donnée.
     * @return le répertoire de sauvegarde de la base de donnée.
     */
    String directoryPath() default "flatXmlDataSet/save";
}
