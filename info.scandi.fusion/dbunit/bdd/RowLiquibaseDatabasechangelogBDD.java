/**
 * 
 */
package dbunit.bdd;

import dbunit.xml.Columns;
import dbunit.xml.Row;

/**
 * @author Nonorc
 */
public class RowLiquibaseDatabasechangelogBDD implements IRowBDD, Comparable<RowLiquibaseDatabasechangelogBDD> {

    private static final String ELEMENT_COMPARAISON = "id";
    private TableBDD tableBDD;
    private Columns attributs;

    /**
     * Getter de tableBDD.
     * @return the tableBDD
     */
    public TableBDD getTableBDD() {
        return tableBDD;
    }

    /**
     * Setter de tableBDD.
     * @param tableBDD the tableBDD to set
     */
    public void setTableBDD(TableBDD tableBDD) {
        this.tableBDD = tableBDD;
    }

    /**
     * @param tableBDD
     * @param attributs
     */
    public RowLiquibaseDatabasechangelogBDD(TableBDD tableBDD, Columns attributs) {
        super();
        this.tableBDD = tableBDD;
        this.attributs = attributs;
    }

    /**
     * Getter de attributs.
     * @return the attributs
     */
    public Columns getAttributs() {
        return attributs;
    }

    /**
     * Setter de attributs.
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
