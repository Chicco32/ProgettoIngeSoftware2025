package DataBaseImplementationTest;

import java.sql.Connection;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import DataBaseImplementation.ConnessioneSQL;

public class ConnessioneSQLTest {

	@Test
	@DisplayName("Test se si connette correttamente al database")
	public void getConnectionTest() {
		Connection connection = ConnessioneSQL.getConnection();
		assertNotNull(connection, "Ottengo una connessione");
	} 
}
