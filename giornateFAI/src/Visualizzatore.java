import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        this.connection = ConnesioneSQL.getConnection();
    }

    /*
     * Richiede al db la lista delle visite in base allo stato
     * e le mostra all'utente
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

    /**
     * Chiede al DB e mostra la lista dei volontari. In particolare mostra le colonne Nickname, chiave del tipo di visita e titolo della stessa.
     * Inoltre ritorna la lista dei nicknmae gia salvati
     * @return
     */
    public ArrayList<String> visualizzaVolontari() {
        try {
            if (connection != null) {
                String query = "SELECT `Tipo di Visita`,`Volontario Nickname`,`Titolo` FROM dbingesw.`volontari disponibili` join dbingesw.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita`;";
                CliUtente.visualizzaRisulati(connection.createStatement().executeQuery(query), "Volontari");
                ArrayList<String> volontari = new ArrayList<>();
                
                //riporta il puntatore all'inzio prima di salvare
                ResultSet results = connection.createStatement().executeQuery(query);
                while (results.next()) {
                    volontari.add(results.getString("Volontario Nickname"));
                }
                return volontari;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

     */
    public void visualizzaTipiDiVisite() {
        try {
            if (connection != null) {
                String query = "SELECT * FROM dbingesw.`tipo di visita``";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaRisulati(results, "Luoghi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean DBLuoghiIsEmpty() throws SQLException {
        if (connection != null) {
            String query = "SELECT * FROM `dbingesw`.`luogo`";
            //next da true se trova una riga e sposta il cursore su tale riga
            return !connection.createStatement().executeQuery(query).next();
        }
        return false;
    }

    public boolean DBTipiVisiteIsEmpty() throws SQLException {
        if (connection != null) {
            String query = "SELECT * FROM dbingesw.`tipo di visita`";
            return !connection.createStatement().executeQuery(query).next();
        }
        return false;
    }

    
}
