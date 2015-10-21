/**
 * 
 */
package dbunit.bdd;


/**
 * @author Nonorc
 */
public class SequenceBDD implements Comparable<SequenceBDD> {

    private String nomSchema;
    private String nomSequence;

    /**
     * @param nomSchema
     * @param nomSequence
     */
    public SequenceBDD(String nomSchema, String nomSequence) {
        super();
        this.nomSchema = nomSchema;
        this.nomSequence = nomSequence;
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
     * Getter de nomSequence.
     * @return the nomSequence
     */
    public String getNomSequence() {
        return nomSequence;
    }

    /**
     * Setter de nomSequence.
     * @param nomSequence the nomSequence to set
     */
    public void setNomSequence(String nomSequence) {
        this.nomSequence = nomSequence;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TableBDD [nomSchema=" + nomSchema + ", nomSequence=" + nomSequence + "]";
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SequenceBDD o) {
        return (getNomSchema() + getNomSequence()).compareTo(o.getNomSchema() + o.getNomSequence());
    }
}
