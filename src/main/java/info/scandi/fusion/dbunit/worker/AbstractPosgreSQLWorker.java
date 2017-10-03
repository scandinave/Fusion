/**
 * 
 */
package info.scandi.fusion.dbunit.worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import info.scandi.fusion.conf.ExclusionSchemas;
import info.scandi.fusion.dbunit.bdd.SequenceBDD;
import info.scandi.fusion.dbunit.bdd.TableBDD;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.RequestException;

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

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IWorker#cleanSequence()
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
}
