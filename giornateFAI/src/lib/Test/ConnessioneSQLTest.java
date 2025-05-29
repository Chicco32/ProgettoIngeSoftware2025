package lib.Test;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.jupiter.api.*;

import DataBaseImplementation.ConnessioneSQL;

public class ConnessioneSQLTest {

	@Test
	@DisplayName("Test se si connette correttamente al database")
	public void getConnectionTest() {
		Connection connection = ConnessioneSQL.getConnection();
		assertNotNull("Ottengo una connessione", connection);
	} 
}
