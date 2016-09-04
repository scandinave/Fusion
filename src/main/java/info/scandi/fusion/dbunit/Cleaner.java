/**
 * 
 */
package info.scandi.fusion.dbunit;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.dbunit.database.IDatabaseConnection;

import info.scandi.fusion.dbunit.bdd.TableBDD;
import info.scandi.fusion.dbunit.worker.AbstractWorker;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.RequestException;
import info.scandi.fusion.utils.Worker;

/**
 * Cleans all data from a database. This process don't alter the database
 * structure.
 * 
 * @author Scandinave
 */
@Named
@ApplicationScoped
public class Cleaner implements Serializable {

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

	private static final String COMMAND_SQL = "TRUNCATE ";
	private static final String SEPARATEUR_SQL = ", ";
	private static final String END_COMMAND_SQL = " RESTART IDENTITY CASCADE";

	private String[] exclusionSchemas;
	private String[] exclusionTables;

	/**
	 * List of table to process.
	 */
	private Set<TableBDD> tables;

	/**
	 * Starts the clean process.
	 * 
	 * @throws FusionException
	 */
	public void start(String[] exclusionSchemas, String[] exclusionTables) throws FusionException {
		this.exclusionSchemas = exclusionSchemas;
		this.exclusionTables = exclusionTables;
		tables = abstractWorker.getTablesTypeTableWithExclusions(exclusionSchemas, exclusionTables);
		execution();
	}

	/**
	 * Executes the clean process.
	 * 
	 * @param withLiquibase
	 * @throws FusionException
	 */
	public void execution() throws FusionException {
		abstractWorker.cleanSequence();
		emptyBase();
	}

	private void emptyBase() throws FusionException {
		LOGGER.fine("purging the database");
		// Empty database only if table exist in this database
		if (!tables.isEmpty()) {
			int i = 1;
			String table;
			boolean mustBeEmpty = true;
			StringBuilder sql = new StringBuilder();
			sql.append(COMMAND_SQL);
			for (TableBDD tableBDD : tables) {
				mustBeEmpty = true;
				table = tableBDD.getSchemaName() + "." + tableBDD.getTableName();
				if (exclusionSchemas != null) {
					for (int j = 0; j < exclusionSchemas.length; j++) {
						String schema = exclusionSchemas[j];
						if (schema.equals(tableBDD.getSchemaName())) {
							mustBeEmpty = false;
						}
					}
				}

				if (exclusionTables != null) {
					for (int j = 0; j < exclusionTables.length; j++) {
						String schemaDotTable = exclusionTables[j];
						if (schemaDotTable.equals(table)) {
							mustBeEmpty = false;
						}
					}
				}

				if (mustBeEmpty) {
					sql.append(table);
					if (i < tables.size()) {
						sql.append(SEPARATEUR_SQL);
					}
				}
				i++;
			}
			String lastTwoChar = sql.substring(sql.length() - 2);
			if (SEPARATEUR_SQL.equals(lastTwoChar)) {
				sql.delete(sql.length() - 2, sql.length());
			}
			sql.append(END_COMMAND_SQL);
			try (Statement statement = databaseConnect.getConnection().createStatement()) {
				statement.executeUpdate(sql.toString());
			} catch (SQLException e) {
				throw new FusionException(new RequestException(e));
			}
		}
	}

	/**
	 * Returns the list of table to process.
	 * 
	 * @return the tables
	 */
	public Set<TableBDD> getTables() {
		return tables;
	}

	/**
	 * Changes the list of table to process.
	 * 
	 * @param tables
	 *            the tables to set
	 */
	public void setTables(Set<TableBDD> tables) {
		this.tables = tables;
	}

}
