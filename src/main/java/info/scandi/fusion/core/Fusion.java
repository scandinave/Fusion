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
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.dbunit.IDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariOptions;

import info.scandi.fusion.conf.Extension;
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
	private final static String PROPERTY_DATABASE_ENV_URL = "database.envurl";
	private final static String PROPERTY_DATABASE_ENV_DRIVER = "database.envdriver";
	private final static String PROPERTY_DATABASE_ENV_USERNAME = "database.envname";
	private final static String PROPERTY_DATABASE_ENV_PASSWORD = "database.envpassword";
	public static final String DOWNLOAD_DIR = "browser.download";

	public final static int IMPLICITLY_WAIT = 1000;
	public final static int EXPLICITLE_WAIT = 10;
	public final static int PAGELOAD_TIMEOUT = 10000;
	public final static int SCRIPT_TIMEOUT = 10;

	private final static String TYPE_ENV = "env";
	private static final Object TYPE_CUSTOM = "custom";

	private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Inject
	private ConfigurationManager conf;

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
	public CommandExecutor produceExecutor() throws UtilitaireException, ConfigurationException {
		CommandExecutor driver;
		boolean remote = conf.getBrowser().isRemote();
		if (remote) {
			driver = openRemoteDriver();
		} else {
			driver = openLocalDriver();
		}
		LOGGER.info("Driver Open");
		return driver;
	}

	private CommandExecutor openLocalDriver() throws UtilitaireException, ConfigurationException {
		LOGGER.info("Opening Driver...");
		String browserPath = conf.getBrowser().getBinary().trim();
		CommandExecutor commandExecutor = null;
		switch (conf.getBrowser().getType()) {
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "C:\\Developement\\Lib\\geckodriver.exe");
			FirefoxOptions options = new FirefoxOptions();
			// options.setProfile(getFireFoxProfile());
			if (!browserPath.isEmpty()) {
				FirefoxBinary binary = new FirefoxBinary(new File(browserPath));
				options.setBinary(binary);
			}
			DriverService.Builder<?, ?> builder = new GeckoDriverService.Builder()
					.usingFirefoxBinary(options.getBinary());

			commandExecutor = new DriverCommandExecutor(builder.build());
			break;
		case "safari":
			// commandExecutor = new SafariDriver(getSafariOptions());
			break;
		case "edge":
			// commandExecutor = new EdgeDriver(getEdgeOptions());
			break;
		case "ie":
			// commandExecutor = new InternetExplorerDriver();
			break;
		case "opera":
			// commandExecutor = new OperaDriver(getOperaOptions());
			break;
		case "chrome":
			// commandExecutor = new ChromeDriver(getChromeOptions(browserPath));
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
			throw new ConfigurationException("Unsuported Browser");
		}
		return commandExecutor;
	}

	private HttpCommandExecutor openRemoteDriver() throws UtilitaireException {
		try {
			return new HttpCommandExecutor(new URL(conf.getBrowser().getGrid()));
		} catch (MalformedURLException e) {
			throw new UtilitaireException(e);
		}
	}

	@Produces
	@BrowserDesiredCapabilities
	public DesiredCapabilities produceDesiredCapabilities() throws UtilitaireException {
		DesiredCapabilities capabilities = null;
		switch (conf.getBrowser().getType()) {
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
		if (conf.getBrowser().getExtensions().isEnabled()) {
			for (Extension extension : conf.getBrowser().getExtensions().getExtension()) {
				profile.addExtension(new File(extension.getPath()));
			}
		}
		String prop = conf.getBrowser().getDownloadDir();
		if (prop != null && !prop.isEmpty()) {
			// Create directory if not exist
			StringBuilder filePath = new StringBuilder(conf.getBrowser().getDownloadDir());
			File directory = new File(filePath.toString());
			if (!directory.exists()) {
				try {
					FileUtils.forceMkdir(directory);
				} catch (IOException e) {
					throw new UtilitaireException("Can't create the directory " + filePath, e);
				}
			}
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.dir", conf.getBrowser().getDownloadDir());
			profile.setPreference("browser.download.defaultFolder", conf.getBrowser().getDownloadDir());
			profile.setPreference("browser.download.lastDir", conf.getBrowser().getDownloadDir());
			profile.setPreference("browser.download.panel.shown", false);
			profile.setPreference("browser.download.alertOnEXEOpen", false);
			profile.setPreference("app.update.enabled", false);
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
			String datasourceType = conf.getDatabase().getConnectionType();
			IDatabaseTester jdbcConnection = null;
			String databaseDriver = conf.getDatabase().getDriver();
			if (datasourceType.equals(TYPE_CUSTOM)) { // cas jdbc standard
				LOGGER.info("Connexion depuis propriété jdbc");
				String databaseUrl = conf.getDatabase().getUrl();
				String databaseName = conf.getDatabase().getUsername();
				String databasePassword = conf.getDatabase().getPassword();
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
	 * Permet de vérifier les paramètre de connexion à la base de donnée en fonction
	 * du type de connexion (env ou custom)
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
	 *             Exception levée si un des paramètre de connexion est manquant.
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
