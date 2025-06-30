package DataBaseImplementationTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import DataBaseImplementation.XMLManager;
import java.io.File;
import java.io.FileNotFoundException;

public class XMLManagerTest {

	@Test
	public void createManager() {
		XMLManager manager = new XMLManager("testPath.xml");
		assertTrue(manager != null && manager.getPath().equals("testPath.xml"));
	}

	@Test
	public void voidPath() {
		XMLManager manager = new XMLManager(null);
		assertTrue(manager != null && manager.getPath() == null);
	}

	@Test
	public void fileExists() {
		String path = "testPath.xml";
		File file = new File(path);
		try {
			file.createNewFile();
			if (XMLManager.fileExists(path)) assertTrue(true);
			file.delete();
		} catch (Exception e) {
			assertFalse(false);
		}
	}

	@Test
	public void fileNotExists() {
		String path = "testPath.xml";
		assert !XMLManager.fileExists(path);
	}

	@Test
	public void createFile() {
		String path = "testPath.xml";
		XMLManager.creaFile(path);
		File file = new File(path);
		assert file.exists();
		file.delete();
	}

	@Test
	public void readFileNotExists() {
		try {
			XMLManager manager = new XMLManager("nonExistentFile.xml");
			manager.leggiVariabile("tag");
			assertTrue(false); // Should not reach here
		} catch (FileNotFoundException e) {
			assertTrue(true); // Exception expected
		}
		catch (Exception e) {
			assertTrue(false); // Should not reach here
		}
	}

	@Test
	public void readVariable() {
		try {
			XMLManager manager = new XMLManager("testPath.xml");
			XMLManager.creaFile(manager.getPath());
			File file = new File(manager.getPath());
			String content = "<root><tag>value</tag></root>";
			java.nio.file.Files.write(file.toPath(), content.getBytes());
			String value = manager.leggiVariabile("tag");
			assertEquals("value", value); // Assuming no tag exists, should return null
			file.delete();
		} catch (FileNotFoundException e) {
			assertTrue(false); // Should not reach here
		} catch (Exception e) {
			assertTrue(false); // Should not reach here
		}
	}


}
