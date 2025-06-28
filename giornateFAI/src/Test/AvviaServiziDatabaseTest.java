package Test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import DataBaseImplementation.AvviaServiziDatabase;

public class AvviaServiziDatabaseTest {

	@Test
	@DisplayName("Test se i servizi vengono avviati correttamente")

	public void creaRegistratore() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaRegistratore());
	}

	@Test
	public void creaVisualizzatoreVolontario() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaVisualizzatoreVolontario());
	}

	@Test
	public void inizializzaDateDisponibili() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.inizializzaDateDisponibili());
	}
	
	@Test
	public void inizializzaDatePrecluse() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.inizializzaDatePrecluse());
	}

	@Test
	public void creaRegistratoreIscrizioni() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaRegistratoreIscrizioni());
	}

	@Test
	public void creaVisualizzatoreFruitore() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaVisualizzatoreFruitore());
	}

	@Test
	public void creaVisualizzatoreConfiguratore() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaVisualizzatoreConfiguratore());
	}

	@Test
	public void creaGestoreConfiguratore() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaGestoreConfiguratore());
	}

	@Test
	public void creaGestoreFruitore() {
		AvviaServiziDatabase factory = AvviaServiziDatabase.getFactory();
		assertNotNull(factory.creaGestoreFruitore());
	}
	
}
