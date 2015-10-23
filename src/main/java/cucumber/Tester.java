/**
 * 
 */
package cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Soit;
import exception.FusionException;

/**
 * Test générique valable pour l'ensemble des projets.
 * @author Scandinave
 */
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
        this.driver.attendre(type, selector);
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
     * @see cucumber.ITester#fill(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Given("^fill " + REGTEXT + ":" + REGTEXT + ", value:" + REGTEXT + "$")
    public void fill(String type, String selector, String valeur) {
        this.driver.remplir(type, selector, valeur);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#select(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Given("^select " + REGTEXT + ":" + REGTEXT + ", value:" + REGTEXT
        + "$")
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
    @Given("^is selected " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT
        + "$")
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
     * @see cucumber.ITester#countRowsTable(java.lang.String, java.lang.String, int)
     */
    @Override
    @Then("^count row in table " + REGTEXT + ":" + REGTEXT + ", expected:" + REGTEXT
        + "$")
    public void countRowsTable(String type, String selector, int expected) {
        this.driver.countRowsTable(type, selector, expected);
    }
}
