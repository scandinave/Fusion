/**
 * 
 */
package info.scandi.fusion.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import info.scandi.fusion.core.ConfigurationManager;
import info.scandi.fusion.database.bdd.TableBDD;
import info.scandi.fusion.database.worker.AbstractWorker;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.utils.FileUtil;
import info.scandi.fusion.utils.Worker;

/**
 * Saves the data of the database in xml files. One file by datatable.
 * Tables can be exclude from the generation.
 * 
 * @author Scandinave
 */
@Named
@ApplicationScoped
public class Saver implements Serializable {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 142577290372523301L;

	@Inject
	private Logger LOGGER;

	@Inject
	private IDatabaseConnection databaseConnect;

	@Inject
	@Worker
	private AbstractWorker abstractWorker;

	@Inject
	private ConfigurationManager conf;

	/**
	 * List of tables to save.
	 */
	private Set<TableBDD> tables;

	/**
	 * Starts the save operation.
	 * 
	 * @throws FusionException
	 */
	public void start() throws FusionException {
		if (tables == null) {
			tables = abstractWorker.getAllTablesTypeTable();
		}
		destruction();
		construction();
	}

	/**
	 * Deletes all xml file that was used for the last save.
	 */
	private void destruction() {
		FileUtil.cleanDirectories(conf.getBackupDirectory());
	}

	/**
	 * Writes the data into xml files.
	 * 
	 * @throws FusionException
	 */
	private void construction() throws FusionException {
		LOGGER.info("Début de la sauvegarde de la base de données");
		try {
			Set<TableBDD> setTables = getTables();
			for (Iterator<TableBDD> iterator = setTables.iterator(); iterator.hasNext();) {
				TableBDD tableBDD = iterator.next();
				File file = new File(
						conf.getBackupDirectory() + "/" + tableBDD.getSchemaName() + "." + tableBDD.getTableName() + ".xml");
				File parent = file.getParentFile();
				if (!file.getParentFile().exists()) {
					parent.mkdirs();
				}
				QueryDataSet dataSetParTable = new QueryDataSet(databaseConnect);
				dataSetParTable.addTable(tableBDD.getSchemaName() + "." + tableBDD.getTableName());
				FileOutputStream outputStream;
				outputStream = new FileOutputStream(file);
				FlatXmlDataSet.write(dataSetParTable, outputStream);
				LOGGER.fine(file.getCanonicalPath() + " saved");
			}
		} catch (AmbiguousTableNameException e) {
			throw new FusionException(e);
		} catch (FileNotFoundException e1) {
			throw new FusionException(e1);
		} catch (DataSetException | IOException e2) {
			throw new FusionException(e2);
		}
		LOGGER.info("Backup completed successfully.");
	}

	/**
	 * Returns the list of tables to save.
	 * 
	 * @return the tables
	 */
	public Set<TableBDD> getTables() {
		return tables;
	}

	/**
	 * Changes the list of tables to save.
	 * 
	 * @param tables
	 *            the tables to set
	 */
	public void setTables(Set<TableBDD> tables) {
		this.tables = tables;
	}
}
