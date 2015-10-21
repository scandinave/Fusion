/**
 * 
 */
package runner;

import exception.ConfigurationException;
import exception.TatamiException;
import exception.UtilitaireException;
import utils.PropsUtils;

/**
 * @author Scandinave
 */
public class ConfigurationManager {

    private final static String ROOT_PATH = "tatami.rootPath";
    private final static String PROPERTY_DATABASE_URL = "database.url";
    private final static String PROPERTY_DATABASE_DRIVER = "database.driver";
    private final static String PROPERTY_DATABASE_USERNAME = "database.username";
    private final static String PROPERTY_DATABASE_PASSWORD = "database.password";

    private final static String PROPERTY_XML_FILE_PURGE = "database.optionalXmlFilePurge";

    private final static String PROPERTY_AVEC_LIQUIBASE = "database.optionalAvecLiquibase";
    private final static String PROPERTY_XML_FILE_LIQUIBASE = "database.optionalXmlFileLiquibase";
    private static final String PROPERTY_LIQUIBASE_SCHEMA_NAME = "database.optionalLiquibaseSchemaName";
    private static final String PROPERTY_LIQUIBASE_DATABASECHANGELOG = "database.optionalLiquibaseDatabasechangelogName";

    private final static String PROPERTY_AVEC_INIT = "database.optionalAvecInit";
    private final static String PROPERTY_XML_FILE_INIT = "database.optionalXmlFileInit";

    private final static String PROPERTY_AVEC_SAUVEGARDE = "database.optionalAvecSauvegarde";
    private final static String PROPERTY_XML_DIRECTORY_SAVE = "database.optionalXmlDirectorySave";

    private final static String PROPERTY_REPLACE_EMPTY_DATABASE_VALUE = "tatami.allowEmptyString";

    private static String rootPath;

    private Scanner scanner;
    private static ConfigurationManager instance;

    /**
     * @throws UtilitaireException
     */
    private ConfigurationManager() throws TatamiException {
        scanner = Scanner.getInstance();

        try {
            rootPath = PropsUtils.getProperties().getProperty(ROOT_PATH);
        } catch (UtilitaireException e) {
            throw new TatamiException(new ConfigurationException(e));
        }
    }

