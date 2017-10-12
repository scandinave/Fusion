package info.scandi.fusion.core.browser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.service.DriverCommandExecutor;
import org.openqa.selenium.remote.service.DriverService;

import info.scandi.fusion.conf.Extension;
import info.scandi.fusion.core.ConfigurationManager;
import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.exception.UtilitaireException;

@Named
@ApplicationScoped
public class Firefox {

	@Inject
	private ConfigurationManager conf;

	public Firefox() {
	}

	public CommandExecutor getCommandExecutor() throws FusionException, URISyntaxException, UtilitaireException {
		String browserPath = conf.getBrowser().getBinary().trim();
		String driverDirectory = "/info/scandi/fusion/driver".concat(File.separator);
		switch (conf.getOSType()) {
		case LINUX:
			System.setProperty("webdriver.gecko.driver", this.getClass().getClassLoader()
					.getResource(driverDirectory.concat("geckodriver-linux")).toURI().getPath());
			break;
		case WINDOWS:
			System.setProperty("webdriver.gecko.driver", this.getClass().getClassLoader()
					.getResource(driverDirectory.concat("geckodriver.exe")).toURI().getPath());
			break;
		case MACOS:
			System.setProperty("webdriver.gecko.driver", this.getClass().getClassLoader()
					.getResource(driverDirectory.concat("geckodriver-mac")).toURI().getPath());
			break;
		default:
			break;
		}

		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(getFireFoxProfile());
		if (!browserPath.isEmpty()) {
			FirefoxBinary binary = new FirefoxBinary(new File(browserPath));
			options.setBinary(binary);
		}
		DriverService.Builder<?, ?> builder = new GeckoDriverService.Builder().usingFirefoxBinary(options.getBinary());

		return new DriverCommandExecutor(builder.build());
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
}
