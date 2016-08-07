/**
 * 
 */
package dbunit.bdd;

import dbunit.xml.Row;

/**
 * @author Nonorc
 */
public class TableBDD implements IRowBDD, Comparable<TableBDD> {

	private String schemaName;
	private String tableName;
	private Integer deleteOrder = 0;
	private String primaryKey;

	/**
	 * @param schemaName
	 * @param tableName
	 */
	public TableBDD(String schemaName, String tableName) {
		super();
		this.schemaName = schemaName;
		this.tableName = tableName;
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
	 * Getter de nomTable.
	 * 
	 * @return the nomTable
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Setter de nomTable.
	 * 
	 * @param nomTable
	 *            the nomTable to set
	 */
	public void setTableName(String nomTable) {
		this.tableName = nomTable;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Getter de ordreDeSuppression.
	 * 
	 * @return the ordreDeSuppression
	 */
	public Integer getDeleteOrder() {
		return deleteOrder;
	}

	/**
	 * Setter de ordreDeSuppression.
	 * 
	 * @param deleteOrder
	 *            the ordreDeSuppression to set
	 */
	public void setDeleteOrder(Integer deleteOrder) {
		this.deleteOrder = deleteOrder;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.dbUnit.bdd.RowBDD#getRowXML()
	 */
	@Override
	public Row getRowXML() {
		return new Row(schemaName + Constants.SEPARATOR + tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TableBDD o) {
		if (deleteOrder > 0 || o.getDeleteOrder() > 0) {
			return deleteOrder.compareTo(o.getDeleteOrder());
		}
		return getRowXML().toString().compareTo(o.getRowXML().toString());
	}
}
