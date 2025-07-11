package DataBaseImplementationTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;

import DataBaseImplementation.Queries;
import DataBaseImplementation.RegistratoreSQL;
import DataBaseImplementation.ServizioHash;
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
    void tearDown() throws Exception {
    }

    @Test
    void registraNuovoVolontario_volontarioInseritoNelDB_restituisceTrue() throws Exception {
        DTObject volontario = new Tupla(null, Tupla.FORMATO_UTENTE);
        volontario.impostaValore("nuovoVolontarioTest", "Nickname");
        volontario.impostaValore("hashedPassword", "Password");
				volontario.impostaValore("Salt", "Salt");
        ServizioHash.cifraPassword(volontario);

        boolean risultato = registratore.registraNuovoVolontario(volontario);
				assertTrue(risultato);

        // Verifica presenza nel DB
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM dbingeswtest.volontario WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoVolontarioTest");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.volontario WHERE Nickname = ?")) {
            stmt.setString(1, "nuovoVolontarioTest");
            stmt.executeUpdate();
        }
    }

    @Test
    void registraNuovoLuogo_luogoInseritoNelDB_restituisceTrue() throws Exception {
        DTObject luogo = new Tupla(null, Tupla.FORMATO_LUOGO);
        luogo.impostaValore("Parco Test", "Nome");
        luogo.impostaValore("Luogo di prova", "Descrizione");
        luogo.impostaValore("Via del Test 123", "Indirizzo");

        boolean risultato = registratore.registraNuovoLuogo(luogo);
        assertTrue(risultato);

        // Verifica presenza nel DB
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM dbingeswtest.luogo WHERE Nome = ?")) {
            stmt.setString(1, "Parco Test");
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.luogo WHERE Nome = ?")) {
            stmt.setString(1, "Parco Test");
            stmt.executeUpdate();
        }
    }

		@Test
    void associaVolontario_restituisceTrue() throws Exception {

			  DTObject volontario = new Tupla(null, Tupla.FORMATO_UTENTE);
        volontario.impostaValore("Test", "Nickname");
        volontario.impostaValore("hashedPassword", "Password");
				volontario.impostaValore("Salt", "Salt");
        ServizioHash.cifraPassword(volontario);

        registratore.registraNuovoVolontario(volontario);

				String[] formato = {"Tipo di Visita","Volontario Nickname"};
        DTObject associazione = new Tupla("tabella", formato);
        associazione.impostaValore(1, "Tipo di Visita");
        associazione.impostaValore("Test", "Volontario Nickname");

				boolean risultato = registratore.associaVolontarioVisita(associazione);
				assertTrue(risultato);

        // Verifica presenza nel DB
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM dbingeswtest.`volontari disponibili` WHERE `Volontario Nickname` = ?")) {
            stmt.setString(1, "Test");
            try (ResultSet rs = stmt.executeQuery()) {
							boolean result = rs.next();
                assertTrue(result);
            }
        }
				//pulizia
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.`volontari disponibili` WHERE `Volontario Nickname` = ?")) {
            stmt.setString(1, "Test");
            stmt.executeUpdate();
        }
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.volontario WHERE Nickname = ?")) {
            stmt.setString(1, "Test");
            stmt.executeUpdate();
        }
    }

		@Test
		void registraNuovoTipoVisita_restituisceTrue() throws Exception {
				// Crea Tupla del tipo visita
				DTObject tipoVisita = new Tupla(null, Tupla.FORMATO_TIPO_VISITA);
				tipoVisita.impostaValore("Parco Test", "Punto di Incontro");
				tipoVisita.impostaValore("Visita Archeologica", "Titolo");
				tipoVisita.impostaValore("Una visita guidata tra i resti romani.", "Descrizione");
				tipoVisita.impostaValore(Date.valueOf("2025-07-15"), "Giorno inizio");
				tipoVisita.impostaValore(Date.valueOf("2025-08-15"), "Giorno fine");
				tipoVisita.impostaValore(Time.valueOf("10:00:00"), "Ora di inizio");
				tipoVisita.impostaValore(90, "Durata");
				tipoVisita.impostaValore(true, "Necessita Biglietto");
				tipoVisita.impostaValore(5, "Min Partecipanti");
				tipoVisita.impostaValore(25, "Max Partecipanti");
				tipoVisita.impostaValore("nuovoConfiguratoreTest", "Configuratore referente");
				tipoVisita.impostaValore(new String[]{"Lunedì", "Giovedì"}, "Giorni settimana");

				DTObject luogo = new Tupla(null, Tupla.FORMATO_LUOGO);
				luogo.impostaValore("Parco Test", "Nome");
				luogo.impostaValore("Luogo di prova", "Descrizione");
				luogo.impostaValore("Via del Test 123", "Indirizzo");

				DTObject configuratore = new Tupla(null, Tupla.FORMATO_UTENTE);
				configuratore.impostaValore("nuovoConfiguratoreTest", "Nickname");
				configuratore.impostaValore("hashedPassword", "Password");
				configuratore.impostaValore("Salt", "Salt");
				ServizioHash.cifraPassword(configuratore);

				
				//aggiunta di un finto configuratore e luogo
				try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_CONFIGURATORE.getQuery())) {
					stmt.setString(1, (String) configuratore.getValoreCampo("Nickname"));
					stmt.setString(2, (String) configuratore.getValoreCampo("Password"));  // Assumiamo che sia una password già cifrata valida
					stmt.setString(3, (String) configuratore.getValoreCampo("Salt"));
					stmt.executeUpdate();
				}
				registratore.registraNuovoLuogo(luogo);

				// Esegui metodo da testare
				boolean risultato = registratore.registraNuovoTipoVisita(tipoVisita);
				assertTrue(risultato);

				// Ottieni codice generato (dovresti usare lo stesso metodo interno o leggerlo da tipoVisita)
				int codiceVisita = (int) tipoVisita.getValoreCampo("Codice Tipo di Visita");

				// Verifica inserimento nella tabella tipo_visita
				try (PreparedStatement stmt = connection.prepareStatement(
								"SELECT * FROM dbingeswtest.`tipo di visita` WHERE `Codice Tipo di Visita` = ?")) {
						stmt.setInt(1, codiceVisita);
						try (ResultSet rs = stmt.executeQuery()) {
								assertTrue(rs.next());
						}
				}

				// Verifica inserimento dei giorni della settimana
				try (PreparedStatement stmt = connection.prepareStatement(
								"SELECT COUNT(*) as cnt FROM dbingeswtest.`giorni programmabili delle visite` WHERE `Tipo di Visita` = ?")) {
						stmt.setInt(1, codiceVisita);
						try (ResultSet rs = stmt.executeQuery()) {
								if (rs.next()) {
										assertEquals(2, rs.getInt("cnt"));
								} else {
										fail("Nessuna riga trovata nella tabella 'giorni settimana'");
								}
						}
				}

				// Pulizia dati inseriti
				try (PreparedStatement stmt = connection.prepareStatement(
								"DELETE FROM dbingeswtest.`giorni programmabili delle visite` WHERE `Tipo di Visita` = ?")) {
						stmt.setInt(1, codiceVisita);
						stmt.executeUpdate();
				}
				try (PreparedStatement stmt = connection.prepareStatement(
								"DELETE FROM dbingeswtest.`tipo di visita` WHERE `Codice Tipo di Visita` = ?")) {
						stmt.setInt(1, codiceVisita);
						stmt.executeUpdate();
				}
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM dbingeswtest.luogo WHERE Nome = ?")) {
					stmt.setString(1, "Parco Test");
					stmt.executeUpdate();
				}
				try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM `dbingeswtest`.`configuratore` WHERE Nickname = ?")) {
					stmt.setString(1, "nuovoConfiguratoreTest");
					stmt.executeUpdate();
				}
		}

}
