package info.scandi.fusion.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.UtilitaireException;
import info.scandi.fusion.utils.BrowserCapabilities;
import info.scandi.fusion.utils.BrowserDesiredCapabilities;
import info.scandi.fusion.utils.DriverExecutor;
import info.scandi.fusion.utils.PropsUtils;

/**
 * 
 * @author Scandinave
 *
 */
@ApplicationScoped
@Named
public class Fusion {
	protected final static String ROOT_PATH = "fusion.rootPath";
	private static final String CONFIGURATION_FILE = "fusion.xml";
	private final static String PROPERTY_CONNECTION_TYPE = "database.connectionType";
	private final static String PROPERTY_DATABASE_ENV_URL = "database.envurl";
	private final static String PROPERTY_DATABASE_ENV_DRIVER = "database.envdriver";
	private final static String PROPERTY_DATABASE_ENV_USERNAME = "database.envname";
	private final static String PROPERTY_DATABASE_ENV_PASSWORD = "database.envpassword";
	private final static String PROPERTY_DATABASE_URL = "database.url";
	private final static String PROPERTY_DATABASE_DRIVER = "database.driver";
	private final static String PROPERTY_DATABASE_USERNAME = "database.username";
	private final static String PROPERTY_DATABASE_PASSWORD = "database.password";
	private static final String SELENIUM_NAVIGATEUR = "selenium.navigateur";
	private static final String EXTENSION_AVAILABLE = "extension.available";
	private static final String EXTENSION_FIREBUG = "extension.firebug";
	private static final String EXTENSION_WEBDEVELOPER = "extension.webdeveloper";
	public static final String DOWNLOAD_DIR = "browser.download";

	private static final String BROWSER_REMOTE = "browser.remote";
	private static final String BROWSER = "browser.type";
	private static final String SELENIUM_GRID = "selenium.grid";

	public final static int IMPLICITLY_WAIT = 1000;
	public final static int EXPLICITLE_WAIT = 10;
	public final static int PAGELOAD_TIMEOUT = 1000;
	public final static int SCRIPT_TIMEOUT = 10;

	private final static String TYPE_ENV = "env";
	private static final Object TYPE_CUSTOM = "custom";

