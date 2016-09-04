/**
 * 
 */
package info.scandi.fusion.dbunit.worker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.BatchUpdateException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

import info.scandi.fusion.core.Fusion;
import info.scandi.fusion.dbunit.Cleaner;
import info.scandi.fusion.dbunit.Saver;
import info.scandi.fusion.dbunit.bdd.TableBDD;
import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.RequestException;
import info.scandi.fusion.exception.UtilitaireException;
import info.scandi.fusion.utils.PropsUtils;

/**
 * Abstract base class that implements behavior common to all workers.
 * Every workers implementation must extend this class.
 * All worker implementation must implement the singleton pattern.
 * 
 * @author Scandinave
 */
public abstract class AbstractWorker implements IWorker {

	@Inject
	protected Logger LOGGER;
	@Inject
	protected IDatabaseConnection databaseConnect;
	@Inject
	protected Saver saver;
	@Inject
	protected Cleaner clenear;
	@Inject
	protected Cleaner cleanerRestore;

	/**
	 * Element that need to be replaced in xml before the loading into database.
	 */
	protected Map<String, Object> replacementObjects;

	/**
	 * Liste des options.
	 */
	private final static String ROOT_PATH = "fusion.rootPath";

	private final static String PROPERTY_AVEC_LIQUIBASE = "database.optionalAvecLiquibase";
	private static final String PROPERTY_LIQUIBASE_SCHEMA_NAME = "database.optionalLiquibaseSchemaName";
	private static final String PROPERTY_LIQUIBASE_DATABASECHANGELOG = "database.optionalLiquibaseDatabasechangelogName";

	private final static String PROPERTY_AVEC_INIT = "database.optionalAvecInit";
	private final static String PROPERTY_XML_FILE_INIT = "database.optionalXmlFileInit";

	private final static String PROPERTY_AVEC_SAUVEGARDE = "database.optionalAvecSauvegarde";
	private final static String PROPERTY_XML_DIRECTORY_SAVE = "database.optionalXmlDirectorySave";

	private final static String PROPERTY_REPLACE_EMPTY_DATABASE_VALUE = "fusion.emptyStringToNull";

	private String feature_path;

	private static String FLATXMLDATASET_DIR;
	public static String COMMUN_DIR;
	public static String DISTINCT_DIR;

	public static String xmlFileInit;
	private String xmlDirectorySave;

	private boolean withLiquibase = false;
	protected boolean withInit = false;
	protected boolean withSauvegarde = true;
	public static boolean replaceEmptyDatabaseValue = false;
	private String liquibaseSchemaName = "liquibase";
	private String liquibaseDatabasechangelogName = "databasechangelog";
	protected String[] exclusionSchemas;
	protected String[] exclusionTables;

	private Map<String, String> allScenarii = new HashMap<String, String>();

