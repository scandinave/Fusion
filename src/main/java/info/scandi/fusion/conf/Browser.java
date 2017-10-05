//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.3.0-b170531.0717 
// Voir <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2017.10.05 � 11:06:26 PM CEST 
//


package info.scandi.fusion.conf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://fusion.scandi.info}extensions" minOccurs="0"/&gt;
 *         &lt;element name="binary" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="downloadDir" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element ref="{http://fusion.scandi.info}options" minOccurs="0"/&gt;
 *         &lt;element name="grid" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="remote" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="type" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *                 &lt;enumeration value="firefox"/&gt;
 *                 &lt;enumeration value="safari"/&gt;
 *                 &lt;enumeration value="edge"/&gt;
 *                 &lt;enumeration value="ie"/&gt;
 *                 &lt;enumeration value="opera"/&gt;
 *                 &lt;enumeration value="chrome"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * Obtient la valeur de la propri�t� extensions.
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
     * D�finit la valeur de la propri�t� extensions.
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
     * Obtient la valeur de la propri�t� binary.
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
     * D�finit la valeur de la propri�t� binary.
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
     * Obtient la valeur de la propri�t� downloadDir.
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
     * D�finit la valeur de la propri�t� downloadDir.
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
     * Obtient la valeur de la propri�t� options.
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
     * D�finit la valeur de la propri�t� options.
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
     * Obtient la valeur de la propri�t� grid.
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
     * D�finit la valeur de la propri�t� grid.
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
     * Obtient la valeur de la propri�t� enabled.
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
     * D�finit la valeur de la propri�t� enabled.
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
     * Obtient la valeur de la propri�t� remote.
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
     * D�finit la valeur de la propri�t� remote.
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
     * Obtient la valeur de la propri�t� type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * D�finit la valeur de la propri�t� type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
