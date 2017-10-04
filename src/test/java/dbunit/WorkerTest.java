package dbunit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import info.scandi.fusion.dbunit.worker.AbstractWorker;
import info.scandi.fusion.exception.FusionException;

@RunWith(MockitoJUnitRunner.class)
public class WorkerTest {

	@Mock
	private AbstractWorker worker;

	@Before
	public void setUp() throws Exception {
		// Mockito.when(worker.getRootPath()).thenReturn("rootPath/");
	}

	@Test
	public void testCommunDir() throws FusionException {
		Mockito.when(worker.getCommunDir()).thenCallRealMethod();
		assertEquals("rootPath/".concat("flatXmlDataSet").concat("/commun"), worker.getCommunDir());
	}

	@Test
	public void testDistinctDir() throws FusionException {
		Mockito.when(worker.getDistinctDir()).thenCallRealMethod();
		assertEquals("rootPath/".concat("flatXmlDataSet").concat("/distinct"), worker.getDistinctDir());
	}

	@Test
	public void testFeatureInit() {
		Mockito.when(worker.getFeature_path()).thenReturn("/src/test/resources/features/");
	}
}
