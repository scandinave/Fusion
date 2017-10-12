package info.scandi.fusion.database.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents values of one line in table.
 * @author Scandinave
 */
public class Row {

    /** Table name */
    private String name;

    /** List of values of the row. */
    private Map<String, Object> values;

    /**
     * Instantiates row with empty values list.
     * @param name {@link String}
     */
    public Row(String name) {
        super();
        this.name = name;
        this.values = new HashMap<String, Object>();
    }

    /**
     * Instantiates Row with values.
     * @param name Table name for this row
     * @param values List of value for this row.
     */
    public Row(String name, Map<String, Object> values) {
        super();
        this.name = name;
        this.values = values;
    }

    /**
     * Returns the name of the table for this row.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the table for this row.
     * @param name the new name of the table.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of values of the row.
     * @return
     */
    public Map<String, Object> getValues() {
		return values;
	}

    /**
     * Changes the list of values of the row.
     * @param values
     */
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}

	/**
     * Add value to the list of value of the row.
     * @param columnName {@link String} The name of the column in datatable to add.
     * @param value {@link Object} the value of the column.
     */
    public void putValues(String columnName, Object value) {
        this.values.put(columnName, value);
    }

    @Override
    public String toString() {
        return "<".concat(this.name).concat(this.values.toString())
            .concat("/>");
    }
}
