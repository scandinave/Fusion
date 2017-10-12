//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.3.0-b170531.0717 
// Voir <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2017.10.12 � 10:11:45 PM CEST 
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
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element ref="{http://fusion.scandi.info}liquibase"/&gt;
 *         &lt;element name="driver" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *         &lt;element name="port" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *         &lt;element ref="{http://fusion.scandi.info}backup"/&gt;
 *         &lt;element ref="{http://fusion.scandi.info}init"/&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="connectionType" default="custom"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *             &lt;enumeration value="custom"/&gt;
 *             &lt;enumeration value="env"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="allowEmptyString" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
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

    /**
     * Obtient la valeur de la propri�t� liquibase.
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
     * D�finit la valeur de la propri�t� liquibase.
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
     * Obtient la valeur de la propri�t� driver.
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
     * D�finit la valeur de la propri�t� driver.
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
     * Obtient la valeur de la propri�t� host.
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
     * D�finit la valeur de la propri�t� host.
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
     * Obtient la valeur de la propri�t� port.
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
     * D�finit la valeur de la propri�t� port.
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
     * Obtient la valeur de la propri�t� name.
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
     * D�finit la valeur de la propri�t� name.
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
     * Obtient la valeur de la propri�t� username.
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
     * D�finit la valeur de la propri�t� username.
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
     * Obtient la valeur de la propri�t� password.
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
     * D�finit la valeur de la propri�t� password.
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
     * Obtient la valeur de la propri�t� backup.
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
     * D�finit la valeur de la propri�t� backup.
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
     * Obtient la valeur de la propri�t� init.
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
     * D�finit la valeur de la propri�t� init.
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
     * Obtient la valeur de la propri�t� connectionType.
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
     * D�finit la valeur de la propri�t� connectionType.
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
     * Obtient la valeur de la propri�t� allowEmptyString.
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
     * D�finit la valeur de la propri�t� allowEmptyString.
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
     * Obtient la valeur de la propri�t� enabled.
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
