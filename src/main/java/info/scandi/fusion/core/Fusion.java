package info.scandi.fusion.core;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.safari.SafariOptions;

import info.scandi.fusion.core.browser.Firefox;
import info.scandi.fusion.exception.ConfigurationException;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.UtilitaireException;
import info.scandi.fusion.utils.BrowserCapabilities;
import info.scandi.fusion.utils.BrowserDesiredCapabilities;
import info.scandi.fusion.utils.DriverExecutor;

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

	public final static String TYPE_ENV = "env";
	public static final Object TYPE_CUSTOM = "custom";

	private Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Inject
	private ConfigurationManager conf;

	@Inject
	private Firefox firefoxDriver;

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
	public CommandExecutor produceExecutor()
			throws UtilitaireException, ConfigurationException, FusionException, URISyntaxException {
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

	private CommandExecutor openLocalDriver()
			throws UtilitaireException, ConfigurationException, FusionException, URISyntaxException {
		LOGGER.info("Opening Driver...");

		CommandExecutor commandExecutor = null;
		switch (conf.getBrowser().getType()) {
		case "firefox":
			commandExecutor = firefoxDriver.getCommandExecutor();
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
}
