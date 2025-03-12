package giornateFAI;

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
        this.connection = ConnessioneSQL.getConnection();
    }

    public Visualizzatore(Connection conn){
	    this.connection=conn;
    }

    /*
     * Richiede al db la lista delle visite in base allo stato
     * e le mostra all'utente
     */
    public void visualizzaVisite(StatiVisite stato) {
        try {
            if (connection != null) {
                String query = "SELECT `Codice Archivio`,`Tipo di Visita`,`Volontaro Selezionato`,`Data programmata`,`Punto di Incontro`,`Titolo` FROM dbingesw.`archivio delle visite` join dbingesw.`tipo di visita` on `archivio delle visite`.`Tipo di visita` = `tipo di visita`. `Codice Tipo di Visita` WHERE `Stato Visita` = '" + StatiVisite.toString(stato) + "'";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaArchivioVisite(stato);
                CliUtente.visualizzaRisultati(results, "Visite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Chiede al DB e mostra la lista dei volontari. In particolare mostra le colonne Nickname, chiave del tipo di visita e titolo della stessa.
     * Inoltre ritorna la lista dei nickname gia salvati. Attenzione perà che nella lista dei volontari osno presente tutti i volontari registrati, anche quelli a cui non è stata ancora abbianta nessuna visita
     * @return
     */
    public ArrayList<String> visualizzaVolontari() {
        try {
            if (connection != null) {
                String query = "SELECT `Tipo di Visita`,`Volontario Nickname`,`Titolo` FROM dbingesw.`volontari disponibili` join dbingesw.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita`;";
                CliUtente.visualizzaRisultati(connection.createStatement().executeQuery(query), "Volontari");
                ArrayList<String> volontari = new ArrayList<>();
                
                //riporta la lista completa dei volontari
                String queryVolontari = "SELECT * FROM dbingesw.volontario;";
                ResultSet results = connection.createStatement().executeQuery(queryVolontari);
                while (results.next()) {
                    volontari.add(results.getString("Nickname"));
                }
                return volontari;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * La funzione legge la lista di luoghi dalla tabella luogo e li mostra all'utente in forma tabelllare.
     * Inoltre ritorna un arraylist con le chiavi associate a ogni luogo, ovvvero il nome dello stesso
     * @return la lista di nomi dei luoghi visualizzati
     */
    public ArrayList<String> visualizzaLuoghiDaVisitare() {
        try {
            if (connection != null) {
                String query = "SELECT * FROM `dbingesw`.`luogo`";
                CliUtente.visualizzaRisultati(connection.createStatement().executeQuery(query), "Luoghi");
                
                ArrayList<String> luoghi = new ArrayList<>();
                ResultSet results = connection.createStatement().executeQuery(query);
                while (results.next()) {
                    luoghi.add(results.getString("Nome"));
                }
                return luoghi;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Richiede al db la lista delle tipologie di visite
     * e le mostra all'utente

     */
    public void visualizzaTipiDiVisite() {
        try {
            if (connection != null) {
                String query = "SELECT * FROM dbingesw.`tipo di visita`";
                ResultSet results = connection.createStatement().executeQuery(query);
                CliUtente.visualizzaRisultati(results, "tipo di Visita");
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
