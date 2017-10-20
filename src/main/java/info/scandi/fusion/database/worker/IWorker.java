/**
 * 
 */
package info.scandi.fusion.database.worker;

import java.io.File;
import java.util.List;
import java.util.Map;

import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.FusionException;

/**
 * Implementations of this interface are used to control and manipulate data in
 * the database. The implementation is responsible to load, to purge and clean
 * the data before and / or after the tests. Implementations must be singleton.
 * 
 * @author Scandinave
 */
public interface IWorker {

	/**
	 * Initializes the worker with the configuration from fusion.xml.
	 * 
	 * @throws ConfigurationException
	 * @throws FusionException
	 */
	void init() throws ConfigurationException, FusionException;

	/**
	 * Start the initialization process of the database before tests.
	 * 
	 * @throws FusionException
	 * @throws ConfigurationException
	 */
	void start() throws FusionException;

	/**
	 * Restores the database if the save and restore were activated and closes
	 * the
	 * connection to the database. Also delete all attachment donwload during
	 * test.
	 * Any actions that must be perform after the tests can be done by
	 * overriding
	 * this method.
	 * 
	 * @throws FusionException
	 */
	void stop() throws FusionException;

	/**
	 * Empty all tables in the database.
	 * 
	 * @throws FusionException
	 */
	void clean(List<String> list, List<String> list2) throws FusionException;

	/**
	 * Make a clean then initialize the database with reference data required to
	 * start the project.
	 * 
	 * @throws FusionException
	 */
	void reset() throws FusionException;

	/**
	 * Insert data extract from a xml file
	 * 
	 * @param path
	 *            Path of the file that contain the data.
	 * @throws FusionException
	 */
	void insert(String path) throws FusionException;

	/**
	 * Delete data extract from a xml file
	 * 
	 * @param path
	 *            Path of the file that contain the data.
	 * @throws FusionException
	 */
	void delete(String path) throws FusionException;

	/**
	 * Loads the content of a xml file into database.
	 * 
	 * @param filePath
	 *            Path of the file to load.
	 * @throws FusionException
	 */
	void load(String filePath) throws FusionException;

	/**
	 * Loads the content of a data file(xml, json) file into database.
	 * 
	 * @param File
	 *            to load
	 * @throws FusionException
	 */
	void load(File file) throws FusionException;

	/**
	 * Saves all table into data file(xml, json) files. One file by table.
	 * 
	 * @throws FusionException
	 */
	void save() throws FusionException;

	/**
	 * Restores the database with the data file(xml, json) files produced during
	 * the
	 * backup.
	 * 
	 * @throws FusionException
	 */
	void restore() throws FusionException;

	/**
	 * Return all scenario with their respective paths.
	 * 
	 * @return the allScenarii
	 */
	Map<String, String> getAllScenarii();

}
