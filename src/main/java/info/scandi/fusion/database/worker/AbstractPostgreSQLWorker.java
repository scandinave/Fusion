/**
 * 
 */
package info.scandi.fusion.database.worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

import info.scandi.fusion.conf.ExclusionSchemas;
import info.scandi.fusion.core.Fusion;
import info.scandi.fusion.database.bdd.SequenceBDD;
import info.scandi.fusion.database.bdd.TableBDD;
import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.RequestException;
import info.scandi.fusion.exception.UtilitaireException;

/**
 * Default implementation of the PostgreSQL DBMS worker.
 * 
 * @author Scandinave
 */
@Named
@ApplicationScoped
public abstract class AbstractPosgreSQLWorker extends AbstractWorker {

	/**
	 * Default constructor.
	 * 
	 * @throws FusionException
	 */
	protected AbstractPosgreSQLWorker() throws FusionException {
		super();
	}

	@Override
	public void start() throws FusionException {
		super.start();
		this.vacuum();
	}

	@Override
	public void restore() throws FusionException {
		super.restore();
		if (conf.getDatabase().getBackup().isEnabled()) {
			this.vacuum();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dbunit.worker.IBDDWorker#toogleContrainte(boolean)
	 */
	@Override
	public void toogleContrainte(boolean toogle) throws FusionException {
		LOGGER.info(toogle ? "Enabling Constraints" : "Disabling Constraints");
		try {
			String update = "UPDATE pg_trigger SET tgenabled = " + (toogle ? "'O'" : "'D'");
			Statement statement = databaseConnect.getConnection().createStatement();
			statement.executeUpdate(update);
			statement.close();
		} catch (Exception e) {
			throw new FusionException(String.format("problem when %s constraints.", toogle ? "enabling" : "disabling"),
					e);
		}
	}

	/**
	 * Initializes sequence to 0.
	 * 
	 * @throws FusionException
	 */
	@Override
	public void cleanSequence() throws FusionException {
		LOGGER.fine("Updates sequences to 1");
		TreeSet<SequenceBDD> sequences = getSequences(conf.getDatabase().getLiquibase().getExclusionSchemas(), null);
		for (Iterator<SequenceBDD> iterator = sequences.iterator(); iterator.hasNext();) {
			SequenceBDD sequenceBDD = iterator.next();
			setSequence(sequenceBDD, "1");
		}
		LOGGER.fine("Sequences update finish.");
	}

	/**
	 * Synchronizes the sequence with data after table update.
	 * 
	 * @throws FusionException
	 */
	@Override
	public void majSequence() throws FusionException {
		TreeSet<SequenceBDD> sequences = getSequences(conf.getDatabase().getLiquibase().getExclusionSchemas(), null);
		for (SequenceBDD sequence : sequences) {
			setSequence(sequence, "(SELECT MAX(" + sequence.getTableBDD().getPrimaryKey() + ") FROM "
					+ sequence.getSchemaNamePointSequenceName() + ")");
		}
	}

	/**
	 * Returns list of sequences in the database.
	 * 
	 * @return
	 * @throws FusionException
	 */
	// TODO rewrite the loop
	private TreeSet<SequenceBDD> getSequences(ExclusionSchemas exclusionSchemas, String[] exclusionTables)
			throws FusionException {
		String schemaName;
		String sequenceName;
		String primaryKey;
		SequenceBDD sequence;
		TreeSet<SequenceBDD> sequences = new TreeSet<SequenceBDD>();
		String tableName;
		try {
			String sql = "SELECT n.nspname AS schemaname, c.relname as sequencename, t.relname as tablename, a.attname as primaryKey "
					+ "FROM pg_class c " + "JOIN pg_namespace n ON n.oid = c.relnamespace "
					+ "JOIN pg_depend d ON d.objid = c.oid "
					+ "JOIN pg_class t ON d.objid = c.oid AND d.refobjid = t.oid "
					+ "JOIN pg_attribute a ON (d.refobjid, d.refobjsubid) = (a.attrelid, a.attnum) "
					+ "WHERE c.relkind = 'S'";
			PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					schemaName = resultSet.getString("schemaname");
					tableName = resultSet.getString("tableName");
					sequenceName = resultSet.getString("sequencename");
					primaryKey = resultSet.getString("primaryKey");
					if (exclusionSchemas != null) {
						for (String exclusionSchema : exclusionSchemas.getExclusionSchema()) {
							if (!schemaName.equals(exclusionSchema)) {
								if (exclusionTables != null) {
									for (int j = 0; j < exclusionTables.length; j++) {
										String exludeTable = exclusionTables[j];
										if (!tableName.equals(exludeTable)) {
											TableBDD tableBDD = new TableBDD(schemaName, tableName);
											tableBDD.setPrimaryKey(primaryKey);
											sequence = new SequenceBDD(tableBDD, sequenceName);
											sequences.add(sequence);
										}
									}
								} else {
									TableBDD tableBDD = new TableBDD(schemaName, tableName);
									tableBDD.setPrimaryKey(primaryKey);
									sequence = new SequenceBDD(tableBDD, sequenceName);
									sequences.add(sequence);
								}
							}
						}
						;
					} else {
						TableBDD tableBDD = new TableBDD(schemaName, tableName);
						tableBDD.setPrimaryKey(primaryKey);
						sequence = new SequenceBDD(tableBDD, sequenceName);
						sequences.add(sequence);
					}
				}
			} else {
				LOGGER.fine("no sequences to reset");
			}
			statement.close();
		} catch (

		SQLException e) {
			throw new FusionException(new RequestException(e));
		}
		return sequences;
	}

