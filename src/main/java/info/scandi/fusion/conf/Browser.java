//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.10.03 à 10:13:22 PM CEST 
//


package info.scandi.fusion.conf;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://fusion.scandi.info}extensions" minOccurs="0"/>
 *         &lt;element name="binary" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="downloadDir" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element ref="{http://fusion.scandi.info}options" minOccurs="0"/>
 *         &lt;element name="grid" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="remote" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction>
 *             &lt;simpleType>
 *               &lt;list>
 *                 &lt;simpleType>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                     &lt;enumeration value="firefox"/>
 *                     &lt;enumeration value="safari"/>
 *                     &lt;enumeration value="edge"/>
 *                     &lt;enumeration value="ie"/>
 *                     &lt;enumeration value="opera"/>
 *                     &lt;enumeration value="chrome"/>
 *                   &lt;/restriction>
 *                 &lt;/simpleType>
 *               &lt;/list>
 *             &lt;/simpleType>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extensions",
    "binary",
    "downloadDir",
    "options",
    "grid"
})
@XmlRootElement(name = "browser")
public class Browser {

    protected Extensions extensions;
    @XmlSchemaType(name = "anyURI")
    protected String binary;
    @XmlSchemaType(name = "anyURI")
    protected String downloadDir;
    protected Options options;
    @XmlSchemaType(name = "anyURI")
    protected String grid;
    @XmlAttribute(name = "enabled")
    protected Boolean enabled;
    @XmlAttribute(name = "remote")
    protected Boolean remote;
    @XmlAttribute(name = "type", required = true)
    protected List<String> type;

    /**
     * Obtient la valeur de la propriété extensions.
     * 
     * @return
     *     possible object is
     *     {@link Extensions }
     *     
     */
    public Extensions getExtensions() {
        return extensions;
    }

    /**
     * Définit la valeur de la propriété extensions.
     * 
     * @param value
     *     allowed object is
     *     {@link Extensions }
     *     
     */
    public void setExtensions(Extensions value) {
        this.extensions = value;
    }

    /**
     * Obtient la valeur de la propriété binary.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinary() {
        return binary;
    }

    /**
     * Définit la valeur de la propriété binary.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinary(String value) {
        this.binary = value;
    }

    /**
     * Obtient la valeur de la propriété downloadDir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownloadDir() {
        return downloadDir;
    }

    /**
     * Définit la valeur de la propriété downloadDir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownloadDir(String value) {
        this.downloadDir = value;
    }

    /**
     * Obtient la valeur de la propriété options.
     * 
     * @return
     *     possible object is
     *     {@link Options }
     *     
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Définit la valeur de la propriété options.
     * 
     * @param value
     *     allowed object is
     *     {@link Options }
     *     
     */
    public void setOptions(Options value) {
        this.options = value;
    }

    /**
     * Obtient la valeur de la propriété grid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrid() {
        return grid;
    }

    /**
     * Définit la valeur de la propriété grid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrid(String value) {
        this.grid = value;
    }

    /**
     * Obtient la valeur de la propriété enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isEnabled() {
        if (enabled == null) {
            return false;
        } else {
            return enabled;
        }
    }

    /**
     * Définit la valeur de la propriété enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnabled(Boolean value) {
        this.enabled = value;
    }

    /**
     * Obtient la valeur de la propriété remote.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRemote() {
        if (remote == null) {
            return false;
        } else {
            return remote;
        }
    }

    /**
     * Définit la valeur de la propriété remote.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemote(Boolean value) {
        this.remote = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the type property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getType() {
        if (type == null) {
            type = new ArrayList<String>();
        }
        return this.type;
    }

}
