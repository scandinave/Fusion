//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.10.03 à 10:13:22 PM CEST 
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
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="schemaName" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="databaseChangelogName" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element ref="{http://fusion.scandi.info}exclusionSchemas" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
     * Obtient la valeur de la propriété schemaName.
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
     * Définit la valeur de la propriété schemaName.
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
     * Obtient la valeur de la propriété databaseChangelogName.
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
     * Définit la valeur de la propriété databaseChangelogName.
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
     * Obtient la valeur de la propriété exclusionSchemas.
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
     * Définit la valeur de la propriété exclusionSchemas.
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

}
