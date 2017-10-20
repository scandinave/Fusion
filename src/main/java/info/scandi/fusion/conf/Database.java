//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.10.20 à 07:25:18 PM CEST 
//


package info.scandi.fusion.conf;

import java.math.BigInteger;
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
 *       &lt;all>
 *         &lt;element ref="{http://fusion.scandi.info}liquibase"/>
 *         &lt;element name="driver" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element ref="{http://fusion.scandi.info}backup"/>
 *         &lt;element ref="{http://fusion.scandi.info}init"/>
 *       &lt;/all>
 *       &lt;attribute name="connectionType" default="custom">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="custom"/>
 *             &lt;enumeration value="env"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="allowEmptyString" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="postgresql"/>
 *             &lt;enumeration value="oracle"/>
 *             &lt;enumeration value="mongodb"/>
 *             &lt;enumeration value="mysql"/>
 *             &lt;enumeration value="mariadb"/>
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

})
@XmlRootElement(name = "database")
public class Database {

    @XmlElement(required = true)
    protected Liquibase liquibase;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String driver;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String host;
    @XmlElement(required = true)
    protected BigInteger port;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String username;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String password;
    @XmlElement(required = true)
    protected Backup backup;
    @XmlElement(required = true)
    protected Init init;
    @XmlAttribute(name = "connectionType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String connectionType;
    @XmlAttribute(name = "allowEmptyString")
    protected Boolean allowEmptyString;
    @XmlAttribute(name = "enabled")
    protected Boolean enabled;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * Obtient la valeur de la propriété liquibase.
     * 
     * @return
     *     possible object is
     *     {@link Liquibase }
     *     
     */
    public Liquibase getLiquibase() {
        return liquibase;
    }

    /**
     * Définit la valeur de la propriété liquibase.
     * 
     * @param value
     *     allowed object is
     *     {@link Liquibase }
     *     
     */
    public void setLiquibase(Liquibase value) {
        this.liquibase = value;
    }

    /**
     * Obtient la valeur de la propriété driver.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Définit la valeur de la propriété driver.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriver(String value) {
        this.driver = value;
    }

    /**
     * Obtient la valeur de la propriété host.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Définit la valeur de la propriété host.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Obtient la valeur de la propriété port.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPort() {
        return port;
    }

    /**
     * Définit la valeur de la propriété port.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPort(BigInteger value) {
        this.port = value;
    }

    /**
     * Obtient la valeur de la propriété name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Définit la valeur de la propriété name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtient la valeur de la propriété username.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Définit la valeur de la propriété username.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Obtient la valeur de la propriété password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Définit la valeur de la propriété password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Obtient la valeur de la propriété backup.
     * 
     * @return
     *     possible object is
     *     {@link Backup }
     *     
     */
    public Backup getBackup() {
        return backup;
    }

    /**
     * Définit la valeur de la propriété backup.
     * 
     * @param value
     *     allowed object is
     *     {@link Backup }
     *     
     */
    public void setBackup(Backup value) {
        this.backup = value;
    }

    /**
     * Obtient la valeur de la propriété init.
     * 
     * @return
     *     possible object is
     *     {@link Init }
     *     
     */
    public Init getInit() {
        return init;
    }

    /**
     * Définit la valeur de la propriété init.
     * 
     * @param value
     *     allowed object is
     *     {@link Init }
     *     
     */
    public void setInit(Init value) {
        this.init = value;
    }

    /**
     * Obtient la valeur de la propriété connectionType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionType() {
        if (connectionType == null) {
            return "custom";
        } else {
            return connectionType;
        }
    }

    /**
     * Définit la valeur de la propriété connectionType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionType(String value) {
        this.connectionType = value;
    }

    /**
     * Obtient la valeur de la propriété allowEmptyString.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAllowEmptyString() {
        if (allowEmptyString == null) {
            return true;
        } else {
            return allowEmptyString;
        }
    }

    /**
     * Définit la valeur de la propriété allowEmptyString.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllowEmptyString(Boolean value) {
        this.allowEmptyString = value;
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
            return true;
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
     * Obtient la valeur de la propriété type.
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
     * Définit la valeur de la propriété type.
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
