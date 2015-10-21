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
 * Détermine si des données nécéssaire au démarrage du serveur doivent être chargé.
 * @author Scandinave
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Init {

    /**
     * L'emplacement du fichier contenant les données nécéssaire au démarrage du serveur.
     * @return l'emplacement du fichier contenant les données nécéssaire au démarrage du serveur.
     */
    String filePath() default "flatXmlDataSet/init/init.xml";
}
