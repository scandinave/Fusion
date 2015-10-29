/**
 * 
 */
package dbunit;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;

import dbunit.bdd.RowLiquibaseDatabasechangelogBDD;
import dbunit.bdd.TableBDD;
import dbunit.generators.LiquibaseGen;
import dbunit.generators.PurgeGen;
import dbunit.worker.AbstractWorker;
import exception.RequeteException;
import exception.FusionException;

/**
 * Cleans all data from a database. This process don't alter the database structure.
 * @author Scandinave
 */
public class Cleaner implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 142577290372523301L;

    /**
     * Logger de la classe.
     */
    private Log LOGGER = LogFactory.getLog(Cleaner.class);
    /**
     * Connection to the database.
     */
    private IDatabaseConnection databaseConnect;
    /**
     * Application worker.
     */
    private AbstractWorker abstractWorker;
    /**
     * If true a Liquibase update will be performed.
     */
    private boolean avecLiquibase;

    /**
     * List of table to process.
     */
    private Set<TableBDD> tables;

    /**
     * Instantiates Cleaner.
     * @param databaseConnect Connection to the database.
     * @param abstractWorker Application worker.
     * @param avecLiquibase If true a Liquibase update will be performed.
     */
    public Cleaner(IDatabaseConnection databaseConnect, AbstractWorker abstractWorker, boolean avecLiquibase) {
        this.databaseConnect = databaseConnect;
        this.abstractWorker = abstractWorker;
        this.avecLiquibase = avecLiquibase;
    }

    /**
     * Starts the clean process.
     * @throws FusionException
     */
    public void start() throws FusionException {
        construction();
        execution();
    }

    /**
     * Executes the clean process.
     * @param avecLiquibase
     * @throws FusionException
     */
    public void execution() throws FusionException {
        abstractWorker.load(abstractWorker.xmlFilePurge, DatabaseOperation.DELETE_ALL);

        LOGGER.info("Mise à jour des séquences à 1");
        abstractWorker.cleanSequence();

        if (avecLiquibase) {
            LOGGER.info("Insertion données liquibase");
            abstractWorker.load(abstractWorker.xmlFileLiquibase, DatabaseOperation.INSERT);
        }
    }

    /**
     * Builds the files that are necessary to the clean process.
     * @throws FusionException
     */
    private void construction() throws FusionException {
        if (avecLiquibase) {
            LOGGER.debug("Construction du fichier flatXmlDataSet liquibase dans le cas où liquibase est lancé en même temps que le serveur");
            LiquibaseGen liquibaseGen = new LiquibaseGen(abstractWorker.xmlFileLiquibase, false, 0);
            liquibaseGen.setSetRowsLiquibaseDatabasechangelog(getRowsLiquibaseDatabasechangelog());
            liquibaseGen.start();
        }
        LOGGER.debug("Construction du fichier flatXmlDataSet de purge (le remplace le cas échéant)");

        tables = abstractWorker.getAllTablesTypeTable();

        PurgeGen purgeGen = new PurgeGen(abstractWorker.xmlFilePurge, false, 0);
        purgeGen.setSetTables(tables);
        purgeGen.start();
    }

    /**
     * Returns the list of row from the liquibase table.
     * @return The list of row from the liquibase table.
     * @throws FusionException
     */
    private Set<RowLiquibaseDatabasechangelogBDD> getRowsLiquibaseDatabasechangelog() throws FusionException {
        RowLiquibaseDatabasechangelogBDD attribut = null;
        Set<RowLiquibaseDatabasechangelogBDD> attributs = new TreeSet<RowLiquibaseDatabasechangelogBDD>();
        TableBDD databasechangelog = new TableBDD(abstractWorker.liquibaseSchemaName, abstractWorker.liquibaseDatabasechangelogName);
        Map<String, Object> colonnes = new HashMap<String, Object>();
        try {
            String sql = "SELECT * "
                + "FROM " + databasechangelog.getNomSchema() + "." + databasechangelog.getNomTable();

            PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            String name;
            Object value;
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                colonnes = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    name = resultSet.getMetaData().getColumnName(i + 1);
                    value = resultSet.getObject(i + 1);
                    if (value != null && !value.equals("")) {
                        colonnes.put(name, value);
                    }
                    attribut = new RowLiquibaseDatabasechangelogBDD(databasechangelog, colonnes);
                }
                attributs.add(attribut);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new FusionException(new RequeteException(e));
        }
        return attributs;
    }

    /**
     * Returns true if liquibase is enabled on the datatable. false otherwise.
     * @return true if liquibase is enabled on the datatable. false otherwise.
     */
    public boolean isAvecLiquibase() {
        return avecLiquibase;
    }

    /**
     * True to enable liquibase support. False to disable it.
     * @param avecLiquibase the avecLiquibase to set
     */
    public void setAvecLiquibase(boolean avecLiquibase) {
        this.avecLiquibase = avecLiquibase;
    }

    /**
     * Returns the list of table to process.
     * @return the tables
     */
    public Set<TableBDD> getTables() {
        return tables;
    }

    /**
     * Changes the list of table to process.
     * @param tables the tables to set
     */
    public void setTables(Set<TableBDD> tables) {
        this.tables = tables;
    }

}
