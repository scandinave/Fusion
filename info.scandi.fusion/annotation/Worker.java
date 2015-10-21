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
 * Définis le worker de l'application. Ce worker est chargé de la gestion de la base de donnée pour tatami.
 * @author Scandinave
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Worker {

    /**
     * Le driver de connexion à la base de donnée.
     * @return le driver de connexion à la base de donnée
     */
    String driver();

    /**
     * L'url de connexion à la base de donnée.
     * @return l'url de connexion à la base de donnée.
     */
    String url();

    /**
     * L'identifiant de connexion à la base de donnée.
     * @return l'identifiant de connexion à la base de donnée.
     */
    String username();

    /**
     * Le mot de passe de connexion à la base de donnée.
     * @return le mot de passe de connexion à la base de donnée.
     */
    String password();

    /**
     * La politique de sauvegarde de la base de donnée.
     * @return Si true sauvegarde avant les tests et restaure après les tests, false sinon.
     */
    boolean avecSauvegarde() default false;

    /**
     * Le répertoire de sauvegarde de la base de donnée. Pris en compte que si l'option avecSauvegarde est à true.
     * @return Le répertoire de sauvegarde de la base de donnée. Par defaut {rootPath}flatXmlDataSet/save.
     */
    String xmlDirectorySave() default "flatXmlDataSet/save";

    /**
     * <p>
     * L'emplacement du fichier de purge.
     * </p>
     * <p>
     * Par defaut <i>{rootPath}/flatXmlDataSet/init/purge.xml</i>
     * </p>
     * @return
     */
    String purgeFilePath() default "flatXmlDataSet/init/purge.xml";

    /**
     * Par défaut les chaines de caractère vide seront remplacé par un null.
     * @return True si les chaines de caractère vide doivent être laissé telle quelle. False sinon.
     */
    boolean allowEmptyString() default false;
}
