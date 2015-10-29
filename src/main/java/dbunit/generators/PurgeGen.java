/**
 * 
 */
package dbunit.generators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dbunit.FlatXmlBuilder;
import dbunit.bdd.TableBDD;
import dbunit.worker.AbstractWorker;

/**
 * Generates purge.xml file used purge the database. Some table can be skip by redefining the worker save method .
 * @see AbstractWorker#save()
 * @see AbstractWorker#getTablesParTypeWithExclusions(String[], String[], String[])
 * @author Nonorc
 */
public class PurgeGen extends FlatXmlBuilder {

	/**
	 * List of tables that need to be purged. 
	 */
    private Set<TableBDD> setTables = new HashSet<TableBDD>();

    /**
     * Default constructor.
     * @param outputPath Path to save the generated file.
     * @param devMode True if the generated xml must be display on the console instead of file. 
     * @param defaultStartId The default start id used to generate rows id.
     */
    public PurgeGen(String outputPath, boolean devMode, long defaultStartId) {
        super(outputPath, devMode, defaultStartId);
    }

    /**
     * Returns the list of tables that need to be purged.
     * @return the setTables the list of tables that need to be purged.
     */
    public Set<TableBDD> getSetTables() {
        return setTables;
    }

    /**
     * Changes the list of tables that need to be purged.
     * @param setTables The new list of tables that need to be purged.
     */
    public void setSetTables(Set<TableBDD> setTables) {
        this.setTables = setTables;
    }

    /*
     * (non-Javadoc)
     * @see utils.dbUnit.XMLGenerator.FlatXmlBuilder#addData()
     */
    @Override
    public void addData() throws FileNotFoundException, IOException {
    	Iterator<TableBDD> it = setTables.iterator();
        while (it.hasNext()) {
            TableBDD tableBDD = (TableBDD) it.next();
            this.add(tableBDD.getRowXML());
        }
    }
}
