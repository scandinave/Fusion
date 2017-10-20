package info.scandi.fusion.database;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;

import info.scandi.fusion.core.ConfigurationManager;
import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.UtilitaireException;

@Named
@ApplicationScoped
public class DatabaseProducer {

	public static final String TYPE_ENV = "env";
	public static final String TYPE_CUSTOM = "custom";

	@Inject
	private ConfigurationManager conf;

	@Inject
	protected Logger LOGGER;

	@Produces
	@ApplicationScoped
	public IDatabaseConnection produceDatabaseConnection() throws ConfigurationException {
		LOGGER.info("Initialisation de la connexion");
		try {
			checkDatabaseParameter();
			String datasourceType = conf.getDatabase().getConnectionType();
			IDatabaseTester jdbcConnection = null;
			String databaseDriver = conf.getDatabase().getDriver();
			String databaseUrl = conf.getDatabasePrefix() + conf.getDatabase().getHost() + ":"
					+ conf.getDatabase().getPort() + "/" + conf.getDatabase().getName();
			String databaseId = conf.getDatabase().getUsername();
			String databasePassword = conf.getDatabase().getPassword();
			if (datasourceType.equals(DatabaseProducer.TYPE_CUSTOM)) { // cas
																		// jdbc
				// standard
				LOGGER.info("Connexion depuis propriété jdbc");
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseUrl);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, databaseDriver);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, databaseId);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, databasePassword);
			} else if (datasourceType.equals(DatabaseProducer.TYPE_ENV)) { // cas
				// datasource
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
	 * Permet de vérifier les paramètre de connexion à la base de donnée en
	 * fonction
	 * du type de connexion (env ou custom)
	 * 
	 * @throws ConfigurationException
	 *             Exception levée si un des paramètre de connexion est
	 *             manquant.
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