	/**
	 * Reset the sequence to a specific value.
	 * 
	 * @param sequenceBDD
	 *            The sequence to reset.
	 * @param valueStart
	 *            The value to reset the sequence
	 * @throws FusionException
	 */
	private void setSequence(SequenceBDD sequenceBDD, String valueStart) throws FusionException {
		String sql = "SELECT setval('" + sequenceBDD.getSchemaNamePointSequenceName() + "', " + valueStart + ")";
		try (PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql)) {
			statement.executeQuery();
		} catch (SQLException e) {
			throw new FusionException(new RequestException(e));
		}
	}

	protected void vacuum() throws FusionException {
		LOGGER.info("VACUUM FULL");
		String vacuum = "VACUUM FULL";
		try (Statement statement = databaseConnect.getConnection().createStatement()) {
			statement.executeUpdate(vacuum);
		} catch (Exception e) {
			throw new FusionException("can't make a vacum full", e);
		}
	}

	@Produces
	@ApplicationScoped
	public IDatabaseConnection produceDatabaseConnection() throws ConfigurationException {
		LOGGER.info("Initialisation de la connexion");
		try {
			checkDatabaseParameter();
			String datasourceType = conf.getDatabase().getConnectionType();
			IDatabaseTester jdbcConnection = null;
			String databaseDriver = conf.getDatabase().getDriver();
			String databaseUrl = "jdbc:postgresql://" + conf.getDatabase().getHost() + ":"
					+ conf.getDatabase().getPort() + "/" + conf.getDatabase().getName();
			String databaseId = conf.getDatabase().getUsername();
			String databasePassword = conf.getDatabase().getPassword();
			if (datasourceType.equals(Fusion.TYPE_CUSTOM)) { // cas jdbc standard
				LOGGER.info("Connexion depuis propriété jdbc");
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseUrl);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, databaseDriver);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, databaseId);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, databasePassword);
			} else if (datasourceType.equals(Fusion.TYPE_ENV)) { // cas datasource
				LOGGER.info("Connexion depuis variable d'environnement");
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, System.getenv(databaseUrl));
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, databaseDriver);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, System.getenv(databaseId));
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, System.getenv(databasePassword));
			} else {
				throw new ConfigurationException(
						"Aucun type de connexion défini. Veuillez choisir entre 'custom' et 'env'");
			}
			jdbcConnection = new PropertiesBasedJdbcDatabaseTester();
			return jdbcConnection.getConnection();
		} catch (UtilitaireException e) {
			throw new ConfigurationException(
					"Impossible de récupérer les informations de connexion à la base de données.", e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConfigurationException(
					"Un problème est survenu lors de l'initialisation de la connexion à la base de donnée", e);
		}
	}

	/**
	 * Permet de vérifier les paramètre de connexion à la base de donnée en fonction
	 * du type de connexion (env ou custom)
	 * 
	 * @throws ConfigurationException
	 *             Exception levée si un des paramètre de connexion est manquant.
	 */
	private void checkDatabaseParameter() throws ConfigurationException {
		if (conf.getDatabase().getHost() == null) {
			throw new ConfigurationException("The property database.host is missing");
		}
		if (conf.getDatabase().getPort() == null) {
			throw new ConfigurationException("The property database.port is missing");
		}
		if (conf.getDatabase().getName() == null) {
			throw new ConfigurationException("The property database.name is missing");
		}
		if (conf.getDatabase().getDriver() == null) {
			throw new ConfigurationException("The property database.driver is missing");
		}
		if (conf.getDatabase().getUsername() == null) {
			throw new ConfigurationException("The property database.name is missing");
		}
		if (conf.getDatabase().getPassword() == null) {
			throw new ConfigurationException("The property database.password is missing");
		}
	}

}
