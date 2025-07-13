package DataBaseImplementationTest;

import java.sql.Connection;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import DataBaseImplementation.ConnessioneSQL;

public class ConnessioneSQLTest {

	private static final String URL = "localhost:3306/dbingeswTest";
	private static final String USER = "root"; 
	private static final String PASSWORD = "root";

	@Test
	@DisplayName("Test se si connette correttamente al database")
	public void getConnectionDefaultTest() {
		Connection connection = ConnessioneSQL.getConnection();
		assertNotNull(connection, "Ottengo una connessione");
	} 

	@Test
	@DisplayName("Test se si connette correttamente al database")
	public void getConnectionTest() {
		Connection connection = ConnessioneSQL.getConnection(URL, USER, PASSWORD);
		assertNotNull(connection, "Ottengo una connessione");
	} 

	/**
	 * Metodo di mock per connettersi a un db copia di test
	 * @return Connection connessione al DB di test separato da quello sorgente
	 */
	public static Connection getTestDbConnection() {
		return ConnessioneSQL.getConnection(URL, USER, PASSWORD);
	}
}
