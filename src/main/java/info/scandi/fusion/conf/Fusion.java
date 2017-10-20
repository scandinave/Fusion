//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.10.20 à 07:25:18 PM CEST 
//


package info.scandi.fusion.conf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://fusion.scandi.info}database"/>
 *         &lt;element ref="{http://fusion.scandi.info}browser"/>
 *         &lt;element ref="{http://fusion.scandi.info}common"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "fusion")
public class Fusion {

    @XmlElement(required = true)
    protected Database database;
    @XmlElement(required = true)
    protected Browser browser;
    @XmlElement(required = true)
    protected Common common;

    /**
     * Obtient la valeur de la propriété database.
     * 
     * @return
     *     possible object is
     *     {@link Database }
     *     
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Définit la valeur de la propriété database.
     * 
     * @param value
     *     allowed object is
     *     {@link Database }
     *     
     */
    public void setDatabase(Database value) {
        this.database = value;
    }

    /**
     * Obtient la valeur de la propriété browser.
     * 
     * @return
     *     possible object is
     *     {@link Browser }
     *     
     */
    public Browser getBrowser() {
        return browser;
    }

    /**
     * Définit la valeur de la propriété browser.
     * 
     * @param value
     *     allowed object is
     *     {@link Browser }
     *     
     */
    public void setBrowser(Browser value) {
        this.browser = value;
    }

    /**
     * Obtient la valeur de la propriété common.
     * 
     * @return
     *     possible object is
     *     {@link Common }
     *     
     */
    public Common getCommon() {
        return common;
    }

    /**
     * Définit la valeur de la propriété common.
     * 
     * @param value
     *     allowed object is
     *     {@link Common }
     *     
     */
    public void setCommon(Common value) {
        this.common = value;
    }

}
