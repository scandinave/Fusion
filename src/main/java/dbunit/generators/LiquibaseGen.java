/**
 * 
 */
package dbunit.generators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dbunit.FlatXmlBuilder;
import dbunit.bdd.RowLiquibaseDatabasechangelogBDD;

/**
 * @author Nonorc
 */
public class LiquibaseGen extends FlatXmlBuilder implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 6626920958191370586L;
    private Set<RowLiquibaseDatabasechangelogBDD> setRowsLiquibaseDatabasechangelog = new HashSet<RowLiquibaseDatabasechangelogBDD>();

    /**
     * @param prefix
     * @param devMode
     * @param defaultStartId
     * @param abstractWorker
     */
    public LiquibaseGen(String outputPath, boolean devMode, long defaultStartId) {
        super(outputPath, devMode, defaultStartId);
    }

    /**
     * Getter de setRowsLiquibaseDatabasechangelog.
     * @return the setRowsLiquibaseDatabasechangelog
     */
    public Set<RowLiquibaseDatabasechangelogBDD> getSetRowsLiquibaseDatabasechangelog() {
        return setRowsLiquibaseDatabasechangelog;
    }

    /**
     * Setter de setRowsLiquibaseDatabasechangelog.
     * @param setRowsLiquibaseDatabasechangelog the setRowsLiquibaseDatabasechangelog to set
     */
    public void setSetRowsLiquibaseDatabasechangelog(Set<RowLiquibaseDatabasechangelogBDD> setRowsLiquibaseDatabasechangelog) {
        this.setRowsLiquibaseDatabasechangelog = setRowsLiquibaseDatabasechangelog;
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
        Iterator<RowLiquibaseDatabasechangelogBDD> it = setRowsLiquibaseDatabasechangelog.iterator();
        while (it.hasNext()) {
            RowLiquibaseDatabasechangelogBDD row = (RowLiquibaseDatabasechangelogBDD) it.next();
            this.add(row.getRowXML());
        }
    }
}
