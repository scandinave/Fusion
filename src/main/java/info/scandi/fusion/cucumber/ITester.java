/**
 * 
 */
package info.scandi.fusion.cucumber;

import info.scandi.fusion.exception.FusionException;
import info.scandi.fusion.selenium.BySelec;
import info.scandi.fusion.selenium.driver.AbstractDriver;

/**
 * A tester is a class that defines Cucumber steps definitions Fusion provide
 * its own common steps definitions with this interface. Some step definition
 * are not implemented in the driver and must be define by the test project.
 * 
 * @author Scandinave
 */
public interface ITester {

	/**
	 * Log in the user to the application. This method must be implemented by
	 * the application driver.
	 * 
	 * @param user
	 *            User login.
	 * @see AbstractDriver
	 * @throws FusionException
	 */
	void connection(String login) throws FusionException;

	/**
	 * Log of the user to the application. This method must be implemented by
	 * the application driver.
	 */
	void disconnection();

	/**
	 * Loads a page by clicking on a link. This method must be implemented by
	 * the application driver.
	 * 
	 * @param target
	 *            Target text link.
	 */
	void navigation(String target);

	/**
	 * Wait for page to load. This method is a shortcut of the refresh method
	 * with a predefined element. This method must be implemented by the
	 * application driver.
	 */
	void waitForPage();

	/**
	 * Tests if the application returns an error message. This method must be
	 * implemented by the application driver.
	 */
	void errorMessage();

	/**
	 * Tests if the application returns an warning message. This method must be
	 * implemented by the application driver.
	 */
	void warningMessage();

	/**
	 * Tests if the application returns an info message. This method must be
	 * implemented by the application driver.
	 */
	void infoMessage();

	/**
	 * Counts the number of row in the table. This method must be implemented by
	 * the application driver.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param expected
	 *            The number of row expected.
	 */
	void countRowsTable(String type, String selector, int expected);

	/**
	 * Goes to the application home page.
	 */
	void home();

	/**
	 * Convenience method to tell that this test is good at this moment if no
	 * other result is awaited.
	 */
	void ok();

	/**
	 * Wait for a element to be present before resume.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void wait(String type, String selector);

	/**
	 * Refreshes the current page and wait for an element to be load.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void refresh(String type, String selector);

	/**
	 * Tests if the application returns an html error message.
	 * 
	 * @param selector
	 *            Javascript selector of the input
	 */
	void html5Erreur(String selector);

	/**
	 * Clicks on a element.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void click(String type, String selector);

	/**
	 * Fills a input with a value.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param valeur
	 *            The value to fill. Can be empty.
	 */
	void fill(String type, String selector, String valeur);

	/**
	 * Selects element into a select box.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param valeur
	 *            The value to select.
	 */
	void select(String type, String selector, String valeur);

	/**
	 * Tests if the selected element is disabled.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isDisabled(String type, String selector);

	/**
	 * Tests if the selected element is enabled.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isEnabled(String type, String selector);

	/**
	 * Tests if the selected input has a value.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isFull(String type, String selector);

	/**
	 * Tests if the selected input is empty.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isEmpty(String type, String selector);

	/**
	 * Tests if the selected element is visible.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isVisible(String type, String selector);

	/**
	 * Tests if the selected element is hidden.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 */
	void isHidden(String type, String selector);

	/**
	 * Toggles the state of a checkbox.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param valeur
	 *            The value of the checkbox to toggle.
	 */
	void check(String type, String selector, String valeur);

	/**
	 * Tests if a value is selected in the target select element.
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param valeur
	 *            The value that will be tested.
	 */
	void isSelected(String type, String selector, String valeur);

	/**
	 * Validate a JavaScript popup window
	 */
	void acceptPopupWindow();

	/**
	 * Cancel a JavaScript popup window
	 */
	void cancelPopupWindow();

	/**
	 * Test if a input field has the value passed in parameter
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param expected
	 *            The value that will be tested.
	 */
	void hasValue(String type, String selector, int expected);

	/**
	 * Test if a input field has the class passed in parameter
	 * 
	 * @param type
	 *            Selector type {@link BySelec}
	 * @param selector
	 *            Selector value
	 * @param expected
	 *            The value that will be tested.
	 */
	void hasClass(String type, String selector, String value);

}
