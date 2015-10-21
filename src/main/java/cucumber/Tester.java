/**
 * 
 */
package cucumber;

import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Soit;
import exception.TatamiException;

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
     * @throws TatamiException
     */
    public Tester() throws TatamiException {
        super();
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#acceuil()
     */
    @Override
    @Soit("^accueil$")
    public void accueil() {
        this.driver.accueil();
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#ok()
     */
    @Override
    @Soit("^ok$")
    public void ok() {
        this.driver.ok();
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#attendre(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^attendre " + REGTEXT + ":" + REGTEXT + "$")
    public void attendre(String type, String selector) {
        this.driver.attendre(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#raffraichir(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^attendre raffraichissement " + REGTEXT + ":" + REGTEXT + "$")
    public void raffraichir(String type, String selector) {
        this.driver.raffraichir(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#html5Erreur(java.lang.String)
     */
    @Override
    @Alors("^erreur required " + REGTEXT + "$")
    public void html5Erreur(String selector) {
        this.driver.html5Erreur(selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#cliquer(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^clic " + REGTEXT + ":" + REGTEXT + "$")
    public void cliquer(String type, String selector) {
        this.driver.cliquer(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#remplir(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^remplir " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT + "$")
    public void remplir(String type, String selector, String valeur) {
        this.driver.remplir(type, selector, valeur);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#selectionner(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^selectionner " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT
        + "$")
    public void selectionner(String type, String selector, String valeur) {
        this.driver.selectionner(type, selector, valeur);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etreDesactiver(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est inactif " + REGTEXT + ":" + REGTEXT + "$")
    public void etreDesactiver(String type, String selector) {
        this.driver.etreDesactiver(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etreActiver(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est actif " + REGTEXT + ":" + REGTEXT + "$")
    public void etreActiver(String type, String selector) {
        this.driver.etreActiver(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etrePlein(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est pas vide " + REGTEXT + ":" + REGTEXT + "$")
    public void etrePlein(String type, String selector) {
        this.driver.etrePlein(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etreVide(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est vide " + REGTEXT + ":" + REGTEXT + "$")
    public void etreVide(String type, String selector) {
        this.driver.etreVide(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etreVisible(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est visible " + REGTEXT + ":" + REGTEXT + "$")
    public void etreVisible(String type, String selector) {
        this.driver.etreVisible(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#etreInvisible(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^est invisible " + REGTEXT + ":" + REGTEXT + "$")
    public void etreInvisible(String type, String selector) {
        this.driver.etreInvisible(type, selector);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#checker(java.lang.String, java.lang.String)
     */
    @Override
    @Soit("^cocher " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT + "$")
    public void checker(String type, String selector, String valeur) {
        this.driver.checker(type, selector, valeur);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#navigation(java.lang.String)
     */
    @Override
    @Soit("^etre selectionne " + REGTEXT + ":" + REGTEXT + ", valeur:" + REGTEXT
        + "$")
    public void etreSelectionne(String type, String selector, String valeur) {
        this.driver.etreSelectionne(type, selector, valeur);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#connexion(java.lang.String)
     */
    @Override
    @Soit("^connexion login:" + REGTEXT + "$")
    public void connexion(String login) throws TatamiException {
        this.driver.connexion(login);
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#deconnexion()
     */
    @Override
    @Soit("^deconnexion$")
    public void deconnexion() {
        this.driver.deconnexion();

    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#navigation(java.lang.String)
     */
    @Override
    @Soit("^navigation " + REGTEXT + "$")
    public void navigation(String target) {
        this.driver.navigation(target);

    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#attendrePage()
     */
    @Override
    @Soit("^attendre page$")
    public void attendrePage() {
        this.driver.attendrePage();

    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#messageErreur()
     */
    @Override
    @Alors("^erreur$")
    public void messageErreur() {
        this.driver.messageErreur();

    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#messageWarn()
     */
    @Override
    @Alors("^alert$")
    public void messageWarn() {
        this.driver.messageWarn();
    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#messageInfo()
     */
    @Override
    @Alors("^info$")
    public void messageInfo() {
        this.driver.messageInfo();

    }

    /*
     * (non-Javadoc)
     * @see cucumber.ITester#countRowsTable(java.lang.String, java.lang.String, int)
     */
    @Override
    @Alors("^compter ligne dans table " + REGTEXT + ":" + REGTEXT + ", attendu:" + REGTEXT
        + "$")
    public void countRowsTable(String type, String selector, int attendu) {
        this.driver.countRowsTable(type, selector, attendu);
    }
}
