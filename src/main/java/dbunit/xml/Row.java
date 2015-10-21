package dbunit.xml;


/**
 * Représente les ligne d'une table à générer.
 * @author Scandinave
 * @since 12/05/2014
 * @version 1.0
 */
public class Row {

    /** Nom de la table en Bdd. */
    private String name;

    /** Ligne courante de la table. */
    private Columns row;

    /**
     * 
     */
    public Row() {
        super();
    }

    /**
     * Constructeur
     * @param name {@link String}
     */
    public Row(String name) {
        super();
        this.name = name;
        this.row = new Columns();
    }

    /**
     * @param name
     * @param row
     */
    public Row(String name, Columns row) {
        super();
        this.name = name;
        this.row = row;
    }

    /**
     * Getter de name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter de name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter de row.
     * @return the row
     */
    public Columns getRow() {
        return row;
    }

    /**
     * Setter de row.
     * @param row the row to set
     */
    public void setRow(Columns row) {
        this.row = row;
    }

    /**
     * Permet d'ajouter un couple nom et valeur pour une colonne dans la ligne courante.
     * @param columnName {@link String}
     * @param value {@link Object}
     */
    public void putColumnRow(String columnName, Object value) {
        this.row.put(columnName, value);
    }

    @Override
    public String toString() {
        return "<".concat(this.name).concat(this.row.toString())
            .concat("/>");
    }
}
