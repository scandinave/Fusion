package dbunit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dbunit.xml.Row;
import exception.TatamiException;

/**
 * Génère un fichier XML pour DBUnit
 * @author Scandinave
 * @since 12/04/2014
 * @version 1.0
 */
public abstract class FlatXmlBuilder {

    private long defaultID;

    private String outputPath;

    /** Liste des lignes représentant un tupple d'une table à ajouter. */
    protected final List<Row> rows;

    /** Ligne en cours de traitement. */
    private Row currentRow;

    /**
     * Si false écrit le résultat de la génération dans un fichier xml. Si true l'écrit dans la console.
     */
    private boolean devMode = false;

    /**
     * Génère une nouvel identifiant unique en se basant sur le defaultID. la méthode utilisée est defaultID + 1
     * @return le nouvel identifiant généré
     */
    protected Long newId() {
        this.defaultID++;
        return this.defaultID;
    }

    /**
     * Génère une chaine de caractère aléatoire d'une longeur comprise dans un intervalle.
     * @param start Taille minimale de la chaine.
     * @param end Taille maximale de la chaine.
     * @return La chaine de caractère aléatoire
     */
    protected String randomString(Integer start, Integer end) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Integer length = random.nextInt(start, end);
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return text.toString();
    }

    /**
     * Génère une chaine de caractère aléatoire d'une longeur fixe.
     * @param length Taille de la chaine.
     * @return La chaine de caractère aléatoire
     */
    protected String randomString(Integer length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return text.toString();
    }

    /**
     * Génère une date entre dans l'intervalle d'année spécifier.
     * @param startYear L'année de départ de l'intervalle
     * @param endYear l'année de fin de l'intervalle
     * @return La date généré
     */
    protected Date randomDate(Integer startYear, Integer endYear) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int month = random.nextInt(1, 12);
        int day = 1;
        if (month == GregorianCalendar.FEBRUARY) {
            day = random.nextInt(1, 28);
        } else if (month == GregorianCalendar.APRIL || month == GregorianCalendar.JUNE || month == GregorianCalendar.SEPTEMBER || month == GregorianCalendar.OCTOBER) {
            day = random.nextInt(1, 30);
        } else {
            day = random.nextInt(1, 31);
        }
        GregorianCalendar gc = new GregorianCalendar(random.nextInt(startYear, endYear), random.nextInt(1, 12), day);
        return gc.getTime();
    }

    /**
     * Ajoute ou retire un jour à la date.
     * @param date La date à modifier.
     * @param value la valeur de l'incrément.
     * @return La date modifiée.
     */
    protected Date addDay(Date date, Integer value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(GregorianCalendar.DAY_OF_MONTH, value);
        return gc.getTime();
    }

    /**
     * Ajoute ou retire un mois à la date.
     * @param date La date à modifier.
     * @param value la valeur de l'incrément.
     * @return La date modifiée.
     */
    protected Date addMonth(Date date, Integer value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(GregorianCalendar.MONTH, value);
        return gc.getTime();
    }

    /**
     * Ajoute ou retire un année à la date.
     * @param date La date à modifier.
     * @param value la valeur de l'incrément.
     * @return La date modifiée.
     */
    protected Date addYears(Date date, Integer value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(GregorianCalendar.YEAR, value);
        return gc.getTime();
    }

    /**
     * Retourne une date correctement formatée au format string
     * @param date La date à formater.
     * @return La date formatée.
     */
    protected String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
        return sdf.format(date);
    }

    /**
     * Génère un entier aléatoire
     * @param start Le début de l'interval
     * @param end la fin de l'interval
     * @return L'entier aléatoire.
     */
    protected Integer randomInt(Integer start, Integer end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    /**
     * <p>
     * Génère une adresse aléatoire.
     * </p>
     * <ul>
     * <li>Le numéro de la rue sera compris entre 0 et 100.</li>
     * <li>Le type sera choisi au hasard parmis les suivants : {"Rue", "Allée", "Impasse", "Avenue", "Boulevard", "Chemin", "Route", "Place"}</li>
     * <li>Le nom de la rue sera choisi au hasard parmis les suivants {"du général De Gaulle", "du Maréchal Foch", "Clémenceau", "Alphonse Daudet", "Picasso",
     * "des lilas", "Henri de Monterlan", "de l'Eglise", "de la mairie", "du stade","Pasteur", "Victor-Hugo"}</li>
     * <li>Le code postal sera compris entre 0 et 98000</li>
     * <li>la ville sera choisie parmis la liste suivantes : {"Paris", "Bordeaux", "Nantes", "Lilles", "Marseille", "Rennes", "Vannes", "Lorient", "Toulouse",
     * "Montpellier", "Nice", "Pau", "Aix-en-Provence", "Besançon", "Arles", "Caen","Brest", "Biarritz", "Clermont-Ferrand", "Ajaccio"}</li>
     * </ul>
     * @return l'adresse aléatoire.
     */
    protected String randomAdress() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Integer number = random.nextInt(0, 100);
        String[] type = {"Rue", "Allée", "Impasse", "Avenue", "Boulevard", "Chemin", "Route", "Place"};
        String[] name = {"du général De Gaulle", "du Maréchal Foch", "Clémenceau", "Alphonse Daudet", "Picasso", "des lilas", "Henri de Monterlan", "de l'Eglise", "de la mairie", "du stade",
            "Pasteur", "Victor-Hugo"};
        Integer codePostal = random.nextInt(0, 98000);
        String[] ville = {"Paris", "Bordeaux", "Nantes", "Lilles", "Marseille", "Rennes", "Vannes", "Lorient", "Toulouse", "Montpellier", "Nice", "Pau", "Aix-en-Provence", "Besançon", "Arles", "Caen",
            "Brest", "Biarritz", "Clermont-Ferrand", "Ajaccio"};
        String adress = number.toString() + " " + type[random.nextInt(type.length)] + " " + name[random.nextInt(name.length)] + " " + codePostal + " " + ville[random.nextInt(ville.length)];
        return adress;

    }

    /**
     * Génère un fichier de donnée en XML
     * @param devMode Si false écrit le résultat de la génération dans un fichier xml. Si true l'écrit dans la console.
     * @param defaultStartId Identifiant de base pour la génération automatique d'identifiant unique. Indiquer un grand nombre différent par générateur ( ex :
     *        1000000, 20000000) permet d'éviter les collions en base de donnée.
     */
    public FlatXmlBuilder(String outputPath, boolean devMode, long defaultStartId) {
        super();
        this.outputPath = outputPath;
        this.devMode = devMode;
        this.defaultID = defaultStartId;
        this.rows = new ArrayList<Row>();
    }

    /**
     * Ajoute la Row à la liste des rows à ajouter au fichier xml
     * @param currentRow La row à ajouter
     */
    public void add(Row currentRow) {
        this.currentRow = currentRow;
        this.add();
    }

    /**
     * Démarre la génération du fichier xml.
     * @throws TatamiException
     */
    public void start() throws TatamiException {
        try {
            File outputFile = new File(outputPath);
            if (outputFile.exists()) {
                outputFile.delete();

            } else {
                outputFile.getParentFile().mkdirs();
            }

            this.write(new FileWriter(new File(outputPath),
                true), true, false);
            this.addData();

            this.write(new FileWriter(new File(outputPath),
                true), false, true);
        } catch (Exception e) {
            throw new TatamiException(e);
        }
    }

    /**
     * Permet de créer une nouvelle ligne.
     * @param row {@link Row}
     * @return
     */
    public FlatXmlBuilder newRow(Row row) {
        this.currentRow = row;
        return this;
    }

    /**
     * Permet de créer une nouvelle ligne.
     * @param row {@link String} le nom de la table sur laquel ajouter la row.
     * @return FlatXmlBuilder L'object FlatXmlBuilder pour chainage.
     */
    public FlatXmlBuilder newRow(String row) {
        this.currentRow = new Row(row);
        return this;
    }

    /**
     * Ajoute une valeur pour une colonne.
     * @param columnName {@link String} Le nom de la colonne
     * @param value {@link Object} La valeur de la colonne
     * @return FlatXmlBuilder L'object FlatXmlBuilder pour chainage.
     */
    public FlatXmlBuilder with(String columnName, Object value) {
        this.currentRow.putColumnRow(columnName, value);
        return this;
    }

    /**
     * Méthode utilitaire équivalente à with("id", this.newId());
     * @return {@link FlatXmlBuilder}
     */
    public FlatXmlBuilder withNewId() {
        return this.with("id", this.newId());
    }

    /**
     * Valide la création d'un ligne(row) en base de donnée. Cette méthode est à appeler obligatoirement pour générer une ligne.
     */
    public void add() {
        this.rows.add(this.currentRow);
    }

    /**
     * Permet d'ajouter des données pour alimenter les lignes.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public abstract void addData() throws FileNotFoundException, IOException;

    /**
     * Permet l'écriture des lignes dans un flux.
     * @param out {@link OutputStream}
     * @throws IOException
     */
    protected void write(OutputStream out) throws IOException {
        PrintWriter pW = null;
        if (this.devMode) {
            pW = new PrintWriter(System.out);
        } else {
            pW = new PrintWriter(out);
        }

        pW.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pW.println("<dataset>");
        for (Row row : this.rows) {
            pW.println("\t".concat(row.toString()));
        }
        pW.println("</dataset>");
        pW.close();

        this.rows.clear();
    }

    /**
     * Permet l'écriture des lignes dans un flux.
     * @param out {@link OutputStream}
     * @throws IOException
     */
    protected void write(FileWriter fw, boolean debut, boolean fin)
        throws IOException {
        PrintWriter pW = null;
        if (this.devMode) {
            pW = new PrintWriter(System.out);
        } else {
            pW = new PrintWriter(fw);
        }

        if (debut) {
            pW.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pW.println("<dataset>");
        }
        for (Row row : this.rows) {
            pW.println("\t".concat(row.toString()));
        }
        if (fin) {
            pW.println("</dataset>");
        }
        pW.close();

        this.rows.clear();
    }
}
