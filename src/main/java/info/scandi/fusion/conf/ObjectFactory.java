//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.3.0-b170531.0717 
// Voir <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.10.05 à 11:06:26 PM CEST 
//


package info.scandi.fusion.conf;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the info.scandi.fusion.conf package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: info.scandi.fusion.conf
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExclusionSchemas }
     * 
     */
    public ExclusionSchemas createExclusionSchemas() {
        return new ExclusionSchemas();
    }

    /**
     * Create an instance of {@link Liquibase }
     * 
     */
    public Liquibase createLiquibase() {
        return new Liquibase();
    }

    /**
     * Create an instance of {@link Backup }
     * 
     */
    public Backup createBackup() {
        return new Backup();
    }

    /**
     * Create an instance of {@link Init }
     * 
     */
    public Init createInit() {
        return new Init();
    }

    /**
     * Create an instance of {@link Database }
     * 
     */
    public Database createDatabase() {
        return new Database();
    }

    /**
     * Create an instance of {@link Extension }
     * 
     */
    public Extension createExtension() {
        return new Extension();
    }

    /**
     * Create an instance of {@link Extensions }
     * 
     */
    public Extensions createExtensions() {
        return new Extensions();
    }

    /**
     * Create an instance of {@link Option }
     * 
     */
    public Option createOption() {
        return new Option();
    }

    /**
     * Create an instance of {@link Options }
     * 
     */
    public Options createOptions() {
        return new Options();
    }

    /**
     * Create an instance of {@link Browser }
     * 
     */
    public Browser createBrowser() {
        return new Browser();
    }

    /**
     * Create an instance of {@link Common }
     * 
     */
    public Common createCommon() {
        return new Common();
    }

    /**
     * Create an instance of {@link Fusion }
     * 
     */
    public Fusion createFusion() {
        return new Fusion();
    }

}
