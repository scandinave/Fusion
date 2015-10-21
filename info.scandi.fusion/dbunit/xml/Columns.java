package dbunit.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * Ligne d'une table à générer.
 * @author Scandinave
 * @since 12/05/2014
 * @version 1.0
 */
public class Columns {

    /** Représente le nom des colonnes avec leurs valeurs. */
    private Map<String, Object> columns;

    public Columns() {
        super();
        this.columns = new HashMap<String, Object>();
    }

    /**
     * @see java.util.Map#put(Object, Object)
     */
    public void put(String cle, Object valeur) {
        this.columns.put(cle, valeur);
    }

    /**
     * Getter de columns.
     * @return the columns
     */
    public Map<String, Object> getColumns() {
        return columns;
    }

    /**
     * Setter de columns.
     * @param columns the columns to set
     */
    public void setColumns(Map<String, Object> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        String attributes = "";
        for (String column : this.columns.keySet()) {
            attributes = attributes.concat(" ").concat(column)
                .concat("=\"")
                .concat(this.columns.get(column) == null
                    ? ""
                    : this.columns.get(column).toString())
                .concat("\"");
        }

        return attributes;
    }

}
