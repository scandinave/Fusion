//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.3.0-b170531.0717 
// Voir <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2017.10.11 � 09:24:59 PM CEST 
//


package info.scandi.fusion.conf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="appUrl" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="rootPath" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="logPath" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="screenshotPath" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "common")
public class Common {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String appUrl;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String rootPath;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String logPath;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String screenshotPath;

    /**
     * Obtient la valeur de la propri�t� appUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppUrl() {
        return appUrl;
    }

    /**
     * D�finit la valeur de la propri�t� appUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppUrl(String value) {
        this.appUrl = value;
    }

    /**
     * Obtient la valeur de la propri�t� rootPath.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * D�finit la valeur de la propri�t� rootPath.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRootPath(String value) {
        this.rootPath = value;
    }

    /**
     * Obtient la valeur de la propri�t� logPath.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogPath() {
        return logPath;
    }

    /**
     * D�finit la valeur de la propri�t� logPath.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogPath(String value) {
        this.logPath = value;
    }

    /**
     * Obtient la valeur de la propri�t� screenshotPath.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreenshotPath() {
        return screenshotPath;
    }

    /**
     * D�finit la valeur de la propri�t� screenshotPath.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreenshotPath(String value) {
        this.screenshotPath = value;
    }

}
