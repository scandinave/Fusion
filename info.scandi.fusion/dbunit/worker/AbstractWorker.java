/**
 * 
 */
package dbunit.worker;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

import dbunit.Cleaner;
import dbunit.Saver;
import dbunit.bdd.TableBDD;
import exception.RequeteException;
import exception.TatamiException;
import runner.ConfigurationManager;

/**
 * Un worker est la classe permettant l'accès et la manipulation des données d'une base de donnée. Toute classe héritant de cette classe doit impérativement
 * implémenter le pattern singleton avec une méthode getInstance();
 * @author Scandinave
 */
public abstract class AbstractWorker implements IWorker {

    /**
     * Logger de la classe.
     */
    private Log LOGGER = LogFactory.getLog(AbstractWorker.class);
    protected IDatabaseConnection databaseConnect;
    private ConfigurationManager manager;

    /**
     * Permet de reférencer tous les paramètres applicables pour l'insertion de données.
     */
    protected Map<String, Object> replacementObjects;

    /**
     * Liste des options.
     */

    /**
     * @throws TatamiException
     */
    protected AbstractWorker() throws TatamiException {
        manager = ConfigurationManager.getInstance();
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#init()
     */
    @Override
    public void init() throws TatamiException {
        LOGGER.info("Initialisation");
        initConnexion();
    }

    /**
     * Méthode initConnexion.
     * @throws TatamiException
     */
    private void initConnexion() throws TatamiException {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, manager.getDatabaseUrl());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, manager.getDatabaseDriver());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, manager.getDatabaseUsername());
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, manager.getDatabasePassword());
        try {
            IDatabaseTester jdbcConnection = new PropertiesBasedJdbcDatabaseTester();
            databaseConnect = jdbcConnection.getConnection();
            // Permet d'inserer des CLOB
            DatabaseConfig config = databaseConnect.getConfig();
            // TODO Enlever la dépendance à posgresql
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
            config.setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
            config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        } catch (Exception e) {
            throw new TatamiException(
                "Un problème est survenu lors de l'initialisation de la connexion dbUnit!",
                e);
        }
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#clean()
     */
    @Override
    public void clean() throws TatamiException {
        new Cleaner(databaseConnect, this, manager.isLiquibaseEnable()).start();
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#start()
     */
    @Override
    public void start() throws TatamiException {
        this.init();
        try {
            databaseConnect.getConnection().setAutoCommit(false);
            this.toogleContrainte(false);
            if (manager.isSaveEnable()) {
                this.save();
            }
            if (manager.isInitialLoad()) {
                this.reset();
            } else {
                this.clean();
            }
            LOGGER.info("Insertion des données de tests");
            this.load(manager.getCommunDir());
            this.toogleContrainte(true);
            databaseConnect.getConnection().commit();
            databaseConnect.getConnection().setAutoCommit(true);
        } catch (Exception e1) {
            try {
                databaseConnect.getConnection().rollback();
            } catch (SQLException e) {
                throw new TatamiException(e);
            }
            throw new TatamiException(e1);
        }
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#reset()
     */
    @Override
    public void reset() throws TatamiException {
        this.clean();
        this.initBddForServer();
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#load(java.lang.String)
     */
    @Override
    public void load(String filePath) throws TatamiException {
        File file = new File(filePath);
        load(file);
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#load(java.lang.String)
     */
    @Override
    public void load(String filePath, DatabaseOperation operation) throws TatamiException {
        File file = new File(filePath);
        load(file, operation);
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#load(java.io.File)
     */
    @Override
    public void load(File file) throws TatamiException {
        this.load(file, DatabaseOperation.INSERT);
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#load(java.io.File)
     */
    @Override
    public void load(File file, DatabaseOperation operation) throws TatamiException {
        // try {
        // databaseConnect.getConnection().setAutoCommit(false);

        if (file.exists()) {
            recursive(file, operation);
        } else {
            String chemin = "";
            try {
                chemin = file.getCanonicalPath();
            } catch (IOException e) {
                throw new TatamiException(String.format(
                    "Le chemin \"%s\" n'existe pas! impossible de traiter les données!", chemin));
            }
        }
        // databaseConnect.getConnection().commit();
        // databaseConnect.getConnection().setAutoCommit(true);
        // } catch (Exception e1) {
        // try {
        // databaseConnect.getConnection().rollback();
        // } catch (SQLException e) {
        // throw new TatamiException(e);
        // }
        // throw new TatamiException(e1);
        // }
    }

    /*
     * (non-Javadoc)
     * @see worker.IWorker#save()
     */
    @Override
    public void save() throws TatamiException {
        new Saver(databaseConnect, this).start();
    }

    /*
     * (non-Javadoc)
     * @see worker.IWorker#restore()
     */
    @Override
    public void restore() throws TatamiException {
        new Cleaner(databaseConnect, this, false).execution();
        LOGGER.info("Insertion des données de la base sauvegardée");
        load(manager.getSaveDirectory(), DatabaseOperation.INSERT);
    }

    /*
     * (non-Javadoc)
     * @see worker.IBDDWorker#stop()
     */
    @Override
    public void stop() throws TatamiException {
        if (manager.isSaveEnable()) {
            try {
                databaseConnect.getConnection().setAutoCommit(false);
                this.toogleContrainte(false);
                this.restore();
                this.toogleContrainte(true);
                databaseConnect.getConnection().commit();
                databaseConnect.getConnection().setAutoCommit(true);
            } catch (Exception e1) {
                try {
                    databaseConnect.getConnection().rollback();
                } catch (SQLException e) {
                    throw new TatamiException(e);
                }
                throw new TatamiException(e1);
            }
        }

        try {
            databaseConnect.close();
        } catch (SQLException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Méthode recursive.
     * @param file
     * @param hasParameters
     * @param operation
     * @throws TatamiException
     */
    private void recursive(File file, DatabaseOperation operation) throws TatamiException {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                recursive(childFile, operation);
            }
        } else {
            dataOperation(file, operation);
        }
    }

    /**
     * Méthode dataOperation.
     * @param file
     * @param operation
     * @throws TatamiException
     */
    private void dataOperation(File file, DatabaseOperation operation) throws TatamiException {
        String ope = "";
        if (operation.equals(DatabaseOperation.CLEAN_INSERT)) {
            ope = "CLEAN_INSERT";
        }
        if (operation.equals(DatabaseOperation.DELETE)) {
            ope = "DELETE";
        }
        if (operation.equals(DatabaseOperation.DELETE_ALL)) {
            ope = "DELETE_ALL";
        }
        if (operation.equals(DatabaseOperation.INSERT)) {
            ope = "INSERT";
        }
        if (operation.equals(DatabaseOperation.NONE)) {
            ope = "NONE";
        }
        if (operation.equals(DatabaseOperation.REFRESH)) {
            ope = "REFRESH";
        }
        if (operation.equals(DatabaseOperation.TRUNCATE_TABLE)) {
            ope = "TRUNCATE_TABLE";
        }
        if (operation.equals(DatabaseOperation.UPDATE)) {
            ope = "UPDATE";
        }
        LOGGER.info("Opération en BDD : " + ope + " fichier : " + file.getName());
        if (file.isFile()) {
            try {
                IDataSet dataSet = getDataSet(file);
                operation.execute(databaseConnect, dataSet);
            } catch (SQLException e) {
                if (e instanceof BatchUpdateException) {
                    SQLException next = ((BatchUpdateException) e).getNextException();
                    throw new TatamiException(new RequeteException(next));
                }
                if (e instanceof SQLException) {
                    throw new TatamiException(new RequeteException(e));
                }
                throw new TatamiException(e);
            } catch (DatabaseUnitException e) {
                SQLException next = ((BatchUpdateException) e.getCause()).getNextException();
                throw new TatamiException(new RequeteException(next));
            }
        }
    }

    /**
     * Permet de créer un dataSet pouvant parser un fichier xml.
     * @return {@link IDataSet}
     * @throws TatamiException
     */
    private IDataSet getBuilder(File file) throws TatamiException {
        FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
        dataSetBuilder.setColumnSensing(true);
        dataSetBuilder.setCaseSensitiveTableNames(true);
        dataSetBuilder.setDtdMetadata(false);
        try {
            // FileInputStream xmlInputFile = new FileInputStream(file);
            FlatXmlDataSet dataSet = dataSetBuilder.build(file);
            // xmlInputFile.close();
            return dataSet;
        } catch (Exception e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Permet de créer un dataSet pouvant parser un fichier xml. Va permettre de remplacer des expressions présentes dans ce fichier pour leurs attribuer des
     * valeurs.
     * @return {@link IDataSet}
     * @throws TatamiException
     */
    private IDataSet getDataSet(File file) throws TatamiException {
        // TODO Factoriser le contenu de cette méthode
        IDataSet dataSet = null;
        int index = manager.getPurgeFilePath().lastIndexOf(file.getName());
        if (index > 0) {
            if (manager.getPurgeFilePath().substring(index).equals(file.getName())) {
                dataSet = getBuilder(file);
            } else {
                // Permet de reférencer tous les paramètres applicables pour l'insertion de données.
                HashMap<String, Object> replacementObjects = new HashMap<String, Object>();
                // Remplace les valeurs "" par valeur null lors d'insertion en BDD.
                if (!manager.allowEmptyString()) {
                    replacementObjects.put("", null);
                }
                dataSet = new ReplacementDataSet(getBuilder(file), replacementObjects, null);
            }
        } else {
            // Permet de reférencer tous les paramètres applicables pour l'insertion de données.
            HashMap<String, Object> replacementObjects = new HashMap<String, Object>();
            // Remplace les valeurs "" par valeur null lors d'insertion en BDD.
            if (!manager.allowEmptyString()) {
                replacementObjects.put("", null);
            }
            dataSet = new ReplacementDataSet(getBuilder(file), replacementObjects, null);
        }
        // dataSet = getBuilder(file);
        return dataSet;
    }

    /**
     * Méthode initBddForServer.
     * @throws Exception
     */
    private void initBddForServer() throws TatamiException {
        LOGGER.info("Insertion des données nécessaires pour le démarrage du l'application");
        load(manager.getInitialLoadFile());
    }

    /**
     * Méthode getTables. Pour les autree type passez par la méthode getTablesParType.
     * @return
     * @throws TatamiException
     */
    public Set<TableBDD> getAllTablesTypeTable() throws TatamiException {
        String[] types = {"TABLE"};
        return getTablesParTypeWithExclusions(types, null, null);
    }

    /**
     * Méthode getTables. Pour les autree type passez par la méthode getTablesParType.
     * @return
     * @throws TatamiException
     */
    public Set<TableBDD> getTablesParType(String[] types) throws TatamiException {
        return getTablesParTypeWithExclusions(types, null, null);
    }

    /**
     * Méthode getTables.
     * @return
     * @throws TatamiException
     */
    public Set<TableBDD> getTablesTypeTableWithExclusions(String[] schemaExclusions, String[] tablesExclusions) throws TatamiException {
        String[] types = {"TABLE"};
        return getTablesParTypeWithExclusions(types, schemaExclusions, tablesExclusions);
    }

    /**
     * Méthode getTablesParType.
     * @param types : TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
     *        "SYNONYM".
     * @throws TatamiException
     */
    public Set<TableBDD> getTablesParTypeWithExclusions(String[] types, String[] schemaExclusions, String[] tablesExclusions) throws TatamiException {
        TableBDD tableBDD;
        String nomTable;
        TreeSet<TableBDD> setTables = new TreeSet<TableBDD>();
        ResultSet schemas = null;
        ResultSet tables = null;
        try {
            DatabaseMetaData meta = databaseConnect.getConnection().getMetaData();
            schemas = meta.getSchemas();
            while (schemas.next()) {
                String nomSchema = schemas.getString("TABLE_SCHEM");
                if (schemaExclusions != null) {
                    for (int i = 0; i < schemaExclusions.length; i++) {
                        String schemaAExclure = schemaExclusions[i];
                        if (!nomSchema.equals(schemaAExclure)) {
                            tables = databaseConnect.getConnection().getMetaData().getTables(null, nomSchema, null, types);
                            while (tables.next()) {
                                nomTable = tables.getString("TABLE_NAME");
                                if (tablesExclusions != null) {
                                    for (int j = 0; j < tablesExclusions.length; j++) {
                                        String tableAExclure = tablesExclusions[j];
                                        if (!nomTable.equals(tableAExclure)) {
                                            tableBDD = new TableBDD(nomSchema, nomTable);
                                            setTables.add(tableBDD);
                                        }
                                    }
                                } else {
                                    tableBDD = new TableBDD(nomSchema, nomTable);
                                    setTables.add(tableBDD);
                                }
                            }
                        }
                    }
                } else {
                    tables = databaseConnect.getConnection().getMetaData().getTables(null, nomSchema, null, types);
                    while (tables.next()) {
                        nomTable = tables.getString("TABLE_NAME");
                        tableBDD = new TableBDD(nomSchema, nomTable);
                        setTables.add(tableBDD);
                    }
                }
            }
        } catch (SQLException e) {
            throw new TatamiException(new RequeteException(e));
        } catch (Exception e) {
            throw new TatamiException(e);
        } finally {
            try {
                schemas.close();
                tables.close();
            } catch (SQLException e) {
                throw new TatamiException(e);
            }
        }
        return setTables;
    }

}
