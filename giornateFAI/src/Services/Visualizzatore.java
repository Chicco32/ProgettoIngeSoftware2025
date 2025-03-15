package Services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import ConfigurationFiles.ConnessioneSQL;
import ConfigurationFiles.Queries;


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

    /**
     * Ritorna i risultati dell'esecuzone di una certa query sul database
     * @param query la query da far eseguire, il metodo è specifico per le query solo di visualizzazione non di modifica, per quelle usare il registratore
     * @return Un oggetto {@code ResultSet} con le tuple risultate dalla interrogazione
     * @throws SQLException se ci sono errori nella esecuzione della query
     */
    public ResultSet visualizzaTabella(String query) throws SQLException {
        if (connection != null) {
            return connection.createStatement().executeQuery(query);
        }
        return null;
    }

    /*
     * Richiede al db la lista delle visite in base allo stato
     * e le mostra all'utente
     */
    public ResultSet visualizzaVisite(StatiVisite stato)  throws SQLException {
        if (connection != null) {
            String query = "SELECT `Codice Archivio`,`Tipo di Visita`,`Volontaro Selezionato`,`Data programmata`,`Punto di Incontro`,`Titolo` FROM dbingesw.`archivio delle visite` join dbingesw.`tipo di visita` on `archivio delle visite`.`Tipo di visita` = `tipo di visita`. `Codice Tipo di Visita` WHERE `Stato Visita` = '" + StatiVisite.toString(stato) + "'";
            return connection.createStatement().executeQuery(query);
        }
        return null;
    }

    /**
     * Riporta la lista con anche i volontari non associati, da eliminare in futuro per mantenere la coerenza
     * @deprecated
     */
    public ArrayList<String> listaCompletaVolontari() throws IllegalArgumentException{
        //riporta la lista completa dei volontari
        String queryVolontari = "SELECT * FROM dbingesw.volontario;";
        try {
            ResultSet results = connection.createStatement().executeQuery(queryVolontari);
            return estraiColonna(results, "Nickname");
        }
        catch (SQLException e) {
            //Non può non esserci il campo dato che è inserito forzatamente dentro, da migliorare con la seprazione delle query
        }
        return null;
    }

    /**
     * Estrae solo tutti i valori assunti da uno specifico atributo. Siccome esso sposta avanti il puntatore va evocato su un oggetto non ancora letto altrimenti causerà eccezioni
     * @param resultSet un oggetto che rappresenta tutti i risultati della query su cui filtrare
     * @param campo la colonna selezionata su cui filtrare
     * @return un arraylist contenente tutti i valori assunti dalla colonna selezionata
     * @throws IllegalArgumentException se l'oggetto resultset è null o se il campo è null
     * @throws SQLException se il campo non è presente tra i campi disponibili nel resulset
     */
    public ArrayList<String> estraiColonna(ResultSet results, String campo) throws IllegalArgumentException, SQLException{
        if (results == null || campo == null ) throw new IllegalArgumentException();
        ArrayList<String> valori = new ArrayList<>();

        while (results.next()) {
            valori.add(results.getString("Nome"));
        }
        return valori;
    }

    /**
     * Funzione che avvisa se una tabella del DB è vuota, in particolare controlla se non vi e nemmeno una tupla in esso
     * @param tabella il nome della tabella da ispezionare (per ora supporta solo "luogo" e "tipo di visita")
     * @return true se la tabella è vuota, false altrimenti
     * @throws SQLException
     */
    public boolean tabellaDBVuota(String tabella) throws SQLException {
        Map<String, String> selezioneTabella = Map.ofEntries(
            Map.entry("luogo", Queries.SELEZIONA_LUOGHI.getQuery()),
            Map.entry("tipo di visita", Queries.SELEZIONA_TIPI_VISITE.getQuery())
        );
        if (connection != null) {
            //next da true se trova una riga e sposta il cursore su tale riga
            return !connection.createStatement().executeQuery(selezioneTabella.get(tabella)).next();
        }
        return false;
    }
    
}
