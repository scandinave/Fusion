/**
 * 
 */
package dbunit.worker;

import java.io.File;
import java.io.IOException;

import org.dbunit.operation.DatabaseOperation;

import exception.ConfigurationException;
import exception.FusionException;

/**
 * Implementations of this interface are used to control and manipulate data in the database. 
 * The implementation is responsible to load, to purge and clean the data before and / or after the tests.
 * Implementations must be singleton.
 * @author Scandinave
 */
public interface IWorker {

    /**
     * Initializes the worker with the configuration from fusion.properties.
     * @throws ConfigurationException 
     */
    void init() throws ConfigurationException;

    /**
     * Start the initialization process of the database before tests.
     * @throws FusionException
     * @throws ConfigurationException 
     */
    void start() throws FusionException, ConfigurationException;

    /**
     * Restores the database if the save and restore were activated and closes the connection to the database . 
     * Any actions that must be perform after the tests can be done by overriding this method.
     * @throws FusionException
     */
    void stop() throws FusionException;

    /**
     * Empty all tables in the database.
     * @throws FusionException
     */
    void clean() throws FusionException;

    /**
     * Make a clean then initialize the database with reference data required to start the project.
     * @throws FusionException
     */
    void reset() throws FusionException;

    /**
     * Loads the content of a xml file into database.
     * @param filePath Path of the file to load.
     * @throws FusionException
     */
    void load(String filePath) throws FusionException;

    /**
     * Loads the content of a xml file into database.
     * @param filePath Path of the file to load.
     * @param operation Database operation to execute.
     * @throws FusionException
     */
    void load(String filePath, DatabaseOperation operation) throws FusionException;

    /**
     * Loads the content of a xml file into database.
     * @param File to load
     * @throws FusionException
     */
    void load(File file) throws FusionException;

    /**
     * Toggles constraints on a database.
     * @param toogle if true enables constraints, if false disables constraints
     * @throws FusionException
     */
    void toogleContrainte(boolean toogle) throws FusionException;

    /**
     * Initializes sequence to 0.
     * @throws FusionException
     */
    void cleanSequence() throws FusionException;

    /**
     * Loads the content of a xml file into database.
     * @param file File to load
     * @param operation Database operation to execute.
     * @throws FusionException
     */
    void load(File file, DatabaseOperation operation) throws FusionException;

    /**
     * Saves all table into xml files. One file by table.
     * @throws FusionException
     */
    void save() throws FusionException;

    /**
     * Restores the database with the xml files produced during the backup.
     * @throws FusionException
     */
    void restore() throws FusionException;

}
