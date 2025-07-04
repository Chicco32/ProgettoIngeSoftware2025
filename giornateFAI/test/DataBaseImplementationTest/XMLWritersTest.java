package DataBaseImplementationTest;

import org.junit.jupiter.api.*;

import DataBaseImplementation.XMLConfiguratore;
import ServicesAPI.Eccezioni.ConfigFilesException;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.io.FileNotFoundException;
import java.io.IOException;


public class XMLWritersTest {

	@Test
	void leggiAreaCompetenza() {
		Path path = null;
		try {
			// Crea file temporaneo
			path = Files.createTempFile("areaCompetenza", ".xml");

			// Usa il path assoluto
			String fileName = path.toAbsolutePath().toString();

			XMLConfiguratore configuratore = new XMLConfiguratore(fileName);
			configuratore.scriviRegistratoreDefault("prova", 3);
			assertEquals("prova", configuratore.leggiAreaCompetenza());

		} catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		} finally {
			// Prova a cancellare il file dopo il test
			try {
				if (path != null && Files.exists(path)) {
					Files.delete(path);
				}
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il file di test: " + e.getMessage());
			}
		}
	}


	@Test
	void leggiMaxPartecipanti() {
		Path path = null;
		try {
			// Crea file temporaneo
			path = Files.createTempFile("maxPartecipanti", ".xml");

			// Usa il path assoluto
			String fileName = path.toAbsolutePath().toString();

			XMLConfiguratore configuratore = new XMLConfiguratore(fileName);
			configuratore.scriviRegistratoreDefault("prova", 3);
			assertEquals(3, configuratore.leggiNumeroPartecipanti());

		} catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		} finally {
			// Prova a cancellare il file dopo il test
			try {
				if (path != null && Files.exists(path)) {
					Files.delete(path);
				}
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il file di test: " + e.getMessage());
			}
		}
	}

	@Test
	void AreaCompetenzaNotFound() {
		String path = "areaCompetenza.xml";
		try {
			XMLConfiguratore configuratore = new XMLConfiguratore(path);
			String value = configuratore.leggiAreaCompetenza();

			fail("Ha letto: " + value);
		}
		catch (ConfigFilesException e) {
			assertTrue(true);
		} 
		catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		}
	}

	@Test
	void maxPartecipantiNotFound() {
		String path = "maxPartecipanti.xml";
		try {
			XMLConfiguratore configuratore = new XMLConfiguratore(path);
			int value = configuratore.leggiNumeroPartecipanti();

			fail("Ha letto: " + value);
		}
		catch (ConfigFilesException e) {
			assertTrue(true);
		} 
		catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		}
	}

	@Test
	void testScriviRegistratoreDefault() {
		Path path = null;
		try {
			// Crea file temporaneo
			path = Files.createTempFile("test", ".xml");

			// Usa il path assoluto
			String fileName = path.toAbsolutePath().toString();
			String expected = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
												"<registratore>\r\n" +
												"<maxPartecipanti>3</maxPartecipanti>\r\n" +
												"<areaCompetenza>prova</areaCompetenza>\r\n" +
												"</registratore>";

			XMLConfiguratore configuratore = new XMLConfiguratore(fileName);
			configuratore.scriviRegistratoreDefault("prova", 3);
			
			String value = Files.readString(path);
			
			assertEquals(expected, value);

		} catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		} finally {
			// Prova a cancellare il file dopo il test
			try {
				if (path != null && Files.exists(path)) {
					Files.delete(path);
				}
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il file di test: " + e.getMessage());
			}
		}
	}

	@Test
	void pathErratoRegistratoreDefault() {
		String path = "test/foo.xml";
		try {
			XMLConfiguratore configuratore = new XMLConfiguratore(path);
      configuratore.scriviRegistratoreDefault("prova", 3);
			assertFalse(true);
		}
		catch (FileNotFoundException e) {
			assertTrue(true);
		} 
		catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		}
	}

	@Test
	void modificaAreaCompetenza() {
		Path path = null;
		try {
			// Crea file temporaneo
			path = Files.createTempFile("areaCompetenza", ".xml");

			// Usa il path assoluto
			String fileName = path.toAbsolutePath().toString();

			XMLConfiguratore configuratore = new XMLConfiguratore(fileName);
			configuratore.scriviRegistratoreDefault("prova", 3);
			configuratore.modificaAreaCompetenza("ValoreModificato");
			assertEquals("ValoreModificato", configuratore.leggiAreaCompetenza());

		} catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		} finally {
			// Prova a cancellare il file dopo il test
			try {
				if (path != null && Files.exists(path)) {
					Files.delete(path);
				}
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il file di test: " + e.getMessage());
			}
		}
	}

	@Test
	void modificaMaxPartecipanti() {
		Path path = null;
		try {
			// Crea file temporaneo
			path = Files.createTempFile("maxPartecipanti", ".xml");

			// Usa il path assoluto
			String fileName = path.toAbsolutePath().toString();

			XMLConfiguratore configuratore = new XMLConfiguratore(fileName);
			configuratore.scriviRegistratoreDefault("prova", 3);
			configuratore.modificaMaxPartecipanti(5);
			assertEquals(5, configuratore.leggiNumeroPartecipanti());

		} catch (Exception e) {
			fail("Eccezione durante il test: " + e.getMessage());
		} finally {
			// Prova a cancellare il file dopo il test
			try {
				if (path != null && Files.exists(path)) {
					Files.delete(path);
				}
			} catch (IOException e) {
				System.err.println("Impossibile eliminare il file di test: " + e.getMessage());
			}
		}
	}

}
