package info.scandi.fusion.database;

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

import info.scandi.fusion.database.xml.Row;
import info.scandi.fusion.exception.FusionException;

/**
 * Base class for xml generator. All generator should extend from this class.
 * 
 * @author Scandinave
 * @since 12/04/2014
 * @version 1.0
 */
public abstract class FlatXmlBuilder {

	private long defaultID;

	/**
	 * Path where the generated file will be stored.
	 */
	private String outputPath;

	/**
	 * Represents a line in the xml file and a tuple in the table of the
	 * database
	 */
	protected final List<Row> rows;

	/** Processed row */
	private Row currentRow;

	/**
	 * If true write the generated result in console instead of a file. Default
	 * to false.
	 */
	private boolean devMode = false;

	/**
	 * Computes a new unique id that id equal to current id + 1.
	 * This method must be use instead of an arbitrary id to avoid collisions
	 * and avoid to the next developers to search for the last created id into
	 * the generator.
	 * 
	 * @return The new unique id.
	 */
	protected Long newId() {
		this.defaultID++;
		return this.defaultID;
	}

	/**
	 * Generates a random string with a size comprised between start and end.
	 * 
	 * @param start
	 *            Minimal size of the string
	 * @param end
	 *            Maximal size of the string
	 * @return The generated string.
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
	 * Generates a random string with a size comprised between 0 and end.
	 * 
	 * @param length
	 *            size of the string
	 * @return The generated string.
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
	 * Generates a random date between to years.
	 * 
	 * @param startYear
	 *            The minimal year
	 * @param endYear
	 *            The maximal year
	 * @return The generated date.
	 */
	protected Date randomDate(Integer startYear, Integer endYear) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int month = random.nextInt(1, 12);
		int day = 1;
		if (month == GregorianCalendar.FEBRUARY) {
			day = random.nextInt(1, 28);
		} else if (month == GregorianCalendar.APRIL || month == GregorianCalendar.JUNE
				|| month == GregorianCalendar.SEPTEMBER || month == GregorianCalendar.OCTOBER) {
			day = random.nextInt(1, 30);
		} else {
			day = random.nextInt(1, 31);
		}
		GregorianCalendar gc = new GregorianCalendar(random.nextInt(startYear, endYear), random.nextInt(1, 12), day);
		return gc.getTime();
	}

	/**
	 * Adds or remove days to a date.
	 * 
	 * @param date
	 *            The date in which add the days
	 * @param value
	 *            The number of days to add.
	 * @return The new date augmented with the number of mdays.
	 */
	protected Date addDay(Date date, Integer value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.DAY_OF_MONTH, value);
		return gc.getTime();
	}

	/**
	 * Adds or remove months to a date.
	 * 
	 * @param date
	 *            The date in which add the months
	 * @param value
	 *            The number of months to add.
	 * @return The new date augmented with the number of months.
	 */
	protected Date addMonth(Date date, Integer value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.MONTH, value);
		return gc.getTime();
	}

	/**
	 * Adds or remove years to a date.
	 * 
	 * @param date
	 *            The date in which add the years
	 * @param value
	 *            The number of years to add.
	 * @return The new date augmented with the number of years.
	 */
	protected Date addYears(Date date, Integer value) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(GregorianCalendar.YEAR, value);
		return gc.getTime();
	}

	/**
	 * Returns the date format as string with the following format dd/MM/yyyy
	 * HH:mm:SS
	 * 
	 * @param date
	 *            Date to format.
	 * @return The formated date.
	 */
	protected String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		return sdf.format(date);
	}

	/**
	 * Generates a random Integer with a size comprised between start and end.
	 * 
	 * @param start
	 *            Minimal size of the Integer
	 * @param end
	 *            Maximal size of the Integer
	 * @return The generated Integer.
	 */
	protected Integer randomInt(Integer start, Integer end) {
		return ThreadLocalRandom.current().nextInt(start, end);
	}

	/**
	 * <p>
	 * Generate a random address.
	 * </p>
	 * <ul>
	 * <li>Address number will be between 0 and 100</li>
	 * <li>Type will be choose randomly inside : {"Rue", "Allée", "Impasse",
	 * "Avenue", "Boulevard", "Chemin", "Route", "Place"}</li>
	 * <li>Le nom de la rue sera choisi au hasard parmis les suivants {"du
	 * général De Gaulle", "du Maréchal Foch", "Clémenceau", "Alphonse Daudet",
	 * "Picasso",
	 * "des lilas", "Henri de Monterlan", "de l'Eglise", "de la mairie", "du
	 * stade","Pasteur", "Victor-Hugo"}</li>
	 * <li>Le code postal sera compris entre 0 et 98000</li>
	 * <li>la ville sera choisie parmis la liste suivantes : {"Paris",
	 * "Bordeaux", "Nantes", "Lilles", "Marseille", "Rennes", "Vannes",
	 * "Lorient", "Toulouse",
	 * "Montpellier", "Nice", "Pau", "Aix-en-Provence", "Besançon", "Arles",
	 * "Caen","Brest", "Biarritz", "Clermont-Ferrand", "Ajaccio"}</li>
	 * </ul>
	 * 
	 * @return l'adresse aléatoire.
	 */
	protected String randomAdress() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		Integer number = random.nextInt(0, 100);
		String[] type = { "Rue", "Allée", "Impasse", "Avenue", "Boulevard", "Chemin", "Route", "Place" };
		String[] name = { "du général De Gaulle", "du Maréchal Foch", "Clémenceau", "Alphonse Daudet", "Picasso",
				"des lilas", "Henri de Monterlan", "de l'Eglise", "de la mairie", "du stade", "Pasteur",
				"Victor-Hugo" };
		Integer codePostal = random.nextInt(0, 98000);
		String[] ville = { "Paris", "Bordeaux", "Nantes", "Lilles", "Marseille", "Rennes", "Vannes", "Lorient",
				"Toulouse", "Montpellier", "Nice", "Pau", "Aix-en-Provence", "Besançon", "Arles", "Caen", "Brest",
				"Biarritz", "Clermont-Ferrand", "Ajaccio" };
		String adress = number.toString() + " " + type[random.nextInt(type.length)] + " "
				+ name[random.nextInt(name.length)] + " " + codePostal + " " + ville[random.nextInt(ville.length)];
		return adress;

	}

	/**
	 * Default Constructor
	 * 
	 * @param outputPath
	 *            Path where the generated file will be stored.
	 * @param devMode
	 *            If true write the generated result in console instead of a
	 *            file. Default to false.
	 * @param defaultStartId
	 *            Number from where to start the id generation.
	 */
	public FlatXmlBuilder(String outputPath, boolean devMode, long defaultStartId) {
		super();
		this.outputPath = outputPath;
		this.devMode = devMode;
		this.defaultID = defaultStartId;
		this.rows = new ArrayList<Row>();
	}

	/**
	 * Adds the row to the list of row that will be generated into the xml file.
	 * 
	 * @param currentRow
	 *            The row to add.
	 */
	protected void add(Row currentRow) {
		this.currentRow = currentRow;
		this.add();
	}

	/**
	 * Start the xml file generation.
	 * 
	 * @throws FusionException
	 */
	public void start() throws FusionException {
		try {
			File outputFile = new File(outputPath);
			if (outputFile.exists()) {
				outputFile.delete();

			} else {
				outputFile.getParentFile().mkdirs();
			}

			this.write(new FileWriter(new File(outputPath), true), true, false);
			this.addData();

			this.write(new FileWriter(new File(outputPath), true), false, true);
		} catch (Exception e) {
			throw new FusionException(e);
		}
	}

	/**
	 * Creates a new row.
	 * 
	 * @param row
	 *            {@link String} Name of the table in which the row will be add.
	 * @return FlatXmlBuilder The generator.
	 */
	protected FlatXmlBuilder newRow(String row) {
		this.currentRow = new Row(row);
		return this;
	}

	/**
	 * Adds value to the row.
	 * 
	 * @param columnName
	 *            {@link String} The name of the column
	 * @param value
	 *            {@link Object} The value of the column
	 * @return FlatXmlBuilder The generator.
	 */
	protected FlatXmlBuilder with(String columnName, Object value) {
		this.currentRow.putValues(columnName, value);
		return this;
	}

	/**
	 * Shorcut methode of with("id", this.newId());
	 * 
	 * @return {@link FlatXmlBuilder} The generator
	 */
	protected FlatXmlBuilder withNewId() {
		return this.with("id", this.newId());
	}

	/**
	 * Triggers the write into the xml file.
	 */
	public void add() {
		this.rows.add(this.currentRow);
	}

	/**
	 * Represents the data that will be add to the xml file.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected abstract void addData() throws FileNotFoundException, IOException;

	/**
	 * Writes data into an outputStream.
	 * 
	 * @param out
	 *            {@link OutputStream} Target OutputStream.
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
	 * Writes data into a file.
	 * 
	 * @param fw
	 *            The fileWriter used to write the data.
	 * @param debut
	 *            If true generates header for the xml file.
	 * @param fin
	 *            If true generates footer for the xml file.
	 * @throws IOException
	 */
	protected void write(FileWriter fw, boolean debut, boolean fin) throws IOException {
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