	/**
	 * @throws FusionException
	 */
	protected AbstractWorker() throws FusionException {
		getRootPath();
		COMMUN_DIR = getCommunDir();
		DISTINCT_DIR = getDistinctDir();
		feature_path = getRootPath().concat("scenarii");
		defaultOptions();
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IWorker#getAllScenarii()
	 */
	@Override
	public Map<String, String> getAllScenarii() {
		return allScenarii;
	}

	public String getFeature_path() {
		return feature_path;
	}

	/**
	 * Setter de allScenarii.
	 * 
	 * @param allScenarii
	 *            the allScenarii to set
	 */
	public void setAllScenarii(Map<String, String> allScenarii) {
		this.allScenarii = allScenarii;
	}

	public String getRootPath() throws FusionException {
		try {
			return PropsUtils.getProperties().getProperty(ROOT_PATH);
		} catch (UtilitaireException e) {
			throw new FusionException(new ConfigurationException(e));
		}
	}

	/**
	 * Méthode optionsParDefaut.
	 * 
	 * @throws FusionException
	 */
	private void defaultOptions() throws FusionException {
		xmlFileInit = FLATXMLDATASET_DIR.concat("/init/init.xml");
		xmlDirectorySave = FLATXMLDATASET_DIR.concat("/save");
	}

	/**
	 * Méthode getCommunDir.
	 * 
	 * @param rootPath
	 * @return
	 * @throws FusionException
	 * @throws UtilitaireException
	 */
	public String getCommunDir() throws FusionException {
		FLATXMLDATASET_DIR = getRootPath() + "flatXmlDataSet";
		COMMUN_DIR = FLATXMLDATASET_DIR.concat("/commun");
		return COMMUN_DIR;
	}

	/**
	 * Méthode getCommunDir.
	 * 
	 * @return
	 * @throws FusionException
	 * @throws UtilitaireException
	 */
	public String getDistinctDir() throws FusionException {
		FLATXMLDATASET_DIR = getRootPath() + "flatXmlDataSet";
		DISTINCT_DIR = FLATXMLDATASET_DIR.concat("/distinct");
		return DISTINCT_DIR;
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#init()
	 */
	@Override
	public void init() throws ConfigurationException {
		LOGGER.info("Initialisation");
		initConnexion();
		initOptions();
		try {
			initFeature();
		} catch (IOException e) {
			throw new ConfigurationException(e.getMessage());
		}
	}

	private void initFeature() throws IOException {
		try (Stream<Path> stream = Files.walk(Paths.get(getFeature_path()))) {
			List<String> joined = stream.filter(path -> path.toString().endsWith("feature")).sorted()
					.map(String::valueOf).collect(Collectors.toList());
			joined.forEach(feature -> {
				try {
					List<String> scenarii = Files.readAllLines(Paths.get(feature), StandardCharsets.UTF_8);
					scenarii.forEach(scenario -> {
						if (scenario.trim().startsWith("Scénario:")) {
							allScenarii.put(
									scenario.trim().replaceFirst("Scénario:", "").replace(".feature", "").trim(),
									feature);
						} else if (scenario.trim().startsWith("Plan du scénario:")) {
							allScenarii.put(scenario.trim().replaceFirst("Plan du scénario:", "")
									.replace(".feature", "").trim(), feature);
						}
					});
				} catch (Exception e) {
					// TODO Find a way to throw exception
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * Initializes Fusion properties with the fusion.properties file.
	 * 
	 * @throws FusionException
	 */
	private void initOptions() throws ConfigurationException {
		LOGGER.info("initialization of Fusion base on fusion.properties file.");
		try {
			String propertyAvecLiquibase = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_LIQUIBASE);
			if (propertyAvecLiquibase != null && !"".equals(propertyAvecLiquibase)) {
				withLiquibase = "true".equals(propertyAvecLiquibase) ? true : false;

				String propertyLiquibaseSchemaName = PropsUtils.getProperties()
						.getProperty(PROPERTY_LIQUIBASE_SCHEMA_NAME);
				if (propertyLiquibaseSchemaName != null && !"".equals(propertyLiquibaseSchemaName)) {
					liquibaseSchemaName = propertyLiquibaseSchemaName;
				}

				if (withLiquibase) {
					exclusionSchemas = new String[] { liquibaseSchemaName };
				}

				String propertyLiquibaseDatabasechangelogName = PropsUtils.getProperties()
						.getProperty(PROPERTY_LIQUIBASE_DATABASECHANGELOG);
				if (propertyLiquibaseDatabasechangelogName != null
						&& !"".equals(propertyLiquibaseDatabasechangelogName)) {
					liquibaseDatabasechangelogName = propertyLiquibaseDatabasechangelogName;
				}
			}

			String propertyAvecInit = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_INIT);
			if (propertyAvecInit != null && !"".equals(propertyAvecInit)) {
				withInit = "true".equals(propertyAvecInit) ? true : false;
				String propertyInitFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_INIT);
				if (propertyInitFile != null && !"".equals(propertyInitFile)) {
					xmlFileInit = propertyInitFile;
				}
			}

			String propertyAvecSauvegarde = PropsUtils.getProperties().getProperty(PROPERTY_AVEC_SAUVEGARDE);
			if (propertyAvecSauvegarde != null && !"".equals(propertyAvecSauvegarde)) {
				withSauvegarde = "true".equals(propertyAvecSauvegarde) ? true : false;
				String propertySaveFile = PropsUtils.getProperties().getProperty(PROPERTY_XML_DIRECTORY_SAVE);
				if (propertySaveFile != null && !"".equals(propertySaveFile)) {
					xmlDirectorySave = propertySaveFile;
				}
			}

			String propertyReplaceDatabaseValue = PropsUtils.getProperties()
					.getProperty(PROPERTY_REPLACE_EMPTY_DATABASE_VALUE);
			if (propertyReplaceDatabaseValue != null && !"".equals(propertyReplaceDatabaseValue)) {
				replaceEmptyDatabaseValue = "true".equals(propertyReplaceDatabaseValue) ? true : false;
			}
		} catch (UtilitaireException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * Initializes the database connection.
	 * 
	 * @throws ConfigurationException
	 */
	private void initConnexion() throws ConfigurationException {
		LOGGER.fine("Initialization of the connection");
		try {
			DatabaseConfig config = databaseConnect.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());
			config.setProperty(DatabaseConfig.FEATURE_BATCHED_STATEMENTS, true);
			config.setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
		} catch (Exception e) {
			throw new ConfigurationException("A problem occurred while initializing the connection to the database!",
					e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#clean()
	 */
	@Override
	public void clean() throws FusionException {
		clean(exclusionSchemas, exclusionTables);
	}

	public void clean(String[] exclusionSchemas, String[] exclusionTables) throws FusionException {
		cleanerRestore.start(exclusionSchemas, exclusionTables);
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#start()
	 */
	@Override
	public void start() throws FusionException {
		try {
			LOGGER.info("Starting Fusion...");
			LOGGER.info("Preparation of the database");
			databaseConnect.getConnection().setAutoCommit(false);
			this.toogleContrainte(false);
			if (withInit) {
				this.reset();
			} else {
				this.clean();
			}
			databaseConnect.getConnection().commit();
			LOGGER.info("Inserting test data...");
			this.toogleContrainte(false);
			this.load(COMMUN_DIR);
			this.majSequence();
			this.toogleContrainte(true);
			databaseConnect.getConnection().commit();
			databaseConnect.getConnection().setAutoCommit(true);
		} catch (Exception e1) {
			try {
				databaseConnect.getConnection().rollback();
			} catch (SQLException e) {
				throw new FusionException("Can't rollback the database", e);
			}
			throw new FusionException("Can't start the worker", e1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#reset()
	 */
	@Override
	public void reset() throws FusionException {
		this.clean();
		this.initBddForServer();
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#insert(java.lang.String)
	 */
	@Override
	public void insert(String path) throws FusionException {
		this.toogleContrainte(false);
		LOGGER.info("Inserting data of file : " + path);
		this.load(path, DatabaseOperation.INSERT);
		this.toogleContrainte(true);
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#delete(java.lang.String)
	 */
	@Override
	public void delete(String path) throws FusionException {
		this.toogleContrainte(false);
		LOGGER.info("Deleting data of file : " + path);
		this.load(path, DatabaseOperation.DELETE);
		this.toogleContrainte(true);
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#load(java.lang.String)
	 */
	@Override
	public void load(String filePath, DatabaseOperation operation) throws FusionException {
		File file = new File(filePath);
		load(file, operation);
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#load(java.io.File)
	 */
	@Override
	public void load(File file, DatabaseOperation operation) throws FusionException {
		if (file.exists()) {
			recursive(file, operation);
		} else {
			String path = "";
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				throw new FusionException(String.format("Path \"%s\" doesn't exist! Can't manage data!", path));
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#load(java.io.File)
	 */
	@Override
	public void load(File file) throws FusionException {
		this.load(file, DatabaseOperation.INSERT);
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IWorker#save()
	 */
	@Override
	public void save() throws FusionException {
		if (withSauvegarde) {
			LOGGER.info("Saving database...");
			saver.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IWorker#restore()
	 */
	@Override
	public void restore() throws FusionException {
		if (withSauvegarde) {
			LOGGER.info("Restoring database...");
			try {
				databaseConnect.getConnection().setAutoCommit(false);
				this.toogleContrainte(false);

				Set<TableBDD> tables = getAllTablesTypeTable();
				cleanerRestore.setTables(tables);
				cleanerRestore.execution();

				load(xmlDirectorySave, DatabaseOperation.INSERT);
				this.majSequence();
				this.toogleContrainte(true);
				databaseConnect.getConnection().commit();
				databaseConnect.getConnection().setAutoCommit(true);
			} catch (Exception e) {
				try {
					databaseConnect.getConnection().rollback();
				} catch (SQLException | NullPointerException e1) {
					throw new FusionException("Can't rollback the database.", e1);
				}
				throw new FusionException(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see dbunit.worker.IBDDWorker#stop()
	 */
	@Override
	public void stop() throws FusionException {
		try {
			databaseConnect.close();
			String prop = PropsUtils.getProperties().getProperty(Fusion.DOWNLOAD_DIR);
			if (prop != null && !prop.isEmpty()) {
				File[] pjs = new File(prop).listFiles();
				for (File file : pjs) {
					file.delete();
				}
			}
		} catch (SQLException | UtilitaireException e) {
			throw new FusionException(e);
		}
	}

	/**
	 * Loads recursively all file into a specific folder.
	 * 
	 * @param file
	 *            File or folder to load
	 * @param operation
	 *            Database operation to execute.
	 * @throws FusionException
	 */
	private void recursive(File file, DatabaseOperation operation) throws FusionException {
		if (file.isDirectory() && !".snv".equals(file.getName())) {
			for (File childFile : file.listFiles()) {
				recursive(childFile, operation);
			}
		} else {
			if ("xml".equals(FilenameUtils.getExtension(file.getAbsolutePath()))) {
				dataOperation(file, operation);
			} else {
				LOGGER.warning(file.getAbsolutePath() + " is not a valid file and should not be here.");
			}
		}
	}

	/**
	 * Execute database operation with a file.
	 * 
	 * @param file
	 *            File to process.
	 * @param operation
	 *            Database operation to execute.
	 * @throws FusionException
	 */
	private void dataOperation(File file, DatabaseOperation operation) throws FusionException {
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
		LOGGER.info("Operation in BDD : " + ope + " file : " + file.getName());
		if (file.isFile()) {
			try {
				IDataSet dataSet = getDataSet(file);
				operation.execute(databaseConnect, dataSet);
			} catch (SQLException e) {
				if (e instanceof BatchUpdateException) {
					SQLException next = ((BatchUpdateException) e).getNextException();
					throw new FusionException(new RequestException(next));
				}
				if (e instanceof SQLException) {
					throw new FusionException(new RequestException(e));
				}
				throw new FusionException(e);
			} catch (DatabaseUnitException e) {
				SQLException next = ((BatchUpdateException) e.getCause()).getNextException();
				throw new FusionException(new RequestException(next));
			}
		}
	}

	/**
	 * Returns a dataset to parse the target file.
	 * 
	 * @param file
	 *            File to parse.
	 * @return {@link IDataSet}
	 * @throws FusionException
	 */
	private IDataSet getBuilder(File file) throws FusionException {
		FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
		dataSetBuilder.setColumnSensing(true);
		dataSetBuilder.setCaseSensitiveTableNames(true);
		dataSetBuilder.setDtdMetadata(false);
		try {
			FlatXmlDataSet dataSet = dataSetBuilder.build(file);
			return dataSet;
		} catch (Exception e) {
			throw new FusionException(e);
		}
	}

	/**
	 * Returns a dataset to parse the target file.
	 * 
	 * @return {@link IDataSet}
	 * @throws FusionException
	 */
	private IDataSet getDataSet(File file) throws FusionException {
		IDataSet dataSet = null;
		HashMap<String, Object> replacementObjects = new HashMap<String, Object>();
		if (replaceEmptyDatabaseValue) {
			replacementObjects.put("", null);
		}
		dataSet = new ReplacementDataSet(getBuilder(file), replacementObjects, null);
		return dataSet;
	}

	/**
	 * Insert data that are required by the server to start.
	 * 
	 * @throws Exception
	 */
	// TODO Should be remove.
	private void initBddForServer() throws FusionException {
		LOGGER.info("Inserting data needed by the application server to start");
		load(xmlFileInit);
	}

	/**
	 * Returns all table with type Table.
	 * Other type are "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL
	 * TEMPORARY", "ALIAS",
	 * "SYNONYM"
	 * 
	 * @return List of table with type Table.
	 * @throws FusionException
	 */
	public Set<TableBDD> getAllTablesTypeTable() throws FusionException {
		String[] types = { "TABLE" };
		return getTablesParTypeWithExclusions(types, null, null);
	}

	/**
	 * Returns all table with target type.
	 * Type are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL
	 * TEMPORARY", "ALIAS",
	 * "SYNONYM"
	 * 
	 * @param types
	 *            Filter return table by type.
	 * @return List of table with target type.
	 * @throws FusionException
	 */
	public Set<TableBDD> getTablesParType(String[] types) throws FusionException {
		return getTablesParTypeWithExclusions(types, null, null);
	}

	/**
	 * Returns all table with type table. Can be filter by schema or specific
	 * table.
	 * Type are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL
	 * TEMPORARY", "ALIAS",
	 * "SYNONYM"
	 * 
	 * @param schemaExclusions
	 *            Remove schema from the result.
	 * @param tablesExclusions
	 *            Remove table from the result.
	 * @return List of table with type table.
	 * @throws FusionException
	 */
	public Set<TableBDD> getTablesTypeTableWithExclusions(String[] schemaExclusions, String[] tablesExclusions)
			throws FusionException {
		String[] types = { "TABLE" };
		return getTablesParTypeWithExclusions(types, schemaExclusions, tablesExclusions);
	}

	/**
	 * Returns all table with target type. Can be filter by schema or specific
	 * table.
	 * Type are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL
	 * TEMPORARY", "ALIAS",
	 * "SYNONYM"
	 * 
	 * @param schemaExclusions
	 *            Remove schema from the result.
	 * @param tablesExclusions
	 *            Remove table from the result.
	 * @return List of table with target type.
	 * @throws FusionException
	 */
	// TODO verifier que l'implémentation n'est pas scécifique à PostgreSQL.
	// Sinon adapter la méthode ou la déplacer dans le worker de postgresql
	public Set<TableBDD> getTablesParTypeWithExclusions(String[] types, String[] schemaExclusions,
			String[] tablesExclusions) throws FusionException {
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
							tables = databaseConnect.getConnection().getMetaData().getTables(null, nomSchema, null,
									types);
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
			throw new FusionException(new RequestException(e));
		} catch (Exception e) {
			throw new FusionException(e);
		} finally {
			try {
				schemas.close();
				tables.close();
			} catch (SQLException e) {
				throw new FusionException(e);
			}
		}
		return setTables;
	}

	/**
	 * Return the path to the directory where xml are save.
	 * 
	 * @return
	 */
	public String getXmlDirectorySave() {
		return xmlDirectorySave;
	}

}
