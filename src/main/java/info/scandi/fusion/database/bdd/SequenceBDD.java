/**
 * 
 */
package info.scandi.fusion.database.bdd;

/**
 * @author Nonorc
 */
public class SequenceBDD implements Comparable<SequenceBDD> {

	private String schemaName;
	private TableBDD tableBDD;
	private String sequenceName;

	/**
	 * @param schemaName
	 * @param nomSequence
	 */
	public SequenceBDD(TableBDD tableBDD, String nomSequence) {
		super();
		this.tableBDD = tableBDD;
		this.sequenceName = nomSequence;
		this.schemaName = tableBDD.getSchemaName() + "." + nomSequence;

	}

	/**
	 * Getter de nomSchema.
	 * 
	 * @return the nomSchema
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * Setter de nomSchema.
	 * 
	 * @param nomSchema
	 *            the nomSchema to set
	 */
	public void setSchemaName(String nomSchema) {
		this.schemaName = nomSchema;
	}

	/**
	 * Getter de nomSequence.
	 * 
	 * @return the nomSequence
	 */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * Setter de nomSequence.
	 * 
	 * @param nomSequence
	 *            the nomSequence to set
	 */
	public void setSequenceName(String nomSequence) {
		this.sequenceName = nomSequence;
	}

	/**
	 * Return the TableBDD object of the sequence
	 * 
	 * @return the TableBDD object of the sequence
	 */
	public TableBDD getTableBDD() {
		return tableBDD;
	}

	/**
	 * Update the TableBDD object of the sequence
	 * 
	 * @param tableBDD
	 *            the TableBDD object of the sequence to update.
	 */
	public void setTableBDD(TableBDD tableBDD) {
		this.tableBDD = tableBDD;
	}

	public String getSchemaNamePointSequenceName() {
		return tableBDD.getSchemaName() + "." + sequenceName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SequenceBDD o) {
		return (tableBDD.getSchemaName() + getSequenceName())
				.compareTo(o.getTableBDD().getSchemaName() + o.getSequenceName());
	}
}
