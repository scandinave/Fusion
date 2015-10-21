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
import exception.RequeteException;
import exception.TatamiException;

/**
 * Implémentation d'un worker dédié à PostgreSQL. Les projets utilisant PostgreSQL comme base de données devrait étendre de cette classe.
 * @author Scandinave
 */
public abstract class AbstractPosgreSQLWorker extends AbstractWorker {

    /**
     * @throws TatamiException
     */
    protected AbstractPosgreSQLWorker() throws TatamiException {
        super();
    }

    /**
     * Logger de la classe.
     */
    private Log LOGGER = LogFactory.getLog(AbstractPosgreSQLWorker.class);

    /*
     * (non-Javadoc)
     * @see dbunit.worker.IBDDWorker#toogleContrainte(boolean)
     */
    @Override
    public void toogleContrainte(boolean toogle) throws TatamiException {
        LOGGER.info(toogle ? "Activation des contraintes" : "Désactivation des contraintes");
        try {
            String update = "UPDATE pg_trigger SET tgenabled = " + (toogle ? "'O'" : "'D'");
            Statement statement = databaseConnect.getConnection().createStatement();
            statement.executeUpdate(update);
            statement.close();
        } catch (Exception e) {
            throw new TatamiException(
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
    public void cleanSequence() throws TatamiException {
        TreeSet<SequenceBDD> sequences = getSequences();
        for (Iterator<SequenceBDD> iterator = sequences.iterator(); iterator.hasNext();) {
            SequenceBDD sequenceBDD = iterator.next();
            setSequenceZero(sequenceBDD);
        }
    }

    /**
     * Méthode getSequences.
     * @return
     * @throws TatamiException
     */
    private TreeSet<SequenceBDD> getSequences() throws TatamiException {
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
            throw new TatamiException(new RequeteException(e));
        }
        return setSequences;
    }

    /**
     * Méthode sequenceZero.
     * @param sequenceBDD
     * @throws TatamiException
     */
    private void setSequenceZero(SequenceBDD sequenceBDD) throws TatamiException {
        try {
            String sql = "SELECT setval('" + sequenceBDD.getNomSchema() + "." + sequenceBDD.getNomSequence() + "', 1)";
            PreparedStatement statement = databaseConnect.getConnection().prepareStatement(sql);
            statement.executeQuery();
            statement.close();
        } catch (SQLException e) {
            throw new TatamiException(new RequeteException(e));
        }
    }
}
