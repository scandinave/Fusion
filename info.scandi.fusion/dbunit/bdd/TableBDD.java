/**
 * 
 */
package dbunit.bdd;

import dbunit.xml.Row;

/**
 * @author Nonorc
 */
public class TableBDD implements IRowBDD, Comparable<TableBDD> {

    private String nomSchema;
    private String nomTable;
    private Integer ordreDeSuppression = 0;

    /**
     * @param nomSchema
     * @param nomTable
     */
    public TableBDD(String nomSchema, String nomTable) {
        super();
        this.nomSchema = nomSchema;
        this.nomTable = nomTable;
    }

    /**
     * Getter de nomSchema.
     * @return the nomSchema
     */
    public String getNomSchema() {
        return nomSchema;
    }

    /**
     * Setter de nomSchema.
     * @param nomSchema the nomSchema to set
     */
    public void setNomSchema(String nomSchema) {
        this.nomSchema = nomSchema;
    }

    /**
     * Getter de nomTable.
     * @return the nomTable
     */
    public String getNomTable() {
        return nomTable;
    }

    /**
     * Setter de nomTable.
     * @param nomTable the nomTable to set
     */
    public void setNomTable(String nomTable) {
        this.nomTable = nomTable;
    }

    /**
     * Getter de ordreDeSuppression.
     * @return the ordreDeSuppression
     */
    public Integer getOrdreDeSuppression() {
        return ordreDeSuppression;
    }

    /**
     * Setter de ordreDeSuppression.
     * @param ordreDeSuppression the ordreDeSuppression to set
     */
    public void setOrdreDeSuppression(Integer ordreDeSuppression) {
        this.ordreDeSuppression = ordreDeSuppression;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return nomSchema + "." + nomTable;
    }

    /*
     * (non-Javadoc)
     * @see utils.dbUnit.bdd.RowBDD#getRowXML()
     */
    @Override
    public Row getRowXML() {
        return new Row(nomSchema + Constantes.SEPARATEUR + nomTable);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TableBDD o) {
        if (ordreDeSuppression > 0 || o.getOrdreDeSuppression() > 0) {
            return ordreDeSuppression.compareTo(o.getOrdreDeSuppression());
        }
        return getRowXML().toString().compareTo(o.getRowXML().toString());
    }
}
