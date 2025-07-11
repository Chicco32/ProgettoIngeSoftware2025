package DataBaseImplementationTest;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import DataBaseImplementation.Queries;
import DataBaseImplementation.RegistratoreSQL;
import DataBaseImplementation.Tupla;
import ServicesAPI.DTObject;

public class RegistratoreSQLTest {

		private RegistratoreSQL registratore;
		private Connection connection;

	@BeforeEach
    void setUp() throws Exception {
        registratore = new RegistratoreSQL();
        Queries.setDatabase("dbingeswtest");
				connection = ConnessioneSQLTest.getTestDbConnection();
    }

    @AfterEach
    void cleanUp() throws Exception {
    }

}
