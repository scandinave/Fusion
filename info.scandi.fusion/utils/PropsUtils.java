package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import exception.UtilitaireException;

/**
 * Chargement des propriétés pour l'application
 * @author Scandinave
 */
public class PropsUtils {

    /**
     * Logger de la classe.
     */
    private final static Log LOGGER = LogFactory.getLog(PropsUtils.class);


    /**
     * Format des clés des propriétés à concerver. Explication : - (%s) : 1 chaîne de caractère apposée via String.format trouvée 0 ou 1 fois (condition), - *
     * [a-zA-Z]* : 1 chaîne alphabétique de n caractères, - \\. : 1 caractère point, - [a-zA-Z]* : 1 chaîne alphabétique de n caractères.
     */
    public final static String KEY_PATTERN_FORMAT = "^(%s)?[a-zA-Z]*\\.[a-zA-Z]*$";

    /** Objet Properties */
    private static Properties properties = null;

    /** Nom du fichier Properties */
    private static final String NAME = "tatami.properties";

    /** Retourne l'objet Properties initialisé si il ne l'était pas */
    public static Properties getProperties() throws UtilitaireException {
        if (properties == null) {
            load();
        }
        return properties;
    }

    /**
     * Chargement du fichier properties en fonction du chemin d'accès en paramètre
     */
    private static void load() throws UtilitaireException {
        LOGGER.debug("Récupération des propriétés.");
        // Séquence à supprimer dans les clé des propiétés.
        String userNameSequence = System.getProperty("user.name").concat(".");
        // Pattern permettant de récupérer que les propriétés efficaces.
        String keyPattern = String.format(KEY_PATTERN_FORMAT, userNameSequence);
        // Récupération des propriétés efficaces pour le traitement en fonction
        // de l'utilisateur qui lance les tests.
        File file = new File(NAME);
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new UtilitaireException(e);
        }
        properties = new ReferenceProperties(fileInputStream, keyPattern, userNameSequence);
        LOGGER.debug(
            String.format("%d propriétés chargées.", properties.size()));
    }
}
