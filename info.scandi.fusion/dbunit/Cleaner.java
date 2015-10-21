/**
 * 
 */
package dbunit;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import dbunit.xml.Columns;
import exception.RequeteException;
import exception.TatamiException;
import runner.ConfigurationManager;

/**
 * Vide la base de données de toutes ses données.
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
    private IDatabaseConnection databaseConnect;
    private AbstractWorker abstractWorker;
    private boolean avecLiquibase;

    private Set<TableBDD> tables;

    private ConfigurationManager manager;

    /**
     * @param abstractWorker
     * @throws TatamiException
     */
    public Cleaner(IDatabaseConnection databaseConnect, AbstractWorker abstractWorker, boolean avecLiquibase) throws TatamiException {
        this.databaseConnect = databaseConnect;
        this.abstractWorker = abstractWorker;
        this.avecLiquibase = avecLiquibase;
        manager = ConfigurationManager.getInstance();
    }

    /**
     * Méthode start.
     * @throws TatamiException
     */
    public void start() throws TatamiException {
        construction();
        execution();
    }

    /**
     * Méthode execution.
     * @param avecLiquibase
     * @throws TatamiException
     */
    public void execution() throws TatamiException {
        abstractWorker.load(manager.getPurgeFilePath(), DatabaseOperation.DELETE_ALL);

        LOGGER.info("Mise à jour des séquences à 1");
        abstractWorker.cleanSequence();

        if (avecLiquibase) {
            LOGGER.info("Insertion données liquibase");
            abstractWorker.load(manager.getLiquibaseFile(), DatabaseOperation.INSERT);
        }
    }

    /**
     * Méthode construction.
     * @throws TatamiException
     */
    private void construction() throws TatamiException {
        if (avecLiquibase) {
            LOGGER.debug("Construction du fichier flatXmlDataSet liquibase dans le cas où liquibase est lancé en même temps que le serveur");
            LiquibaseGen liquibaseGen = new LiquibaseGen(manager.getLiquibaseFile(), false, 0);
            liquibaseGen.setSetRowsLiquibaseDatabasechangelog(getRowsLiquibaseDatabasechangelog());
            liquibaseGen.start();
        }
        LOGGER.debug("Construction du fichier flatXmlDataSet de purge (le remplace le cas échéant)");

        tables = abstractWorker.getAllTablesTypeTable();

        PurgeGen purgeGen = new PurgeGen(manager.getPurgeFilePath(), false, 0);
        purgeGen.setSetTables(tables);
        purgeGen.start();
    }

    /**
     * Méthode getRowsLiquibaseDatabasechangelog.
     * @return
     * @throws TatamiException
     */
    private Set<RowLiquibaseDatabasechangelogBDD> getRowsLiquibaseDatabasechangelog() throws TatamiException {
        RowLiquibaseDatabasechangelogBDD attribut = null;
        Set<RowLiquibaseDatabasechangelogBDD> attributs = new TreeSet<RowLiquibaseDatabasechangelogBDD>();
        TableBDD databasechangelog = new TableBDD(manager.getLiquibaseSchema(), manager.getLiquibaseChangelogName());
        Columns colonnes = new Columns();
        try {
            String sql = "SELECT * "
                + "FROM " + databasechangelog.getNomSchema() + "." + databasechangelog.getNomTable();

            PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            String name;
            Object value;
            while (resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                colonnes = new Columns();
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
            throw new TatamiException(new RequeteException(e));
        }
        return attributs;
    }

    /**
     * Getter de avecLiquibase.
     * @return the avecLiquibase
     */
    public boolean isAvecLiquibase() {
        return avecLiquibase;
    }

    /**
     * Setter de avecLiquibase.
     * @param avecLiquibase the avecLiquibase to set
     */
    public void setAvecLiquibase(boolean avecLiquibase) {
        this.avecLiquibase = avecLiquibase;
    }

    /**
     * Getter de tables.
     * @return the tables
     */
    public Set<TableBDD> getTables() {
        return tables;
    }

    /**
     * Setter de tables.
     * @param tables the tables to set
     */
    public void setTables(Set<TableBDD> tables) {
        this.tables = tables;
    }

}
