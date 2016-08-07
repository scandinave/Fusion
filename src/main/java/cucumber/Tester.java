/**
 * 
 */
package cucumber;

import javax.enterprise.util.AnnotationLiteral;

import core.Runner;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import exception.FusionException;
import selenium.driver.IDriver;

/**
 * Test générique valable pour l'ensemble des projets.
 * 
 * @author Scandinave
 */
@utils.Tester
public class Tester extends AbstractTester implements ITester {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = -6581608978102622951L;

	/**
	 * @throws FusionException
	 */
	public Tester() throws FusionException {
		super();
		this.driver = Runner.lookup(IDriver.class, new AnnotationLiteral<utils.Driver>() {
		});
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#home()
	 */
	@Override
	@Given("^home$")
	public void home() {
		this.driver.accueil();
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#ok()
	 */
	@Override
	@Then("^ok$")
	public void ok() {
		this.driver.ok();
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#wait(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^wait " + REGTEXT + ":" + REGTEXT + "$")
	public void wait(String type, String selector) {
		this.driver.wait(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#refresh(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^wait refresh " + REGTEXT + ":" + REGTEXT + "$")
	public void refresh(String type, String selector) {
		this.driver.raffraichir(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#html5Erreur(java.lang.String)
	 */
	@Override
	@Then("^html5Erreur " + REGTEXT + "$")
	public void html5Erreur(String selector) {
		this.driver.html5Erreur(selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#click(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^click " + REGTEXT + ":" + REGTEXT + "$")
	public void click(String type, String selector) {
		this.driver.cliquer(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#fill(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Given("^fill " + REGTEXT + ":" + REGTEXT + ", value:" + REGTEXT + "$")
	public void fill(String type, String selector, String valeur) {
		this.driver.remplir(type, selector, valeur);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#select(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Given("^select " + REGTEXT + ":" + REGTEXT + ", value:" + REGTEXT + "$")
	public void select(String type, String selector, String valeur) {
		this.driver.selectionner(type, selector, valeur);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isDisabled(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is disabled " + REGTEXT + ":" + REGTEXT + "$")
	public void isDisabled(String type, String selector) {
		this.driver.etreDesactiver(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isEnabled(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is enabled " + REGTEXT + ":" + REGTEXT + "$")
	public void isEnabled(String type, String selector) {
		this.driver.etreActiver(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isFull(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is full " + REGTEXT + ":" + REGTEXT + "$")
	public void isFull(String type, String selector) {
		this.driver.etrePlein(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isEmpty(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is empty " + REGTEXT + ":" + REGTEXT + "$")
	public void isEmpty(String type, String selector) {
		this.driver.etreVide(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isVisible(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is visible " + REGTEXT + ":" + REGTEXT + "$")
	public void isVisible(String type, String selector) {
		this.driver.etreVisible(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isHidden(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^is hidden " + REGTEXT + ":" + REGTEXT + "$")
	public void isHidden(String type, String selector) {
		this.driver.etreInvisible(type, selector);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#check(java.lang.String, java.lang.String)
	 */
	@Override
	@Given("^check " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT + "$")
	public void check(String type, String selector, String valeur) {
		this.driver.checker(type, selector, valeur);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#isSelected(java.lang.String)
	 */
	@Override
	@Given("^is selected " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT + "$")
	public void isSelected(String type, String selector, String valeur) {
		this.driver.etreSelectionne(type, selector, valeur);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#connection(java.lang.String)
	 */
	@Override
	@Given("^connection login:" + REGTEXT + "$")
	public void connection(String login) throws FusionException {
		this.driver.connexion(login);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#disconnection()
	 */
	@Override
	@Given("^disconnection$")
	public void disconnection() {
		this.driver.deconnexion();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#navigation(java.lang.String)
	 */
	@Override
	@Given("^navigation " + REGTEXT + "$")
	public void navigation(String target) {
		this.driver.navigation(target);

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#waitForPage()
	 */
	@Override
	@Given("^wait for page$")
	public void waitForPage() {
		this.driver.attendrePage();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#errorMessage()
	 */
	@Override
	@Then("^error$")
	public void errorMessage() {
		this.driver.messageErreur();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#warningMessage()
	 */
	@Override
	@Then("^warning$")
	public void warningMessage() {
		this.driver.messageWarn();
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#infoMessage()
	 */
	@Override
	@Then("^info$")
	public void infoMessage() {
		this.driver.messageInfo();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#countRowsTable(java.lang.String, java.lang.String,
	 * int)
	 */
	@Override
	@Then("^count row in table " + REGTEXT + ":" + REGTEXT + ", expected:" + REGTEXT + "$")
	public void countRowsTable(String type, String selector, int expected) {
		this.driver.countRowsTable(type, selector, expected);
	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#acceptPopupWindow()
	 */
	@Override
	@Then("accept popup window$")
	public void acceptPopupWindow() {
		this.driver.acceptPopupWindow();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#cancelPopupWindow()
	 */
	@Override
	@Then("^cancel popup window$")
	public void cancelPopupWindow() {
		this.driver.cancelPopupWindow();

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#hasvalue(java.lang.String, java.lang.String, int)
	 */
	@Override
	@Then("^has value " + REGTEXT + ":" + REGTEXT + ", expected:" + REGTEXT + "$")
	public void hasValue(String type, String selector, int expected) {
		this.driver.hasvalue(type, selector, expected);

	}

	/*
	 * (non-Javadoc)
	 * @see cucumber.ITester#hasClass(java.lang.String, java.lang.String, int)
	 */
	@Override
	@Then("^has class " + REGTEXT + ":" + REGTEXT + ", expected:" + REGTEXT + "$")
	public void hasClass(String type, String selector, String expected) {
		this.driver.hasClass(type, selector, expected);

	}
}
