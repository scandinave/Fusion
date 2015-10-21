package selenium;

import org.openqa.selenium.By;

/**
 * Retourne un selecteur selenium à partir d'un type et d'une valeur sous forme de String.
 * @author Scandinave
 */
public class BySelec {

    /**
     * Retourne un selecteur selenium à partir d'un type et d'une valeur sous forme de String. Les différents sélecteur disponible sont.
     * <ul>
     * <li>id</li>
     * <li>name</li>
     * <li>className</li>
     * <li>xpath</li>
     * <li>css</li>
     * <li>linkText</li>
     * <li>tagName</li>
     * <li>partialLinkText</li>
     * </ul>
     * Retourne null si aucun sélecteur correspondant au type passé en paramètre n'est trouvé.
     * @param type
     * @param selector
     * @return By
     */
    public static By get(String type, String selector) {
        By by = null;
        if ("id".equalsIgnoreCase(type)) {
            by = By.id(selector);
        } else if ("name".equalsIgnoreCase(type)) {
            by = By.name(selector);
        } else if ("className".equalsIgnoreCase(type)) {
            by = By.className(selector);
        } else if ("xpath".equalsIgnoreCase(type)) {
            by = By.xpath(selector);
        } else if ("css".equalsIgnoreCase(type)) {
            by = By.cssSelector(selector);
        } else if ("linkText".equalsIgnoreCase(type)) {
            by = By.linkText(selector);
        } else if ("tagName".equalsIgnoreCase(type)) {
            by = By.tagName(selector);
        } else if ("partialLinkText".equalsIgnoreCase(type)) {
            by = By.partialLinkText(selector);
        }
        return by;
    }

}
