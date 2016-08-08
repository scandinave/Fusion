package info.scandi.fusion.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Permet de positionner les fichiers utilisables pour les tests.
 * @author Scandinave
 * @since 04/06/2014
 * @version 1.0
 */
public class FileUtil {

    /** Interdit l'instanciation. */
    private FileUtil() {}

    /**
     * Permet de créer des répertoires.
     * @param dirPaths {@link String...}
     */
    public static void createDirectories(String... dirPaths) {
        // Parcours des répertoires.
        for (String dirPath : dirPaths) {
            File f = new File(dirPath);
            // création des répertoires.
            f.mkdirs();
        }
    }

    /**
     * Permet de supprimer les fichiers des répertoires passés en paramètre.
     * @param dirPaths {@link String...}
     */
    public static void cleanDirectories(String... dirPaths) {
        // Parcours des répertoires.
        for (String dirPath : dirPaths) {
            File f = new File(dirPath);
            if (f.exists()) {
                // suppression des fichiers du repertoire.
                for (File file : f.listFiles()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Permet copier un fichier.
     * @param source {@link String} chemein du fichier source.
     * @param target {@link String} chemein du fichier cible.
     * @throws IOException
     */
    public static void upload(String source, String target) throws IOException {
        InputStream is = new FileInputStream(new File(source));
        OutputStream os = new FileOutputStream(new File(target));
        try {
            byte[] buffer = new byte[is.available()];

            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
        } finally {
            os.close();
            is.close();
        }
    }

}