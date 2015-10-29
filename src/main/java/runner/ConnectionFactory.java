package runner;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import exception.ConfigurationException;
import exception.FusionException;
import exception.UtilitaireException;
import selenium.driver.IDriver;
import utils.PropsUtils;

/**
 * Class that manage the selenium driver.
 * @author Nonorc
 */
public class ConnectionFactory {

	/**
	 * @see org.openqa.selenium.WebDriver.Timeouts#implicitlyWait(long, TimeUnit)
	 */
    public final static int INPLICITLY_WAIT = 1000;
    /**
     * Amount of time a page can take to load itself.
     * @see org.openqa.selenium.WebDriver.Timeouts#pageLoadTimeout(long, TimeUnit)
     */
    public final static int PAGELOAD_TIMEOUT = 30;
    /**
     * Amount of time a script can take to execute itself.
     * @see org.openqa.selenium.WebDriver.Timeouts#setScriptTimeout(long, TimeUnit)
     */
    public final static int SCRIPT_TIMEOUT = 10;
    private final static String FUSION_DRIVER = "fusion.driver";

    /**
     * Selenium Driver
     */
    private static IDriver driver;

    /**
     * 
     */
    private ConnectionFactory() {}

    /**
     * Get the driver. If the driver is not open, open it.
     * @return the selenium driver.
     * @throws Exception that occur if the driver cannot be open.
     */
    public static IDriver getDriver() throws FusionException {
        if (driver == null) {
            driver = openDriver();
        }
        return driver;
    }

    /**
     * Close the driver.
     */
    public static void closeDriver() {
        if (driver != null) {
        	driver.close();
        	driver.quit();
        	driver = null;
        }
    }

    /**
     * Take screenshot of the application when a problem occurs during a test. 
     * The screenshot is store to the path define by the capture.path property.
     * @param method
     */
    public static void takeScreenShot(FrameworkMethod method) {
        try {
            if (driver instanceof RemoteWebDriver) {
                RemoteWebDriver driverRem = (RemoteWebDriver) driver;
                File scrFile = (File) driverRem.getScreenshotAs(OutputType.FILE);
                StringBuilder filePath = new StringBuilder(
                    PropsUtils.getProperties().getProperty("capture.path"));
                filePath.append(method.getClass()).append(".");
                filePath.append(method.getName()).append(".png");

                FileUtils.copyFile(scrFile, new File(filePath.toString()));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Open the selenium driver.
     * @return The newly created driver.
     * @throws FusionException Exception that occurs if something goes wrong during the opening
     */
    @SuppressWarnings("unchecked")
    private static IDriver openDriver() throws FusionException {
        IDriver tatamiDriver = null;
        try {
            boolean remote = Boolean.valueOf((PropsUtils.getProperties().getProperty("browser.remote", "false")));
            Class<IDriver> clazz = (Class<IDriver>) Class.forName(PropsUtils.getProperties().getProperty(FUSION_DRIVER));
            Constructor<IDriver> cons;
            if (remote) {
                cons = clazz.getConstructor(URL.class, DesiredCapabilities.class);
                tatamiDriver = cons.newInstance(new URL(PropsUtils.getProperties().getProperty("selenium.grid")), DesiredCapabilities.firefox());
            } else {
                FirefoxProfile profile = new FirefoxProfile();
                boolean extensions = Boolean.valueOf(PropsUtils.getProperties().getProperty("extension.available", "false"));
                if (extensions) {
                    try {
                        profile.addExtension(new File(PropsUtils.getProperties().getProperty("extension.firebug")));
                        profile.addExtension(new File(PropsUtils.getProperties().getProperty("extension.webdeveloper")));
                    } catch (IOException e) {
                        throw new FusionException(e);
                    }
                }
                cons = clazz.getConstructor(FirefoxDriver.class);
                tatamiDriver = cons.newInstance(new FirefoxDriver(profile));
                tatamiDriver.manage().timeouts()
                    .implicitlyWait(INPLICITLY_WAIT, TimeUnit.MILLISECONDS);
                tatamiDriver.manage().timeouts()
                    .pageLoadTimeout(PAGELOAD_TIMEOUT, TimeUnit.SECONDS);
                tatamiDriver.manage().timeouts()
                    .setScriptTimeout(SCRIPT_TIMEOUT, TimeUnit.SECONDS);
            }

        } catch (ClassNotFoundException e) {
            throw new FusionException(new ConfigurationException("Problème de configuration pour " + FUSION_DRIVER, e));
        } catch (UtilitaireException e) {
            throw new FusionException(e);
        } catch (Exception e) {
            throw new FusionException(e);
        }
        return tatamiDriver;
    }
}
