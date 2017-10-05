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
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="schemaName" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="databaseChangelogName" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element ref="{http://fusion.scandi.info}exclusionSchemas" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "schemaName",
    "databaseChangelogName",
    "exclusionSchemas"
})
@XmlRootElement(name = "liquibase")
public class Liquibase {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String schemaName;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String databaseChangelogName;
    protected ExclusionSchemas exclusionSchemas;
    @XmlAttribute(name = "enabled")
    protected Boolean enabled;

    /**
     * Obtient la valeur de la propri�t� schemaName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * D�finit la valeur de la propri�t� schemaName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemaName(String value) {
        this.schemaName = value;
    }

    /**
     * Obtient la valeur de la propri�t� databaseChangelogName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabaseChangelogName() {
        return databaseChangelogName;
    }

    /**
     * D�finit la valeur de la propri�t� databaseChangelogName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabaseChangelogName(String value) {
        this.databaseChangelogName = value;
    }

    /**
     * Obtient la valeur de la propri�t� exclusionSchemas.
     * 
     * @return
     *     possible object is
     *     {@link ExclusionSchemas }
     *     
     */
    public ExclusionSchemas getExclusionSchemas() {
        return exclusionSchemas;
    }

    /**
     * D�finit la valeur de la propri�t� exclusionSchemas.
     * 
     * @param value
     *     allowed object is
     *     {@link ExclusionSchemas }
     *     
     */
    public void setExclusionSchemas(ExclusionSchemas value) {
        this.exclusionSchemas = value;
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

}
