/**
 * 
 */
package selenium.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;

import exception.TatamiException;

/**
 * Propose des méthodes utilitairs de manipulation du DOM utilisé par les test cucumber. Les Drivers utilisé par les projets de Test doivent implémenter cette
 * classe, soit directement soit en entendant la classe abstraite AbstractDriver.
 * @author Scandinave
 * @see selenium.driver.AbstractDriver
 */
public interface IDriver extends WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText, FindsByName, FindsByCssSelector, FindsByTagName,
    FindsByXPath, HasInputDevices, HasCapabilities, TakesScreenshot {

    /**
     * Accès à la page d'accueil de l'application
     * @throws Exception
     */
    void index() throws Exception;

    /**
     * Détermine si un élément est présent sur la page.
     * @param driver driver de référence.
     * @param by L'élément à rechercher.
     * @param timeout Définit pendant combien de temps (en seconde) la fonction doit rechercher l'élément.
     * @return true si l'élément est présent, false sinon.
     */
    boolean isElementPresent(By by, long timeoutSec, long timeoutMilliSec);

    /**
     * Détermine si un élément est présent sur la page pendant le timeout par défaut.
     * @param driver driver de référence.
     * @param by L'élément à rechercher.
     * @return true si l'élément est présent, false sinon.
     */
    boolean isElementPresent(By by);

    /**
     * Attend que un élément devienne visible.
     * @param by L'élément à rechercher.
     * @return true si l'élément est devenu visible avant le timeout.
     */
    boolean waitForVisible(By id);

    /**
     * Permet de récupérer le text d'un confirm accepté.
     * @return {@link String}
     */
    String confirmation();

    /**
     * Permet de retourner à l'accueil de l'application.
     */
    void accueil();

    /**
     * Méthode utilitaire permettant de valider des tests qui se sont correctement déroulé jusqu'a présent. Retourne sur True.
     */
    void ok();

    /**
     * Attend qu'un élément soit présent sur la page avant de continuer.
     * @param type Le type du selecteur
     * @param selector
     */
    void attendre(String type, String selector);

    /**
     * Raffraichis la page et attend que l'élément passer en paramètre soit chargé.
     * @param type
     * @param selector
     */
    void raffraichir(String type, String selector);

    /**
     * Permet de tester l'affichage d'un message d'erreur sur un champs imput.
     * @param selector
     */
    void html5Erreur(String selector);

    /**
     * Permet de cliquer sur un élément via son id.
     * @param type
     * @param selector
     */
    void cliquer(String type, String selector);

    /**
     * Permet de remplir un champ via un type(id, name, css, xpath, tagName, linkText, partialLinkText). La valeur peut être vide.
     * @param type
     * @param selector
     * @param valeur
     */
    void remplir(String type, String selector, String valeur);

    /**
     * Permet de selectionner un champ d'une liste deroulante via son id. La valeur peut être vide.
     * @param type
     * @param selector
     * @param valeur
     */
    void selectionner(String type, String selector, String valeur);

    /**
     * Permet de savoir si un élément est désactiver (grisé).
     * @param type
     * @param selector
     */
    void etreDesactiver(String type, String selector);

    /**
     * Permet de savoir si un élément est actif.
     * @param type
     * @param selector
     */
    void etreActiver(String type, String selector);

    /**
     * Permet de savoir si un champ texte est renseigner.
     * @param type
     * @param selector
     */
    void etrePlein(String type, String selector);

    /**
     * Permet de savoir si un champ texte n'est pas renseigner.
     * @param type
     * @param selector
     */
    void etreVide(String type, String selector);

    /**
     * Permet de savoir si un élément est visible.
     * @param type
     * @param selector
     */
    void etreVisible(String type, String selector);

    /**
     * Permet de savoir si un élément n'est pas visible.
     * @param type
     * @param selector
     */
    void etreInvisible(String type, String selector);

    /**
     * Permet de checker une checkbox.
     * @param type
     * @param selector
     * @param valeur
     */
    void checker(String type, String selector, String valeur);

    /**
     * Permet de savoir si une valeur d'un select est sélectionné.
     * @param type
     * @param selector
     * @param valeur
     */
    void etreSelectionne(String type, String selector, String valeur);

    /**
     * Permet la connexion à l'application de l'utilisateur passé en paramère.
     * @param user
     * @throws TatamiException
     */
    void connexion(String login) throws TatamiException;

    /**
     * Permet de se déconnecter de l'application.
     */
    void deconnexion();

    /**
     * Charge une page en cliquant sur le lien de navigation passé en paramètre.
     * @param target
     */
    void navigation(String target);

    /**
     * Attend que le layout de la page soit chargé. Cette méthode est un raccouris de la méthode raffraichir mais avec un élément prédéfinis.
     * @see cucumber.ITester#raffraichir(String, String)
     */
    void attendrePage();

    /**
     * Test la présence d'un message d'erreur sur l'application.
     */
    void messageErreur();

    /**
     * Test la présence d'un message d'alerte sur l'application.
     */
    void messageWarn();

    /**
     * Test la présence d'un message d'info sur l'application.
     */
    void messageInfo();

    /**
     * Compte le nombre de ligne dans une table.
     * @param type
     * @param selector
     * @param attendu
     */
    void countRowsTable(String type, String selector, int attendu);
}
