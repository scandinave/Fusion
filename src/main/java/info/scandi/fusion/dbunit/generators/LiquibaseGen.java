package info.scandi.fusion.dbunit.generators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import info.scandi.fusion.dbunit.FlatXmlBuilder;
import info.scandi.fusion.dbunit.bdd.RowLiquibaseDatabasechangelogBDD;

/**
 * Generates liquibase.xml file used to save the liquibase datachangelog before the purge. 
 * This data are restore after purge.
 * @author Nonorc
 */
public class LiquibaseGen extends FlatXmlBuilder implements Serializable {

    /**
     * serialVersionUID long.
     */
    private static final long serialVersionUID = 6626920958191370586L;
    private Set<RowLiquibaseDatabasechangelogBDD> setRowsLiquibaseDatabasechangelog = new HashSet<RowLiquibaseDatabasechangelogBDD>();

    /**
     * Default constructor.
     * @param outputPath Path to save the generated file.
     * @param devMode True if the generated xml must be display on the console instead of file. 
     * @param defaultStartId The default start id used to generate rows id.
     */
    public LiquibaseGen(String outputPath, boolean devMode, long defaultStartId) {
        super(outputPath, devMode, defaultStartId);
    }

    /**
     * Returns the list of row in the Liquibase table.
     * @return the setRowsLiquibaseDatabasechangelog
     */
    public Set<RowLiquibaseDatabasechangelogBDD> getSetRowsLiquibaseDatabasechangelog() {
        return setRowsLiquibaseDatabasechangelog;
    }

    /**
     * Changes the list of row in the Liquibase table.
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
    	Iterator<RowLiquibaseDatabasechangelogBDD> it = setRowsLiquibaseDatabasechangelog.iterator();
        while (it.hasNext()) {
            RowLiquibaseDatabasechangelogBDD row = (RowLiquibaseDatabasechangelogBDD) it.next();
            this.add(row.getRowXML());
        }
    }
}