	private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);;

	@Produces
	public Logger produceLogger(InjectionPoint injectionPoint) {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.FINE);
		if (injectionPoint == null) {
			// TODO Ajouter une méthode lookupProducer utilisant
			// beanmanager.getinjectableReference au lieu de
			// beanmanager.getReference. Et créer un
			// injectionPoint pour cette méthode.
			return Logger.getLogger(Runner.class.getCanonicalName());
		} else {
			return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
		}
	}

	@Produces
	@DriverExecutor
	@ApplicationScoped
	public CommandExecutor produceExecutor() throws UtilitaireException {
		CommandExecutor driver;
		boolean remote = Boolean.valueOf((PropsUtils.getProperties().getProperty(BROWSER_REMOTE, "false")));
		if (remote) {
			driver = openRemoteDriver();
		} else {
			driver = openLocalDriver();
		}
		LOGGER.info("Driver Open");
		return driver;
	}

	private CommandExecutor openLocalDriver() throws UtilitaireException {
		LOGGER.info("Opening Driver...");
		String browserPath = PropsUtils.getProperties().getProperty(SELENIUM_NAVIGATEUR).trim();
		RemoteWebDriver remoteDriver;
		switch (PropsUtils.getProperties().getProperty(BROWSER)) {
		case "firefox":
			// System.setProperty("webdriver.gecko.driver",
			// "/Users/Ninja/Documents/Developpement/Logiciel/geckodriver");
			if (browserPath.isEmpty()) {
				remoteDriver = new FirefoxDriver(getFireFoxProfile());
			} else {
				FirefoxBinary binary = new FirefoxBinary(new File(browserPath));
				remoteDriver = new FirefoxDriver(binary, getFireFoxProfile());
			}
			break;
		case "safari":
			remoteDriver = new SafariDriver(getSafariOptions());
			break;
		case "edge":
			remoteDriver = new EdgeDriver(getEdgeOptions());
			break;
		case "ie":
			remoteDriver = new InternetExplorerDriver();
			break;
		case "opera":
			remoteDriver = new OperaDriver(getOperaOptions());
			break;
		case "chrome":
			remoteDriver = new ChromeDriver(getChromeOptions(browserPath));
			break;
		// case "htmlUnit":
		// remoteDriver = new HtmlUnitDriver(false);
		// break;
		// case "htmlUnitWithJs":
		// remoteDriver = new HtmlUnitDriver(true);
		// break;
		// case "phantomjs":
		// remoteDriver = new Phan
		// break;
		default:
			if (browserPath.isEmpty()) {
				remoteDriver = new FirefoxDriver(getFireFoxProfile());
			} else {
				FirefoxBinary binary = new FirefoxBinary(new File(browserPath));
				remoteDriver = new FirefoxDriver(binary, getFireFoxProfile());
			}
			break;
		}
		return remoteDriver.getCommandExecutor();
	}

	private HttpCommandExecutor openRemoteDriver() throws UtilitaireException {
		try {
			return new HttpCommandExecutor(new URL(PropsUtils.getProperties().getProperty(SELENIUM_GRID)));
		} catch (MalformedURLException e) {
			throw new UtilitaireException(e);
		}
	}

	@Produces
	@BrowserDesiredCapabilities
	public DesiredCapabilities produceDesiredCapabilities() throws UtilitaireException {
		DesiredCapabilities capabilities = null;
		switch (PropsUtils.getProperties().getProperty(BROWSER)) {
		case "firefox":
			capabilities = DesiredCapabilities.firefox();
			break;
		case "safari":
			capabilities = DesiredCapabilities.safari();
			break;
		case "edge":
			capabilities = DesiredCapabilities.edge();
			break;
		case "ie":
			capabilities = DesiredCapabilities.internetExplorer();
			break;
		case "opera":
			capabilities = DesiredCapabilities.operaBlink();
			break;
		case "chrome":
			capabilities = DesiredCapabilities.chrome();
			break;
		// case "htmlUnit":
		// capabilities = DesiredCapabilities.htmlUnit();
		// break;
		// case "htmlUnitWithJs":
		// capabilities = DesiredCapabilities.htmlUnitWithJs();
		// break;
		// case "phantomjs":
		// capabilities = DesiredCapabilities.phantomjs();
		// break;
		default:
			capabilities = DesiredCapabilities.firefox();
			break;
		}
		return capabilities;
	}

	@Produces
	@BrowserCapabilities
	public Capabilities produceCapabilities() {
		return null;
	}

	private FirefoxProfile getFireFoxProfile() throws UtilitaireException {
		FirefoxProfile profile = new FirefoxProfile();
		boolean extensions = Boolean.valueOf(PropsUtils.getProperties().getProperty(EXTENSION_AVAILABLE, "false"));
		if (extensions) {
			try {
				profile.addExtension(new File(PropsUtils.getProperties().getProperty(EXTENSION_FIREBUG)));
				profile.addExtension(new File(PropsUtils.getProperties().getProperty(EXTENSION_WEBDEVELOPER)));
			} catch (IOException e) {
				throw new UtilitaireException(e);
			}
		}
		String prop = PropsUtils.getProperties().getProperty(DOWNLOAD_DIR);
		if (prop != null && !prop.isEmpty()) {
			// crée le répertoire s'il n'existe pas
			StringBuilder filePath = new StringBuilder(PropsUtils.getProperties().getProperty(DOWNLOAD_DIR));
			File directory = new File(filePath.toString());
			if (!directory.exists()) {
				try {
					FileUtils.forceMkdir(directory);
				} catch (IOException e) {
					throw new UtilitaireException("Impossible de créer le répertoire " + filePath, e);
				}
			}
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.dir", PropsUtils.getProperties().getProperty(DOWNLOAD_DIR));
			profile.setPreference("browser.download.defaultFolder",
					PropsUtils.getProperties().getProperty(DOWNLOAD_DIR));
			profile.setPreference("browser.download.lastDir", PropsUtils.getProperties().getProperty(DOWNLOAD_DIR));
			profile.setPreference("browser.download.panel.shown", false);
			profile.setPreference("browser.download.alertOnEXEOpen", false);
			// profile.setPreference("network.proxy.autoconfig_url", "");
			// profile.setPreference("browser.download.show_plugins_in_list",
			// false);
			profile.setPreference("browser.helperApps.neverAsk.openFile",
					"application/octet-stream;application/csv;text/csv;application/pdf;image/png;image/jpeg;text/plain;application/x-msexcel;application/excel;application/x-excel;application/excel;application/x-excel;application/excel;application/vnd.ms-excel;application/x-excel;application/x-msexcel");
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"application/octet-stream;application/csv;text/csv;application/pdf;image/png;image/jpeg;text/plain;application/x-msexcel;application/excel;application/x-excel;application/excel;application/x-excel;application/excel;application/vnd.ms-excel;application/x-excel;application/x-msexcel");
			profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		}
		// profile.setPreference("extensions.xpiState", "");
		return profile;
	}

	/**
	 * Defined option for the Safari Browser
	 */
	private SafariOptions getSafariOptions() {
		SafariOptions options = new SafariOptions();
		options.setUseCleanSession(true);
		return options;
	}

	private EdgeOptions getEdgeOptions() {
		EdgeOptions options = new EdgeOptions();
		options.setPageLoadStrategy("normal");
		return options;
	}

	private OperaOptions getOperaOptions() {
		OperaOptions options = new OperaOptions();
		return options;
	}

	private ChromeOptions getChromeOptions(String browserPath) {
		ChromeOptions options = new ChromeOptions();
		if (!browserPath.isEmpty()) {
			options.setBinary(browserPath);
		}
		return options;
	}

	@Produces
	@ApplicationScoped
	public IDatabaseConnection produceDatabaseConnection() throws ConfigurationException {
		LOGGER.info("Initialisation de la connexion");
		try {
			String datasourceType = PropsUtils.getProperties().getProperty(PROPERTY_CONNECTION_TYPE);
			IDatabaseTester jdbcConnection = null;
			String databaseDriver = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_DRIVER);
			if (datasourceType.equals(TYPE_CUSTOM)) { // cas jdbc standard
				LOGGER.info("Connexion depuis propriété jdbc");
				String databaseUrl = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_URL);
				String databaseName = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_USERNAME);
				String databasePassword = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_PASSWORD);
				checkDatabaseParameter(datasourceType, databaseUrl, databaseDriver, databaseName, databasePassword);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseUrl);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, databaseDriver);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, databaseName);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, databasePassword);
			} else if (datasourceType.equals(TYPE_ENV)) { // cas datasource
				LOGGER.info("Connexion depuis variable d'environnement");
				String databaseUrl = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_ENV_URL);
				String databaseName = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_ENV_USERNAME);
				String databasePassword = PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_ENV_PASSWORD);
				checkDatabaseParameter(datasourceType, databaseUrl, databaseDriver, databaseName, databasePassword);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, System.getenv(databaseUrl));
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, databaseDriver);
				System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, System.getenv(databaseName));
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
	 * fonction du type de connexion (env ou custom)
	 * 
	 * @param datasourceType
	 *            Le type de connexion
	 * @param databaseUrl
	 *            L'url de la base de donnée
	 * @param databaseDriver
	 *            Le driver de connexion à la base de donnée
	 * @param databaseName
	 *            L'identifiant de connexion à la base de donnée
	 * @param databasePassword
	 *            Le mot de passe de connexion à la base de donnée
	 * @throws ConfigurationException
	 *             Exception levée si un des paramètre de connexion est
	 *             manquant.
	 */
	private void checkDatabaseParameter(String datasourceType, String databaseUrl, String databaseDriver,
			String databaseName, String databasePassword) throws ConfigurationException {
		if (datasourceType.equals(TYPE_ENV)) {
			if (databaseUrl == null) {
				throw new ConfigurationException("La variable d'environnement database.env.url est manquante");
			}
			if (databaseDriver == null) {
				throw new ConfigurationException("La variable d'environnement database.env.driver est manquante");
			}
			if (databaseName == null) {
				throw new ConfigurationException("La variable d'environnement database.env.name est manquante");
			}
			if (databasePassword == null) {
				throw new ConfigurationException("La variable d'environnement database.env.password est manquante");
			}
		} else {
			if (databaseUrl == null) {
				throw new ConfigurationException("La propriété database.url est manquante");
			}
			if (databaseDriver == null) {
				throw new ConfigurationException("La propriété database.driver est manquante");
			}
			if (databaseName == null) {
				throw new ConfigurationException("La propriété database.name est manquante");
			}
			if (databasePassword == null) {
				throw new ConfigurationException("La propriété database.password est manquante");
			}
		}
	}

}
