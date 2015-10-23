/**
 * 
 */
package dbunit.bdd;

import dbunit.xml.Columns;
import dbunit.xml.Row;

/**
 * Corresponds to an entry in the Liquibase table.
 * @author Nonorc
 */
public class RowLiquibaseDatabasechangelogBDD implements IRowBDD, Comparable<RowLiquibaseDatabasechangelogBDD> {

    private static final String ELEMENT_COMPARAISON = "id";
    /**
     * Liquibase table name
     */
    private TableBDD tableBDD;
    /**
     * List of Liquibase fields and values.
     */
    private Columns attributs;

    /**
     * Returns the Liquibase table name.
     * @return the tableBDD
     */
    public TableBDD getTableBDD() {
        return tableBDD;
    }

    /**
     * Changes the Liquibase table name.
     * @param tableBDD New table name.
     */
    public void setTableBDD(TableBDD tableBDD) {
        this.tableBDD = tableBDD;
    }

    /**
     * @param tableBDD The name of Liquibase table
     * @param attributs The list of of Liquibase fields and values.
     */
    public RowLiquibaseDatabasechangelogBDD(TableBDD tableBDD, Columns attributs) {
        super();
        this.tableBDD = tableBDD;
        this.attributs = attributs;
    }

    /**
     * Returns the Liquibase fields and values for one row in dataTable.
     * @return the attributs
     */
    public Columns getAttributs() {
        return attributs;
    }

    /**
     * Changes the Liquibase list fields and values for one row in datatable.
     * @param attributs the attributs to set
     */
    public void setAttributs(Columns attributs) {
        this.attributs = attributs;
    }

    /*
     * (non-Javadoc)
     * @see utils.dbUnit.bdd.IRowBDD#getRowXML()
     */
    @Override
    public Row getRowXML() {
        return new Row(tableBDD.getRowXML().getName(), attributs);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(RowLiquibaseDatabasechangelogBDD o) {
        return attributs.getColumns().get(ELEMENT_COMPARAISON).toString().compareTo(
            o.getAttributs().getColumns().get(ELEMENT_COMPARAISON).toString());
    }
}
