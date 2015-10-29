/**
 * 
 */
package dbunit.worker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dbunit.bdd.SequenceBDD;
import exception.RequestException;
import exception.FusionException;

/**
 * Default implementation of the PostgreSQL DBMS worker.
 * @author Scandinave
 */
public abstract class AbstractPosgreSQLWorker extends AbstractWorker {

    /**
     * Default constructor.
     * @throws FusionException
     */
    protected AbstractPosgreSQLWorker() throws FusionException {
        super();
    }

    /**
     * Class logger.
     */
    private Log LOGGER = LogFactory.getLog(AbstractPosgreSQLWorker.class);

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#toogleContrainte(boolean)
     */
    @Override
    public void toogleContrainte(boolean toogle) throws FusionException {
        LOGGER.info(toogle ? "Activation des contraintes" : "Désactivation des contraintes");
        try {
            String update = "UPDATE pg_trigger SET tgenabled = " + (toogle ? "'O'" : "'D'");
            Statement statement = databaseConnect.getConnection().createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (Exception e) {
            throw new FusionException(
                String.format("Problème lors de %s des contraintes.",
                    toogle ? "l'activation" : "la désactivation"),
                e);
        }
    }

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IWorker#cleanSequence()
     */
    @Override
    public void cleanSequence() throws FusionException {
        TreeSet<SequenceBDD> sequences = getSequences();
        for (Iterator<SequenceBDD> iterator = sequences.iterator(); iterator.hasNext();) {
            SequenceBDD sequenceBDD = iterator.next();
            setSequenceZero(sequenceBDD);
        }
    }

    /**
     * Returns list of sequences in the database.
     * @return
     * @throws FusionException
     */
    private TreeSet<SequenceBDD> getSequences() throws FusionException {
        String nomSchema;
        String nomSequence;
        SequenceBDD sequence;
        TreeSet<SequenceBDD> setSequences = new TreeSet<SequenceBDD>();
        try {
            String sql = "SELECT n.nspname AS schemaname, c.relname as sequencename " +
                "FROM pg_class c " +
                "LEFT JOIN pg_namespace n ON n.oid = c.relnamespace " +
                "WHERE c.relkind = 'S'";
            PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    nomSchema = resultSet.getString("schemaname");
                    nomSequence = resultSet.getString("sequencename");
                    sequence = new SequenceBDD(nomSchema, nomSequence);
                    setSequences.add(sequence);
                }
            } else {
                LOGGER.debug("Pas de sequences à réinitialiser");
            }
            statement.close();
        } catch (SQLException e) {
            throw new FusionException(new RequestException(e));
        }
        return setSequences;
    }

    /**
     * Reset the sequence to 0.
     * @param sequenceBDD The sequence to reset.
     * @throws FusionException
     */
    private void setSequenceZero(SequenceBDD sequenceBDD) throws FusionException {
        try {
            String sql = "SELECT setval('" + sequenceBDD.getNomSchema() + "." + sequenceBDD.getNomSequence() + "', 1)";
            PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);
            statement.executeQuery();
            statement.close();
        } catch (SQLException e) {
            throw new FusionException(new RequestException(e));
        }
    }
}
