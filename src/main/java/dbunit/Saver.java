/**
 * 
 */
package dbunit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import dbunit.bdd.TableBDD;
import dbunit.worker.AbstractWorker;
import exception.FusionException;
import utils.FileUtil;

/**
 * Saves the data of the database in xml files. One file by datatable.
 * Tables can be exclude from the generation.
 * @author Scandinave
 */
public class Saver implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 142577290372523301L;

    /**
     * Class logger.
     */
    private Log LOGGER = LogFactory.getLog(Saver.class);

    /**
     * Database connection.
     */
    private IDatabaseConnection databaseConnect;
    
    /**
     * Database worker.
     */
    private AbstractWorker abstractWorker;

    /**
     * List of tables to save.
     */
    private Set<TableBDD> tables;

    /**
     * Default constructor.
     * @param databaseConnect Database connection
     * @param abstractWorker {@link AbstractWorker}  Database worker.
     */
    public Saver(IDatabaseConnection databaseConnect, AbstractWorker abstractWorker) {
        this.databaseConnect = databaseConnect;
        this.abstractWorker = abstractWorker;
    }

    /**
     * Starts the save operation.
     * @throws FusionException
     */
    public void start() throws FusionException {
        if (tables == null) {
            tables = abstractWorker.getAllTablesTypeTable();
        }
        destruction();
        construction();
    }

    /**
     * Deletes all xml file that was used for the last save.
     */
    private void destruction() {
        FileUtil.cleanDirectories(abstractWorker.xmlDirectorySave);
    }

    /**
     * Writes the data into xml files.
     * @throws FusionException
     */
    private void construction() throws FusionException {
        LOGGER.info("Début de la sauvegarde de la base de données");
        try {
            Set<TableBDD> setTables = getTables();
            for (Iterator<TableBDD> iterator = setTables.iterator(); iterator.hasNext();) {
                TableBDD tableBDD = iterator.next();
                File file = new File(abstractWorker.xmlDirectorySave + "/" + tableBDD.getNomSchema() + "." + tableBDD.getNomTable() + ".xml");
                File parent = file.getParentFile();
                if (!file.getParentFile().exists()) {
                    parent.mkdirs();
                }
                QueryDataSet dataSetParTable = new QueryDataSet(databaseConnect);
                dataSetParTable.addTable(tableBDD.getNomSchema() + "." + tableBDD.getNomTable());
                FileOutputStream outputStream;
                outputStream = new FileOutputStream(file);
                FlatXmlDataSet.write(dataSetParTable, outputStream);
                LOGGER.debug(file.getCanonicalPath() + " sauvegardé");
            }
        } catch (AmbiguousTableNameException e) {
            throw new FusionException(e);
        } catch (FileNotFoundException e1) {
            throw new FusionException(e1);
        } catch (DataSetException | IOException e2) {
            throw new FusionException(e2);
        }
        LOGGER.info("Sauvegarde effectuée avec succès.");
    }

    /**
     * Returns the list of tables to save.
     * @return the tables
     */
    public Set<TableBDD> getTables() {
        return tables;
    }

    /**
     * Changes the list of tables to save.
     * @param tables the tables to set
     */
    public void setTables(Set<TableBDD> tables) {
        this.tables = tables;
    }
}
