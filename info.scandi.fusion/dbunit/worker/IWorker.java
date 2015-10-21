/**
 * 
 */
package dbunit.worker;

import java.io.File;
import java.io.IOException;

import org.dbunit.operation.DatabaseOperation;

import exception.TatamiException;

/**
 * Permet de controller et d'effectuer des opérations sur une base de donnée pour un projet définis. Cette classe peut être implémenter pour chaque projet. En
 * rêgle général les projets choisiront d'étendre la classe BDDWorker pour plus de faciliter.
 * @author Scandinave
 */
public interface IWorker {

    /**
     * Initialise le worker avec les données issue du fichier properties.
     * @throws TatamiException
     */
    void init() throws TatamiException;

    /**
     * Démarre le processus d'initialisation de la base de donnée avant les tests. Cette méthode est appelée automatique par le runner de cucumber.
     * @throws TatamiException
     */
    void start() throws TatamiException;

    /**
     * Point d'ancrage exécuté à la fin des tests. Après la fermeture du driver. Surchargez cette méthode pour ajouter des comportements à la fin des tests.
     * @throws TatamiException
     */
    void stop() throws TatamiException;

    /**
     * Vide l'ensemble des tables de la base de données sans les supprimer.
     * @throws TatamiException
     */
    void clean() throws TatamiException;

    /**
     * Fait un clean puis initialise la base de données avec les données de référence nécessaire au démarrage du projet.
     * @throws TatamiException
     */
    void reset() throws TatamiException;

    /**
     * Charge un fichier xml et son contenu en base de données. DatabaseOperation.INSERT par défaut.
     * @param filePath
     * @throws TatamiException
     */
    void load(String filePath) throws TatamiException;

    /**
     * Charge un fichier xml et son contenu en base de données.
     * @param filePath
     * @param operation
     * @throws TatamiException
     */
    void load(String filePath, DatabaseOperation operation) throws TatamiException;

    /**
     * Charge un fichier xml et son contenu en base de données. DatabaseOperation.INSERT par défaut.
     * @param file
     * @throws TatamiException
     */
    void load(File file) throws TatamiException;

    /**
     * Permet d'activer ou de désactiver les contraintes sur une base de donnée.
     * @param toogle
     * @throws TatamiException
     */
    void toogleContrainte(boolean toogle) throws TatamiException;

    /**
     * Méthode appelée automatiquement après une purge pour réinitialiser les séquences à 0. correspondante.
     * @throws TatamiException
     */
    void cleanSequence() throws TatamiException;

    /**
     * Traite un fichier xml et son contenu en base de données suivant l'operation indiquée.
     * @param file
     * @param operation
     * @throws TatamiException
     * @throws IOException
     */
    void load(File file, DatabaseOperation operation) throws TatamiException;

    /**
     * Sauvegarde les tables de la base de données dans des fichiers XML. (Une table pour un fichier nomschema.nomtable.xml).
     * @throws TatamiException
     */
    void save() throws TatamiException;

    /**
     * Restaure la base de données à partir des fichiers XML de la sauvegarde.
     * @throws TatamiException
     */
    void restore() throws TatamiException;

}
