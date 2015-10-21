/**
 * 
 */
package runner;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import annotation.Init;
import annotation.Liquibase;
import annotation.Save;
import annotation.Worker;
import exception.ConfigurationException;

/**
 * @author Scandinave
 */
public class Scanner extends ClasspathHelper {

    private static Scanner instance;
    Reflections reflections;

    /**
     * @throws ConfigurationException
     */
    private Scanner() {
        reflections = new Reflections();

    }

    public static Scanner getInstance() {
        if (instance == null) {
            instance = new Scanner();
        }
        return instance;
    }

    public Worker getWorker() throws ConfigurationException {
        Set<Class<?>> workers = reflections.getTypesAnnotatedWith(Worker.class);
        Worker annotation = null;
        if (workers.size() > 1) {
            throw new ConfigurationException("Veuillez ne définir qu'une seul classe comme worker. L'annotatation @Worker à été trouvée sur plusieurs classes.");
        } else if (workers.size() == 0) {
            throw new ConfigurationException("Aucun worker de définis. Veuillez Ajouter l'annotation @Worker sur une classe ou bien définir un worker dans le fichier tatami.properties");
        } else {

            for (Class<?> worker : workers) {
                annotation = worker.getAnnotation(Worker.class);
            }
        }
        return annotation;
    }

    public Liquibase getLiquibase() throws ConfigurationException {
        Set<Class<?>> liquibases = reflections.getTypesAnnotatedWith(Liquibase.class);
        Liquibase annotation = null;
        if (liquibases.size() > 1) {
            throw new ConfigurationException("Vous ne pouvez définir qu'une seule fois l'annotation liquibase.");
        } else if (liquibases.size() == 0) {
            return null;
        } else {

            for (Class<?> liquibase : liquibases) {
                annotation = liquibase.getAnnotation(Liquibase.class);
            }
        }
        return annotation;
    }

    public Save getSave() throws ConfigurationException {
        Set<Class<?>> saves = reflections.getTypesAnnotatedWith(Save.class);
        Save annotation = null;
        if (saves.size() > 1) {
            throw new ConfigurationException("Vous ne pouvez définir qu'une seule fois l'annotation save.");
        } else if (saves.size() == 0) {
            return null;
        } else {

            for (Class<?> save : saves) {
                annotation = save.getAnnotation(Save.class);
            }
        }
        return annotation;
    }

    public Init getInit() throws ConfigurationException {
        Set<Class<?>> inits = reflections.getTypesAnnotatedWith(Init.class);
        Init annotation = null;
        if (inits.size() > 1) {
            throw new ConfigurationException("Vous ne pouvez définir qu'une seule fois l'annotation save.");
        } else if (inits.size() == 0) {
            return null;
        } else {

            for (Class<?> init : inits) {
                annotation = init.getAnnotation(Init.class);
            }
        }
        return annotation;
    }
}
