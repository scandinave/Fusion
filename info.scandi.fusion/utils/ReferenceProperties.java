package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.UtilitaireException;

/**
 * Permet de gérer des propriétés avec des références à d'autres variable du fichier ou des propriétés système. Format attendu : {NOM_PROPRIETE}
 */
public class ReferenceProperties extends Properties {

    /** Numéro de serialisation de la classe. */
    private static final long serialVersionUID = -8175703816722146449L;

    /**
     * Pattern comprenant l'expression rationnelle permettant de déterminer une valeur de référence (exemple : {MA_PROPRIETE})
     */
    private final static Pattern SUBSTITUTE_PATTERN = Pattern
        .compile("\\{([^\\{\\}]*)\\}");

    /**
     * 
     */
    public ReferenceProperties() {
        super();
    }

    /**
     * @param arg0
     */
    public ReferenceProperties(Properties arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param keyPattern
     * @param ReplacementSequence
     * @throws UtilitaireException
     */
    public ReferenceProperties(InputStream arg0, String keyPattern,
        String ReplacementSequence) throws UtilitaireException {
        this.loadWithPatternMatches(arg0, keyPattern, ReplacementSequence);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Properties#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String arg0) {
        return this.replaceReferenceValue(super.getProperty(arg0));
    }

    /*
     * (non-Javadoc)
     * @see java.util.Hashtable#get(java.lang.Object)
     */
    @Override
    public synchronized Object get(Object arg0) {
        return this.replaceReferenceValue(super.get(arg0).toString());
    }

    /**
     * Permet de remplacer la valeur d'une propriété.
     * @param arg0 {@link Object} valeur de la propriété.
     * @return {@link Object} la nouvelle valeur si besoin.
     */
    private String replaceReferenceValue(String arg0) {
        if (arg0 == null) {
            return null;
        }

        Matcher matcher = SUBSTITUTE_PATTERN.matcher(arg0);

        while (matcher.find()) {
            // valeur du groupe (exemple : MA_PROPRIETE)
            String keySubstituteProperty = matcher.group(matcher.groupCount());
            String referenceValue = super.getProperty(keySubstituteProperty,
                System.getProperty(keySubstituteProperty));

            if (referenceValue != null) {
                arg0 = arg0.replace(matcher.group(), referenceValue);
            }
        }

        return arg0;
    }

    /**
     * Permet de charger un fichier de propriété.
     * @param arg0 {@link InputStream} : flux en entrée.
     * @param keyPattern {@link String} : pattern des propriétés à conserver.
     * @param ReplacementSequence {@link String} : chaîne de caractères devant être éventuellement supprimée du nom de la clé.
     * @throws UtilitaireException
     */
    public void loadWithPatternMatches(InputStream arg0, String keyPattern,
        String ReplacementSequence) throws UtilitaireException {
        Properties propTemp = new Properties();
        try {
            propTemp.load(arg0);
        } catch (IOException e) {
            throw new UtilitaireException(e);
        }
        // Si keyPattern est null, on va, par défaut, rechercher n'importe quelle chaîne de caractères.
        Pattern patternKey = Pattern.compile(keyPattern == null ? "^.*$" : keyPattern);
        for (Object objKey : propTemp.keySet()) {
            String key = objKey.toString();
            Matcher matcher = patternKey.matcher(key);
            // Cas où la clé correspond au pattern.
            if (matcher.find()) {
                // on remplace rien si ReplacementSequence est null.
                super.put(
                    key.replace(ReplacementSequence == null
                        ? ""
                        : ReplacementSequence, ""),
                    propTemp.getProperty(key));
            }
        }
        propTemp = null;
    }
}
