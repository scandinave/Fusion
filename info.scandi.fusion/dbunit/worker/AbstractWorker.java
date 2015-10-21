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
import exception.ConfigurationException;
import exception.RequeteException;
import exception.TatamiException;
import exception.UtilitaireException;
import utils.PropsUtils;

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

    /**
     * Permet de reférencer tous les paramètres applicables pour l'insertion de données.
     */
    protected Map<String, Object> replacementObjects;

    /**
     * Liste des options.
     */
    private final static String ROOT_PATH = "tatami.rootPath";
    private final static String PROPERTY_DATABASE_URL = "database.url";
    private final static String PROPERTY_DATABASE_DRIVER = "database.driver";
    private final static String PROPERTY_DATABASE_USERNAME = "database.username";
    private final static String PROPERTY_DATABASE_PASSWORD = "database.password";

    private final static String PROPERTY_XML_FILE_PURGE = "database.optionalXmlFilePurge";

    private final static String PROPERTY_AVEC_LIQUIBASE = "database.optionalAvecLiquibase";
    private final static String PROPERTY_XML_FILE_LIQUIBASE = "database.optionalXmlFileLiquibase";
    private static final String PROPERTY_LIQUIBASE_SCHEMA_NAME = "database.optionalLiquibaseSchemaName";
    private static final String PROPERTY_LIQUIBASE_DATABASECHANGELOG = "database.optionalLiquibaseDatabasechangelogName";

    private final static String PROPERTY_AVEC_INIT = "database.optionalAvecInit";
    private final static String PROPERTY_XML_FILE_INIT = "database.optionalXmlFileInit";

    private final static String PROPERTY_AVEC_SAUVEGARDE = "database.optionalAvecSauvegarde";
    private final static String PROPERTY_XML_DIRECTORY_SAVE = "database.optionalXmlDirectorySave";

    private final static String PROPERTY_REPLACE_EMPTY_DATABASE_VALUE = "tatami.emptyStringToNull";

    private static String FLATXMLDATASET_DIR;
    public static String COMMUN_DIR;
    public static String DISTINCT_DIR;

    private static String rootPath;
    public String xmlFilePurge;

    public String xmlFileLiquibase;
    public static String xmlFileInit;
    public String xmlDirectorySave;

    public boolean avecLiquibase = false;
    public static boolean avecInit = false;
    public static boolean avecSauvegarde = true;
    public static boolean replaceEmptyDatabaseValue = false;
    public String liquibaseSchemaName = "liquibase";
    public String liquibaseDatabasechangelogName = "databasechangelog";

    /**
     * @throws TatamiException
     */
    protected AbstractWorker() throws TatamiException {
        try {
            rootPath = PropsUtils.getProperties().getProperty(ROOT_PATH);
        } catch (UtilitaireException e) {
            throw new TatamiException(new ConfigurationException(e));
        }
        COMMUN_DIR = getCommunDir();
        DISTINCT_DIR = getDistinctDir();
        optionsParDefaut();
    }

    /**
     * Méthode optionsParDefaut.
     * @throws TatamiException
     */
    private void optionsParDefaut() throws TatamiException {
        xmlFilePurge = FLATXMLDATASET_DIR.concat("/init/purge.xml");

        xmlFileLiquibase = FLATXMLDATASET_DIR.concat("/init/liquibase.xml");
        xmlFileInit = FLATXMLDATASET_DIR.concat("/init/init.xml");
        xmlDirectorySave = FLATXMLDATASET_DIR.concat("/save");
    }

    /**
     * Méthode getCommunDir.
     * @param rootPath
     * @return
     * @throws TatamiException
     * @throws UtilitaireException
     */
    public static String getCommunDir() throws TatamiException {
        FLATXMLDATASET_DIR = rootPath + "flatXmlDataSet";
        COMMUN_DIR = FLATXMLDATASET_DIR.concat("/commun");
        return COMMUN_DIR;
    }

    /**
     * Méthode getCommunDir.
     * @return
     * @throws TatamiException
     * @throws UtilitaireException
     */
    public static String getDistinctDir() throws TatamiException {
        FLATXMLDATASET_DIR = rootPath + "flatXmlDataSet";
        DISTINCT_DIR = FLATXMLDATASET_DIR.concat("/distinct");
        return DISTINCT_DIR;
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#init()
     */
    @Override
    public void init() throws TatamiException {
        LOGGER.info("Initialisation");
        initConnexion();
        initOptions();
    }

    /**
     * Méthode initProperties.
     * @throws TatamiException
     */
    private void initOptions() throws TatamiException {
        String propertyCleanFile;
        try {
            propertyCleanFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_PURGE);

            if (propertyCleanFile != null && !"".equals(propertyCleanFile)) {
                xmlFilePurge = propertyCleanFile;
            }

            String propertyAvecLiquibase = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_LIQUIBASE);
            if (propertyAvecLiquibase != null && !"".equals(propertyAvecLiquibase)) {
                avecLiquibase = "true".equals(propertyAvecLiquibase) ? true : false;
                String propertyLiquibaseFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_LIQUIBASE);
                if (propertyLiquibaseFile != null && !"".equals(propertyLiquibaseFile)) {
                    xmlFileLiquibase = propertyLiquibaseFile;
                }

                String propertyLiquibaseSchemaName = PropsUtils.getProperties().getProperty(PROPERTY_LIQUIBASE_SCHEMA_NAME);
                if (propertyLiquibaseSchemaName != null && !"".equals(propertyLiquibaseSchemaName)) {
                    liquibaseSchemaName = propertyLiquibaseSchemaName;
                }

                String propertyLiquibaseDatabasechangelogName = PropsUtils.getProperties().getProperty(PROPERTY_LIQUIBASE_DATABASECHANGELOG);
                if (propertyLiquibaseDatabasechangelogName != null && !"".equals(propertyLiquibaseDatabasechangelogName)) {
                    liquibaseDatabasechangelogName = propertyLiquibaseDatabasechangelogName;
                }
            }

            String propertyAvecInit = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_INIT);
            if (propertyAvecInit != null && !"".equals(propertyAvecInit)) {
                avecInit = "true".equals(propertyAvecInit) ? true : false;
                String propertyInitFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_INIT);
                if (propertyInitFile != null && !"".equals(propertyInitFile)) {
                    xmlFileInit = propertyInitFile;
                }
            }

            String propertyAvecSauvegarde = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_SAUVEGARDE);
            if (propertyAvecSauvegarde != null && !"".equals(propertyAvecSauvegarde)) {
                avecSauvegarde = "true".equals(propertyAvecSauvegarde) ? true : false;
                String propertySaveFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_DIRECTORY_SAVE);
                if (propertySaveFile != null && !"".equals(propertySaveFile)) {
                    xmlDirectorySave = propertySaveFile;
                }
            }

            String propertyReplaceDatabaseValue = PropsUtils.getProperties().getProperty(PROPERTY_REPLACE_EMPTY_DATABASE_VALUE);
            if (propertyReplaceDatabaseValue != null && !"".equals(propertyReplaceDatabaseValue)) {
                replaceEmptyDatabaseValue = "true".equals(propertyReplaceDatabaseValue) ? true : false;
            }
        } catch (UtilitaireException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Méthode initConnexion.
     * @throws TatamiException
     */
    private void initConnexion() throws TatamiException {
        try {
            System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_URL));
            System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
                PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_DRIVER));
            System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
                PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_USERNAME));

            System.setProperty(
                PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
                PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_PASSWORD));
        } catch (UtilitaireException e) {
            throw new TatamiException(e);
        }
        try {
            IDatabaseTester jdbcConnection = new PropertiesBasedJdbcDatabaseTester();
            databaseConnect = jdbcConnection.getConnection();
            // Permet d'inserer des CLOB
            DatabaseConfig config = databaseConnect.getConfig();
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
     * @see dbunit.worker.IBDDWorker#clean()
     */
    @Override
    public void clean() throws TatamiException {
        new Cleaner(databaseConnect, this, avecLiquibase).start();
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#start()
     */
    @Override
    public void start() throws TatamiException {
        this.init();
        try {
            databaseConnect.getConnection().setAutoCommit(false);
            this.toogleContrainte(false);
            if (avecSauvegarde) {
                this.save();
            }
            if (avecInit) {
                this.reset();
            } else {
                this.clean();
            }
            LOGGER.info("Insertion des données de tests");
            this.load(COMMUN_DIR);
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
     * @see dbunit.worker.IBDDWorker#reset()
     */
    @Override
    public void reset() throws TatamiException {
        this.clean();
        this.initBddForServer();
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#load(java.lang.String)
     */
    @Override
    public void load(String filePath) throws TatamiException {
        File file = new File(filePath);
        load(file);
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#load(java.lang.String)
     */
    @Override
    public void load(String filePath, DatabaseOperation operation) throws TatamiException {
        File file = new File(filePath);
        load(file, operation);
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#load(java.io.File)
     */
    @Override
    public void load(File file) throws TatamiException {
        this.load(file, DatabaseOperation.INSERT);
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#load(java.io.File)
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
     * @see dbunit.worker.IWorker#save()
     */
    @Override
    public void save() throws TatamiException {
        new Saver(databaseConnect, this).start();
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IWorker#restore()
     */
    @Override
    public void restore() throws TatamiException {
        new Cleaner(databaseConnect, this, false).execution();
        LOGGER.info("Insertion des données de la base sauvegardée");
        load(xmlDirectorySave, DatabaseOperation.INSERT);
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#stop()
     */
    @Override
    public void stop() throws TatamiException {
        if (avecSauvegarde) {
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
        IDataSet dataSet = null;
        int index = xmlFilePurge.lastIndexOf(file.getName());
        if (index > 0) {
            if (xmlFilePurge.substring(index).equals(file.getName())) {
                dataSet = getBuilder(file);
            } else {
                // Permet de reférencer tous les paramètres applicables pour l'insertion de données.
                HashMap<String, Object> replacementObjects = new HashMap<String, Object>();
                // Remplace les valeurs "" par valeur null lors d'insertion en BDD.
                if (replaceEmptyDatabaseValue) {
                    replacementObjects.put("", null);
                }
                dataSet = new ReplacementDataSet(getBuilder(file), replacementObjects, null);
            }
        } else {
            // Permet de reférencer tous les paramètres applicables pour l'insertion de données.
            HashMap<String, Object> replacementObjects = new HashMap<String, Object>();
            // Remplace les valeurs "" par valeur null lors d'insertion en BDD.
            if (replaceEmptyDatabaseValue) {
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
        load(xmlFileInit);
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
