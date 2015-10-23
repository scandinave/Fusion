package dbunit;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.database.IDatabaseConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import dbunit.Cleaner;
import dbunit.bdd.RowLiquibaseDatabasechangelogBDD;
import dbunit.bdd.TableBDD;
import dbunit.generators.LiquibaseGen;
import dbunit.generators.PurgeGen;
import dbunit.worker.AbstractPosgreSQLWorker;
import dbunit.xml.Columns;
import dbunit.xml.Row;
import exception.FusionException;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {
	
	private AbstractPosgreSQLWorker worker;
	private IDatabaseConnection connect;

	@Before
	public void setUp() throws Exception {
		worker = Mockito.mock(AbstractPosgreSQLWorker.class);
		File liquibase = new File("src/test/resources/liquibase.xml");
		File purge = new File("src/test/resources/purge.xml");
		worker.xmlFileLiquibase = liquibase.getAbsolutePath();
		worker.xmlFilePurge = purge.getAbsolutePath();
		connect = Mockito.mock(IDatabaseConnection.class);
	}
	
	public void contruction() {
		Cleaner cleaner = new Cleaner(connect, worker, true);
		//cleaner.execution();
	}
	
	@Test
	public void liquibaseGen() {
		System.out.println("toto");
		Set<RowLiquibaseDatabasechangelogBDD> rows = new HashSet<RowLiquibaseDatabasechangelogBDD>();
		Columns attributs = new Columns();
		attributs.put("id", "883");
		attributs.put("author", "bsta");
		attributs.put("liquibase", "3.3.2");
		attributs.put("description", "sqlFile");
		attributs.put("dateexecuted", "2015-10-25 15:19:45.23");
		
		for(int i=0; i<10; i++) {
			RowLiquibaseDatabasechangelogBDD rowLiquid = Mockito.mock(RowLiquibaseDatabasechangelogBDD.class);
			Mockito.when(rowLiquid.getRowXML()).thenReturn( new Row("liquibase.databaseChangelog", attributs));
			rows.add(rowLiquid);
		}
		
		LiquibaseGen liquid = new LiquibaseGen(worker.xmlFileLiquibase, false, 0);
		liquid.setSetRowsLiquibaseDatabasechangelog(rows);
		try {
			liquid.start();
		} catch (FusionException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void purgeGen() {
		Set<TableBDD> tables = new HashSet<TableBDD>();
		tables.add(new TableBDD("public", "user"));
		tables.add(new TableBDD("public", "profil"));
		tables.add(new TableBDD("public", "autorization"));
		tables.add(new TableBDD("public", "adress"));
		tables.add(new TableBDD("public", "organisation"));
		tables.add(new TableBDD("public", "common"));
		try {
			Mockito.when(worker.getAllTablesTypeTable()).thenReturn(tables);
		} catch (FusionException e1) {
			fail(e1.getMessage());
		}
		PurgeGen purgeGen = new PurgeGen(worker.xmlFilePurge, false, 0);
        try {
			purgeGen.setSetTables(worker.getAllTablesTypeTable());
			purgeGen.start();
		} catch (FusionException e) {
			fail(e.getMessage());
		}
	}

}