    public static ConfigurationManager getInstance() throws TatamiException {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private boolean isAnnotation(String property) throws UtilitaireException {
        if (!PropsUtils.exist()) { // Le fichier de propertie est absent
            return true;
        } else {
            if (PropsUtils.getProperties().getProperty(property) != null) { // La propriété existe
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * L'url de connexion à la base de donnée sous la forme jdbc:'databaseType'://'hote':'port'/'databaseName'
     * @return L'url de connexion à la base de donnée.
     * @throws TatamiException
     */
    public String getDatabaseUrl() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_DATABASE_URL)) {
                if (scanner.getWorker() != null) {
                    return scanner.getWorker().url();
                } else {
                    return null;
                }
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_URL);
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * L'identifiant de connexion à la base de donnée.
     * @return l'identifiant de connexion à la base de donnée.
     * @throws UtilitaireException
     * @throws ConfigurationException l'indentifiant n'est pas trouvé dans le fichier properties ou dans l'annotation @Worker
     */
    public String getDatabaseUsername() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_DATABASE_USERNAME)) {
                if (scanner.getWorker() != null) {
                    return scanner.getWorker().username();
                } else {
                    return null;
                }
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_USERNAME);
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Le mot de passe de connexion à la base de donnée.
     * @return le mot de passe de connexion à la base de donnée.
     * @throws UtilitaireException
     * @throws ConfigurationException le mot de passe n'est pas trouvé dans le fichier properties ou dans l'annotation @Worker
     */
    public String getDatabasePassword() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_DATABASE_PASSWORD)) {
                if (scanner.getWorker() != null) {
                    return scanner.getWorker().password();
                } else {
                    return null;
                }
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_PASSWORD);
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Le driver de connexion à la base de donnée.
     * @return le driver de connexion à la base de donnée.
     * @throws UtilitaireException
     * @throws ConfigurationException le driver n'est pas trouvé dans le fichier properties ou dans l'annotation @Worker
     */
    public String getDatabaseDriver() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_DATABASE_DRIVER)) {
                if (scanner.getWorker() != null) {
                    return scanner.getWorker().driver();
                } else {
                    return null;
                }
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_DATABASE_DRIVER);
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * L'emplacement du fichier de purge
     * @return l'emplacement du fichier de purge.
     * @throws UtilitaireException
     * @throws ConfigurationException le driver n'est pas trouvé dans le fichier properties ou dans l'annotation @Worker
     */
    public String getPurgeFilePath() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_XML_FILE_PURGE)) {
                if (scanner.getWorker() != null) {
                    return scanner.getWorker().purgeFilePath();
                } else {
                    return null;
                }
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_PURGE, rootPath + "flatXmlDataSet/init/purge.xml");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Par défaut les chaines de caractère vide seront remplacé par un null.
     * @return True si les chaines de caractère vide doivent être laissé telle quelle. False sinon.
     * @throws UtilitaireException
     * @throws ConfigurationException Problème de configuration de l'annotation.
     */
    public boolean allowEmptyString() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_REPLACE_EMPTY_DATABASE_VALUE)) {
                return scanner.getLiquibase() != null;
            } else {
                return Boolean.valueOf(PropsUtils.getProperties().getProperty(PROPERTY_REPLACE_EMPTY_DATABASE_VALUE, "false"));
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Détermine si liquibase est activé sur le projet.
     * @return True si liquibase est activé, false sinon.
     * @throws UtilitaireException
     * @throws ConfigurationException Problème de configuration de l'annotation.
     */
    public boolean isLiquibaseEnable() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_AVEC_LIQUIBASE)) {
                return scanner.getLiquibase() != null;
            } else {
                return Boolean.valueOf(PropsUtils.getProperties().getProperty(PROPERTY_AVEC_LIQUIBASE, "false"));
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Le chemin du fichier xml utilisée par liquibase.
     * @return le chemin du fichier xml utilisée par liquibase.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public String getLiquibaseFile() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_XML_FILE_LIQUIBASE) && scanner.getLiquibase() != null) {
                return scanner.getLiquibase().xmlFilepath();
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_LIQUIBASE, rootPath + "flatXmlDataSet/init/liquibase.xml");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Le schema utilisée par liquibase.
     * @return le schema utilisée par liquibase.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public String getLiquibaseSchema() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_LIQUIBASE_SCHEMA_NAME) && scanner.getLiquibase() != null) {
                return scanner.getLiquibase().schema();
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_LIQUIBASE_SCHEMA_NAME, "liquibase");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * La table utilisée par liquibase.
     * @return la table utilisée par liquibase.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public String getLiquibaseChangelogName() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_LIQUIBASE_DATABASECHANGELOG) && scanner.getLiquibase() != null) {
                return scanner.getLiquibase().changelogName();
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_LIQUIBASE_DATABASECHANGELOG, "databasechangelog");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Détermine si la fonction de sauvegarde/restauration doit être activé.
     * @return True si la fonction de sauvegarde/restauration doit être activé. False sinon.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public boolean isSaveEnable() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_AVEC_SAUVEGARDE)) {
                return scanner.getSave() != null;
            } else {
                return Boolean.valueOf(PropsUtils.getProperties().getProperty(PROPERTY_AVEC_SAUVEGARDE, "false"));
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * La table utilisée par liquibase.
     * @return la table utilisée par liquibase.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public String getSaveDirectory() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_XML_DIRECTORY_SAVE) && scanner.getSave() != null) {
                return scanner.getSave().directoryPath();
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_XML_DIRECTORY_SAVE, rootPath + "flatXmlDataSet/save");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Détermine si des données nécéssaire au démarrage du serveur doivent être chargé.
     * @return True si des données nécéssaire au démarrage du serveur doivent être chargé.. False sinon.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public boolean isInitialLoad() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_AVEC_INIT)) {
                return scanner.getInit() != null;
            } else {
                return Boolean.valueOf(PropsUtils.getProperties().getProperty(PROPERTY_AVEC_INIT, "false"));
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * L'emplacement du fichier contenant les données nécéssaire au démarrage du serveur doivent être chargé.
     * @return l'emplacement du fichier contenant les données nécéssaire au démarrage du serveur doivent être chargé.
     * @throws UtilitaireException
     * @throws ConfigurationException
     */
    public String getInitialLoadFile() throws TatamiException {
        try {
            if (isAnnotation(PROPERTY_XML_FILE_INIT) && scanner.getSave() != null) {
                return scanner.getSave().directoryPath();
            } else {
                return PropsUtils.getProperties().getProperty(PROPERTY_XML_FILE_INIT, rootPath + "flatXmlDataSet/init/init.xml");
            }
        } catch (UtilitaireException | ConfigurationException e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Méthode getCommunDir.
     * @param rootPath
     * @return
     * @throws TatamiException
     * @throws UtilitaireException
     */
    public static String getCommunDir() throws TatamiException {
        String flatxmldatasetDir = rootPath + "flatXmlDataSet";
        return flatxmldatasetDir.concat("/commun");
    }

    /**
     * Méthode getCommunDir.
     * @return
     * @throws TatamiException
     * @throws UtilitaireException
     */
    public static String getDistinctDir() throws TatamiException {
        String flatxmldatasetDir = rootPath + "flatXmlDataSet";
        return flatxmldatasetDir.concat("/distinct");
    }

}
