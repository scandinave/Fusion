/**
 * 
 */
package info.scandi.fusion.selenium.driver;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import info.scandi.fusion.core.ConfigurationManager;
import info.scandi.fusion.core.Fusion;
import info.scandi.fusion.selenium.BySelec;
import info.scandi.fusion.utils.BrowserDesiredCapabilities;
import info.scandi.fusion.utils.DriverExecutor;

/**
 * Implémentation minimum d'un driver. Les drivers utiliser par projet doivent
 * implémenter cette classe afin de ne pas avoir à redéfinir des méthodes
 * génériques.
 * 
 * @author Scandinave
 */
public abstract class AbstractDriver extends RemoteWebDriver implements IDriver {

	@Inject
	protected Logger logger;

	@Inject
	ConfigurationManager conf;

	protected Wait<WebDriver> wait;

	@Inject
	public AbstractDriver(@DriverExecutor CommandExecutor remoteAddress,
			@BrowserDesiredCapabilities DesiredCapabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);

	}

	@PostConstruct
	public void init() {
		wait = new FluentWait<WebDriver>(this).withTimeout(30, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		// TODO make available at properties
		manage().window().maximize();
		manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS)
				.pageLoadTimeout(Fusion.PAGELOAD_TIMEOUT, TimeUnit.MILLISECONDS)
				.setScriptTimeout(Fusion.SCRIPT_TIMEOUT, TimeUnit.MILLISECONDS);
		manage().deleteAllCookies();
		navigate().refresh();

	}

	// @PreDestroy
	// public void finish() {
	// this.quit();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#index()
	 */
	@Override
	public void index() throws Exception {
		this.home();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#isElementPresent(org.openqa.selenium.By, long,
	 * long)
	 */
	@Override
	public boolean isElementPresent(By by, long timeoutSec, long timeoutMilliSec) {
		try {
			return wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(by);
				}
			}).isEnabled();
		} catch (Exception e) {
			System.out.println("The element is missing : " + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#isElementPresent(org.openqa.selenium.By)
	 */
	@Override
	public boolean isElementPresent(By by) {
		return this.isElementPresent(by, Fusion.EXPLICITLE_WAIT, Fusion.IMPLICITLY_WAIT);
	}

	@Override
	public boolean isElementImmediatPresent(By by) {
		return this.isElementPresent(by, Fusion.EXPLICITLE_WAIT, Fusion.IMPLICITLY_WAIT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#waitForVisible(org.openqa.selenium.By)
	 */
	@Override
	public boolean waitForVisible(By id) {
		try {
			return wait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(id);
				}
			}).isDisplayed();
		} catch (Exception e) {
			System.out.println("The element has not become visible : " + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#confirmation()
	 */
	@Override
	public String confirmation() {
		Alert alert = this.switchTo().alert();
		String alertText = alert.getText();
		alert.accept();
		return alertText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#accueil()
	 */
	@Override
	public void home() {
		if (!conf.getCommon().getAppUrl().isEmpty()) {
			this.navigate().to(conf.getCommon().getAppUrl());
		} else {
			Assert.fail(
					"Unable to access the application.url variable in the file fusion.xml or unable to communicate with the browser");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#ok()
	 */
	@Override
	public void ok() {
		Assert.assertTrue(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#attendre(java.lang.String, java.lang.String)
	 */
	@Override
	public void wait(String type, String selector) {
		if (!this.isElementPresent(BySelec.get(type, selector))) {
			Assert.fail("The element type :" + type + " selector:" + selector + " is not present");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#raffraichir(java.lang.String, java.lang.String)
	 */
	@Override
	public void raffraichir(String type, String selector) {
		// new WebDriverWait(this, Fusion.EXPLICITLE_WAIT,
		// Fusion.IMPLICITLY_WAIT).until(
		// ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(BySelec.get(type,
		// selector))));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#html5Erreur(java.lang.String)
	 */
	@Override
	public void html5Erreur(String selector) {
		JavascriptExecutor js = this;
		Boolean s = (Boolean) js.executeScript("return document.querySelector('" + selector + "').validity.valid");
		if (!s) {
			Assert.fail(selector + " " + s + " A html5 message should be displayed!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#cliquer(java.lang.String, java.lang.String)
	 */
	@Override
	public void cliquer(String type, String selector) {
		wait(type, selector);
		this.findElement(BySelec.get(type, selector)).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#remplir(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void remplir(String type, String selector, String valeur) {
		wait(type, selector);
		this.findElement(BySelec.get(type, selector)).clear();
		this.findElement(BySelec.get(type, selector)).sendKeys(valeur);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#selectionner(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void selectionner(String type, String selector, String valeur) {
		WebElement elem = this.findElement(BySelec.get(type, selector));
		Select listProfile = new Select(elem);
		listProfile.selectByVisibleText(valeur);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreDesactiver(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void etreDesactiver(String type, String selector) {
		if (this.findElement(BySelec.get(type, selector)).isEnabled()) {
			Assert.fail("The item should be disabled!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreActiver(java.lang.String, java.lang.String)
	 */
	@Override
	public void etreActiver(String type, String selector) {
		wait(type, selector);
		if (!this.findElement(BySelec.get(type, selector)).isEnabled()) {
			Assert.fail("The item should be eabled!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etrePlein(java.lang.String, java.lang.String)
	 */
	@Override
	public void etrePlein(String type, String selector) {
		WebElement elem = this.findElement(BySelec.get(type, selector));
		if (elem.getAttribute("value") != null) {
			if (elem.getAttribute("value").toString().trim().isEmpty()) {
				Assert.fail("l'élément ne devrait pas être vide!");
			}
		} else {
			if (elem.getText().trim().isEmpty()) {
				Assert.fail("l'élément ne devrait pas être vide!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreVide(java.lang.String, java.lang.String)
	 */
	@Override
	public void etreVide(String type, String selector) {
		this.logger.info("etrePlein(String id)");
		WebElement elem = this.findElement(BySelec.get(type, selector));
		if (elem.getAttribute("value") != null) {
			if (!elem.getAttribute("value").toString().trim().isEmpty()) {
				Assert.fail("l'élément " + type + ":" + selector + " devrait être vide!");
			}
		} else {
			if (!elem.getText().trim().isEmpty()) {
				Assert.fail("l'élément " + type + ":" + selector + " devrait être vide!");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreVisible(java.lang.String, java.lang.String)
	 */
	@Override
	public void etreVisible(String type, String selector) {
		this.logger.info("etreActiver(String id)");
		if (!this.waitForVisible(BySelec.get(type, selector))) {
			Assert.fail("l'élément " + type + ":" + selector + " devrait être visible!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreInvisible(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void etreInvisible(String type, String selector) {
		this.logger.info("etreActiver(String id)");
		if (this.isElementPresent(BySelec.get(type, selector))) {
			Assert.fail("l'élément devrait être invisible!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#checker(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void checker(String type, String selector, String valeur) {
		this.logger.info("checker(String id, String valeur)");
		List<WebElement> cbs = this.findElements(BySelec.get(type, selector));
		Iterator<WebElement> it = cbs.iterator();
		boolean flag = false;
		do {
			WebElement cb = it.next();
			if (cb.getAttribute("value").toString().equals(valeur)) {
				cb.click();
				flag = true;
			}
		} while (!flag && it.hasNext());
		Assert.assertTrue("Impossible de trouver la chekbox :" + type + ":" + selector, flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see selenium.driver.IDriver#etreSelectionne(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void etreSelectionne(String type, String selector, String valeur) {
		WebElement elem = this.findElement(BySelec.get(type, selector));
		String tagName = elem.getTagName();
		if (tagName.equals("select")) {
			Select listProfile = new Select(elem);
			if (!listProfile.getFirstSelectedOption().getText().equals(valeur)) {
				Assert.fail("la valeur de l'élément ne correspond pas à la valeur " + valeur + "!");
			}
		} else {// TODO Améliorer la fonction pour prendre en compte les
				// elements avec une pseudo class. (Peut etre JavaScript)
			WebElement item = elem.findElement(By.cssSelector("li > div > div:first-child()"));
			if (!item.getText().equals(valeur)) {
				Assert.fail("la valeur de l'élément ne correspond pas à la valeur " + valeur + "!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.scandi.fusion.selenium.driver.IDriver#hasClass(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public void hasClass(String type, String selector, String value) {
		wait(type, selector);
		WebElement elem = this.findElement(BySelec.get(type, selector));
		Assert.assertTrue("L'élément " + type + ":" + selector + " ne contient pas la class " + value,
				elem.getAttribute("class").matches(value));
	}

	@Override
	public void acceptPopupWindow() {
		this.switchTo().alert().accept();
	}

	@Override
	public void cancelPopupWindow() {
		this.switchTo().alert().dismiss();
	}

	@Override
	public void hasvalue(String type, String selector, int expected) {
		wait(type, selector);
		WebElement elem = this.findElement(BySelec.get(type, selector));
		Assert.assertTrue("Le champs input " + type + ":" + selector + " ne contient pas la valeur " + expected,
				elem.getAttribute("value").equals(expected));
	}
}
