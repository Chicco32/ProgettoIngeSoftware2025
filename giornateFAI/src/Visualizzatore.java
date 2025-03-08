import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe per la gestione della estrazione di elementi all'interno del DB e per la loro visualizzazione.
 * Questa classe serve per interpellare il DB attraverso le query SQL e visualizzare i risultati.
 * Il risultato restitutito dai metodi è il contenuto del DB richiesto dalla query specifica del metodo.
 * Per inserire i dati necessita di connettersi al DB con un connettore.
 * Utilizza un file XML per la memorizzazione dei dati di default.
 * 
 * @see ConnesioneSQL
 */
public class Visualizzatore {

    private Connection connection;

    public Visualizzatore() {
        this.connection = ConnessioneSQL.getConnection();
    }

    public Visualizzatore(Connection conn){
	    this.connection=conn;
    }

    /*
     * Richiede al db la lista delle visite in base allo stato
     * e le mostra all'utente
     * TODO: implementare
     */
    public void visualizzaVisite(StatiVisite stato) {
        try {
            if (connection != null) {
                String query = "SELECT `Codice Archivio`,`Tipo di Visita`,`Volontaro Selezionato`,`Data programmata`,`Punto di Incontro`,`Titolo`,`Descrizione`,`Ora di inizio`,`Durata`,`Necessita Biglietto`,`Min Partecipanti`,`Max Partecipanti` FROM dbingesw.`archivio delle visite` join dbingesw.`tipo di visita` on `archivio delle visite`.`Tipo di visita` = `tipo di visita`. `Codice Tipo di Visita` WHERE `Stato Visita` = '" + StatiVisite.toString(stato) + "'";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaArchivioVisite(stato);
                CliUtente.visualizzaRisulati(results, "Visite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
     * Richiede al db la lista dei volotari
     * e le mostra all'utente
     */
    public void visualizzaVolontari() {
        try {
            if (connection != null) {
                String query = "SELECT Nickname FROM dbingesw.volontario";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaRisulati(results, "Volontari");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Richiede al db la lista dei luoghi visitabili
     * e le mostra all'utente
     */
    public void visualizzaLuoghiDaVisitare() {
        try {
            if (connection != null) {
                String query = "SELECT * FROM `dbingesw`.`luogo`";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaRisulati(results, "Luoghi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Richiede al db la lista delle tipologie di visite
     * e le mostra all'utente
     * TODO: implementare
     */
    public void visualizzaTipiDIVisite() {

    }

    public boolean DBLuoghiIsEmpty() throws SQLException {
        if (connection != null) {
            String query = "SELECT * FROM `dbingesw`.`luogo`";
            return connection.createStatement().executeQuery(query).next();
        }
        return false;
    }

    public boolean DBTipiVisiteIsEmpty() throws SQLException {
        if (connection != null) {
            String query = "SELECT * FROM dbingesw.`tipo di visita`";
            return connection.createStatement().executeQuery(query).next();
        }
        return false;
    }
}
