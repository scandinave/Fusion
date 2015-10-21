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

/**
 * @author Nonorc
 */
public class PurgeGen extends FlatXmlBuilder {

    private Set<TableBDD> setTables = new HashSet<TableBDD>();

    /**
     * @param prefix
     * @param devMode
     * @param defaultStartId
     * @param abstractWorker
     */
    public PurgeGen(String outputPath, boolean devMode, long defaultStartId) {
        super(outputPath, devMode, defaultStartId);
    }

    /**
     * Getter de setTables.
     * @return the setTables
     */
    public Set<TableBDD> getSetTables() {
        return setTables;
    }

    /**
     * Setter de setTables.
     * @param setTables the setTables to set
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
        ajouterLignes();
    }

    private void ajouterLignes() {
        Iterator<TableBDD> it = setTables.iterator();
        while (it.hasNext()) {
            TableBDD tableBDD = (TableBDD) it.next();
            this.newRow(tableBDD.getRowXML()).add();
        }
    }
}
