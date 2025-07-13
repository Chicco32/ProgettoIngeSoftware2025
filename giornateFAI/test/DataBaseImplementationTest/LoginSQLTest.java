package DataBaseImplementationTest;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import DataBaseImplementation.LoginSQL;
import DataBaseImplementation.ServizioHash;
import DataBaseImplementation.Tupla;
import ServicesAPI.Configuratore;
import ServicesAPI.DTObject;
import ServicesAPI.Fruitore;
import ServicesAPI.Utente;
import DataBaseImplementation.Queries;
import ServicesAPI.Volontario;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginSQLTest {

    private LoginSQL login;
		private Connection connection;

    @BeforeEach
    void setUp() throws Exception {

        login = new LoginSQL();
        Queries.setDatabase("dbingeswtest");

				connection = ConnessioneSQLTest.getTestDbConnection();
				DTObject configuratore = new Tupla(null, Tupla.FORMATO_UTENTE);
				configuratore.impostaValore("hashedPassword", "Password");
				configuratore.impostaValore("testConfig", "Nickname");

				DTObject fruitore = new Tupla(null, Tupla.FORMATO_UTENTE);
				fruitore.impostaValore("hashedPassword", "Password");
				fruitore.impostaValore("testGuest", "Nickname");

				DTObject volontario = new Tupla(null, Tupla.FORMATO_UTENTE);
				volontario.impostaValore("hashedPassword", "Password");
				volontario.impostaValore("testVol", "Nickname");

				ServizioHash.cifraPassword(configuratore);
				ServizioHash.cifraPassword(volontario);
				ServizioHash.cifraPassword(fruitore);

        // Aggiunge test nel DB
        try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_CONFIGURATORE.getQuery())) {
            stmt.setString(1, (String) configuratore.getValoreCampo("Nickname"));
            stmt.setString(2, (String) configuratore.getValoreCampo("Password"));  // Assumiamo che sia una password già cifrata valida
            stmt.setString(3, (String) configuratore.getValoreCampo("Salt"));
            stmt.executeUpdate();
        }
				try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_VOLONTARIO.getQuery())) {
            stmt.setString(1, (String) volontario.getValoreCampo("Nickname"));
            stmt.setString(2, (String) volontario.getValoreCampo("Password"));  // Assumiamo che sia una password già cifrata valida
            stmt.setString(3, (String) volontario.getValoreCampo("Salt"));
            stmt.executeUpdate();
        }
				try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_FRUITORE.getQuery())) {
            stmt.setString(1, (String) fruitore.getValoreCampo("Nickname"));
            stmt.setString(2, (String) fruitore.getValoreCampo("Password"));  // Assumiamo che sia una password già cifrata valida
            stmt.setString(3, (String) fruitore.getValoreCampo("Salt"));
            stmt.executeUpdate();
        }
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `dbingeswtest`.`configuratore` WHERE Nickname = ?")) {
            stmt.setString(1, "testConfig");
            stmt.executeUpdate();
        }
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `dbingeswtest`.`volontario` WHERE Nickname = ?")) {
            stmt.setString(1, "testVol");
            stmt.executeUpdate();
        }
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `dbingeswtest`.`fruitori` WHERE Nickname = ?")) {
            stmt.setString(1, "testGuest");
            stmt.executeUpdate();
        }
    }

    @Test
    void loginUtente_configuratorePresenteNelDB_restituisceConfiguratore() throws Exception {
        Utente utente = login.loginUtente("testConfig", "hashedPassword");
        assertNotNull(utente);
        assertTrue(utente instanceof Configuratore);
    }

		@Test
    void loginUtente_volontarioPresenteNelDB_restituisceConfiguratore() throws Exception {
        Utente utente = login.loginUtente("testVol", "hashedPassword");
        assertNotNull(utente);
        assertTrue(utente instanceof Volontario);
    }

		@Test
    void loginUtente_fruitorePresenteNelDB_restituisceConfiguratore() throws Exception {
        Utente utente = login.loginUtente("testGuest", "hashedPassword");
        assertNotNull(utente);
        assertTrue(utente instanceof Fruitore);
    }

		@Test
    void loginUtente_configuratoreCredenzialiDefault_restituisceConfiguratore() throws Exception {
        Utente utente = login.loginUtente("admin", "admin");
        assertNotNull(utente);
        assertTrue(utente instanceof Configuratore);
    }

		@Test
    void loginUtente_configuratoreCredenzialiDefault_restituisceNull() throws Exception {
        Utente utente = login.loginUtente("admin", "NONAdmin");
        assertNull(utente);
    }

		@Test
    void loginUtente_fruitoreCredenzialiDefault_restituisceNull() throws Exception {
        Utente utente = login.loginUtente("new", "some");
        assertNotNull(utente);
        assertTrue(utente instanceof Fruitore);
    }

    @Test
    void loginUtente_utenteNonPresente_restituisceNull() throws Exception {
        Utente utente = login.loginUtente("nonEsisto", "ciao");
        assertNull(utente);
    }

    @Test
    void loginUtente_nomeUnivocoNonPresente_restituisceTrue() throws Exception {
      String nomeUnivoco = "nomeUnivoco";
      assertTrue(login.nomeUtenteUnivoco(nomeUnivoco));
    }

    @Test
    void loginUtente_nomeUnivocoPresenteConfiguratore_restituisceFalse() throws Exception {
      String nomeUnivoco = "testConfig";
      assertFalse(login.nomeUtenteUnivoco(nomeUnivoco));
    }

    @Test
    void loginUtente_nomeUnivocoPresenteVolontario_restituisceFalse() throws Exception {
      String nomeUnivoco = "testVol";
      assertFalse(login.nomeUtenteUnivoco(nomeUnivoco));
    }

    @Test
    void loginUtente_nomeUnivocoPresenteFruitore_restituisceFalse() throws Exception {
      String nomeUnivoco = "testGuest";
      assertFalse(login.nomeUtenteUnivoco(nomeUnivoco));
    }

    @Test
    void registraNuovoConfiguratore_configuratoreRegistratoNelDB_restituisceTrue() throws Exception {
        DTObject nuovoConfiguratore = new Tupla("tabella", Tupla.FORMATO_UTENTE);
        nuovoConfiguratore.impostaValore("nuovoConfig", "Nickname");
        nuovoConfiguratore.impostaValore("hashedPassword", "Password");
        ServizioHash.cifraPassword(nuovoConfiguratore);

        boolean risultato = login.registraNuovoConfiguratore(nuovoConfiguratore);
        assertTrue(risultato);

        // Verifica che sia effettivamente nel DB
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM dbingeswtest.configuratore WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoConfig");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }

        // Pulizia
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.configuratore WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoConfig");
            stmt.executeUpdate();
        }
    }

    @Test
    void registraNuovoFruitore_fruitoreRegistratoNelDB_restituisceTrue() throws Exception {
        DTObject nuovoFruitore = new Tupla(null, Tupla.FORMATO_UTENTE);
        nuovoFruitore.impostaValore("nuovoGuest", "Nickname");
        nuovoFruitore.impostaValore("hashedPassword", "Password");
        ServizioHash.cifraPassword(nuovoFruitore);

        boolean risultato = login.registraNuovoFruitore(nuovoFruitore);
        assertTrue(risultato);

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM dbingeswtest.fruitori WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoGuest");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }

        // Pulizia
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.fruitori WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoGuest");
            stmt.executeUpdate();
        }
    }

    @Test
    void cambioPassword_configuratorePasswordAggiornataNelDB_restituisceTrue() throws Exception {
        DTObject nuovoDato = new Tupla(null, Tupla.FORMATO_UTENTE);
        nuovoDato.impostaValore("testConfig", "Nickname"); // già presente in DB
        nuovoDato.impostaValore("hashedPassword", "Password");
        ServizioHash.cifraPassword(nuovoDato);

        boolean risultato = login.cambioPassword(nuovoDato, "Configuratore");
        assertTrue(risultato);

        // Recupera password e salt aggiornati
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT Password, Salt FROM dbingeswtest.configuratore WHERE Nickname = ?")) {
            stmt.setString(1, "testConfig");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                String nuovaPasswordCifrata = (String) nuovoDato.getValoreCampo("Password");
                String nuovaSalt = (String) nuovoDato.getValoreCampo("Salt");
                assertEquals(nuovaPasswordCifrata, rs.getString("Password"));
                assertEquals(nuovaSalt, rs.getString("Salt"));
            }
        }
    }

}


