/**
 * 
 */
package selenium.driver;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import runner.ConnectionFactory;
import selenium.BySelec;
import utils.PropsUtils;

/**
 * Implémentation minimum d'un driver. Les drivers utiliser par projet doivent implémenter cette classe afin de ne pas avoir à redéfinir des méthodes
 * génériques.
 * @author Scandinave
 */
public abstract class AbstractDriver extends RemoteWebDriver implements IDriver {

    protected final Log logger = LogFactory.getLog(this.getClass());

    public AbstractDriver(URL remoteAddress,
        DesiredCapabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    public AbstractDriver(FirefoxDriver driver) {
        super(driver.getCommandExecutor(), DesiredCapabilities.firefox());
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#index()
     */
    @Override
    public void index() throws Exception {
        this.get(PropsUtils.getProperties().getProperty("application.url"));
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#isElementPresent(org.openqa.selenium.By, long, long)
     */
    @Override
    public boolean isElementPresent(By by, long timeoutSec, long timeoutMilliSec) {
        boolean retour = false;
        try {
            new WebDriverWait(this, timeoutSec, timeoutMilliSec)
                .until(ExpectedConditions.presenceOfElementLocated(by));
            retour = true;
        } catch (Exception e) {
            System.out.println("L'élément est absent : " + e.getMessage());
        }
        return retour;
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#isElementPresent(org.openqa.selenium.By)
     */
    @Override
    public boolean isElementPresent(By by) {
        return this.isElementPresent(by,
            ConnectionFactory.INPLICITLY_WAIT / 1000,
            ConnectionFactory.INPLICITLY_WAIT);
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#waitForVisible(org.openqa.selenium.By)
     */
    @Override
    public boolean waitForVisible(By id) {
        boolean retour = false;
        try {
            new WebDriverWait(this,
                ConnectionFactory.PAGELOAD_TIMEOUT,
                ConnectionFactory.INPLICITLY_WAIT).until(
                    ExpectedConditions.visibilityOfElementLocated(id));
            retour = true;
        } catch (Exception e) {
            System.out.println(
                "L'élément est pas devenu visible : " + e.getMessage());
        }
        return retour;
    }

    /*
     * (non-Javadoc)
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
     * @see selenium.driver.IDriver#accueil()
     */
    @Override
    public void accueil() {
        this.logger.debug("Appel du test accueil()");
        try {
            this.get(PropsUtils.getProperties().getProperty("application.url"));
        } catch (Exception e) {
            Assert.fail("Impossible d'accéder à la variable application.url dans le fichier tatami.properties");
        }

    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#ok()
     */
    @Override
    public void ok() {
        this.logger.debug("Fin de test correct");
        Assert.assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#attendre(java.lang.String, java.lang.String)
     */
    @Override
    public void attendre(String type, String selector) {
        this.logger.debug("Appel du test attendreId()");
        if (!this.waitForVisible(BySelec.get(type, selector))) {
            Assert.fail("L'élément aurait du devenir visible!");
        }
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#raffraichir(java.lang.String, java.lang.String)
     */
    @Override
    public void raffraichir(String type, String selector) {
        new WebDriverWait(this, ConnectionFactory.PAGELOAD_TIMEOUT,
            ConnectionFactory.INPLICITLY_WAIT)
                .until(ExpectedConditions.refreshed(
                    ExpectedConditions.presenceOfElementLocated(
                        BySelec.get(type, selector))));

    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#html5Erreur(java.lang.String)
     */
    @Override
    public void html5Erreur(String selector) {
        this.logger.debug("Appel du test messageErreur()");
        JavascriptExecutor js = this;
        Boolean s = (Boolean) js.executeScript("return document.querySelector('"
            + selector + "').validity.valid");
        if (!s) {
            Assert.fail(selector + " " + s
                + " Un message de sevérité ERROR devrait être présent!");
        }

    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#cliquer(java.lang.String, java.lang.String)
     */
    @Override
    public void cliquer(String type, String selector) {
        this.logger.info("cliquer(String id)");
        this.findElement(BySelec.get(type, selector)).click();
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#remplir(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void remplir(String type, String selector, String valeur) {
        this.logger.info("remplir(String id, String valeur)");
        this.findElement(BySelec.get(type, selector)).clear();
        this.findElement(BySelec.get(type, selector)).sendKeys(valeur);
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#selectionner(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void selectionner(String type, String selector, String valeur) {
        this.logger.info("selectionner(String id, String valeur)");
        WebElement elem = this.findElement(BySelec.get(type, selector));
        String tagName = elem.getTagName();
        if (tagName.equals("select")) {
            Select listProfile = new Select(elem);
            listProfile.selectByVisibleText(valeur);
        } else {// TODO Améliorer la fonction pour prendre en compte les
                // elements avec une pseudo class. (Peut etre JavaScript)
            elem.click();
            List<WebElement> children = elem.findElements(
                By.xpath("//div[@class='listComboBoxElement']/li"));
            Iterator<WebElement> it = children.iterator();
            boolean flag = false;
            do {
                WebElement webElement = it.next();
                if (webElement.getText().startsWith(valeur)) {
                    webElement.click();
                    flag = true;
                }
            } while (!flag && it.hasNext());
            if (!flag) {
                Assert.fail("Impossible de trouver la valeur :" + valeur
                    + " pour le champs :" + type + ":" + selector);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etreDesactiver(java.lang.String, java.lang.String)
     */
    @Override
    public void etreDesactiver(String type, String selector) {
        this.logger.info("etreDesactiver(String id)");
        if (this.findElement(BySelec.get(type, selector)).isEnabled()) {
            Assert.fail("l'élément devrait être désactivé!");
        }
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etreActiver(java.lang.String, java.lang.String)
     */
    @Override
    public void etreActiver(String type, String selector) {
        this.logger.info("etreActiver(String id)");
        if (!this.findElement(BySelec.get(type, selector)).isEnabled()) {
            Assert.fail("l'élément devrait être activé!");
        }

    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etrePlein(java.lang.String, java.lang.String)
     */
    @Override
    public void etrePlein(String type, String selector) {
        this.logger.info("etrePlein(String id)");
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
     * @see selenium.driver.IDriver#etreVide(java.lang.String, java.lang.String)
     */
    @Override
    public void etreVide(String type, String selector) {
        this.logger.info("etrePlein(String id)");
        WebElement elem = this.findElement(BySelec.get(type, selector));
        if (elem.getAttribute("value") != null) {
            if (!elem.getAttribute("value").toString().trim().isEmpty()) {
                Assert.fail("l'élément " + type + ":" + selector
                    + " devrait être vide!");
            }
        } else {
            if (!elem.getText().trim().isEmpty()) {
                Assert.fail("l'élément " + type + ":" + selector
                    + " devrait être vide!");
            }
        }

    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etreVisible(java.lang.String, java.lang.String)
     */
    @Override
    public void etreVisible(String type, String selector) {
        this.logger.info("etreActiver(String id)");
        if (!this.waitForVisible(BySelec.get(type, selector))) {
            Assert.fail("l'élément " + type + ":" + selector
                + " devrait être visible!");
        }
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etreInvisible(java.lang.String, java.lang.String)
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
     * @see selenium.driver.IDriver#checker(java.lang.String, java.lang.String, java.lang.String)
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
        Assert.assertTrue(
            "Impossible de trouver la chekbox :" + type + ":" + selector,
            flag);
    }

    /*
     * (non-Javadoc)
     * @see selenium.driver.IDriver#etreSelectionne(java.lang.String, java.lang.String, java.lang.String)
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
}
