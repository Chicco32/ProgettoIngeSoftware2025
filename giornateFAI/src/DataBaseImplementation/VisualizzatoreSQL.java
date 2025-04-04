package DataBaseImplementation;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ServicesAPI.DTObject;
import ServicesAPI.Eccezioni;
import ServicesAPI.Eccezioni.DBConnectionException;
import ServicesAPI.StatiVisite;
import ServicesAPI.VisualizzatoreConfiguratore;
import ServicesAPI.VisualizzatoreFruitore;
import ServicesAPI.VisualizzatoreVolontario;


/**
 * Classe per la gestione della estrazione di elementi all'interno del DB e per la loro visualizzazione.
 * Questa classe serve per interpellare il DB attraverso le query SQL e visualizzare i risultati.
 * Il risultato restitutito dai metodi è il contenuto del DB richiesto dalla query specifica del metodo.
 * Per inserire i dati necessita di connettersi al DB con un connettore.
 * Utilizza un file XML per la memorizzazione dei dati di default.
 * 
 * @see ConnessioneSQL
 * @see VisualizzatoreConfiguratore
 * @see VisualizzatoreVolontario
 * @see VisualizzatoreFruitore
 * @see Queries
 */
public class VisualizzatoreSQL implements VisualizzatoreConfiguratore, VisualizzatoreVolontario, VisualizzatoreFruitore {

    private Connection connection;

    public VisualizzatoreSQL() {
        this.connection = ConnessioneSQL.getConnection();
    }

    /**
     * Traduce l'oggetto {@code Resulset} derivato dal database SQL in una serie di Tuple di tipo DTObject
     * @param tabellaSQL la tabella originaria della query
     * @return la tabella con gli stessi valori in formato {@code DTObject}, altrimenti <pre> new DTObject[0] </pre> in caso di tabella vuota
     */
    private DTObject[] traduciTabella(ResultSet tabellaSQL) {
        ArrayList<DTObject> listaOggetti = new ArrayList<>();
        try {
            //prima ottieni la struttra della tabella come lista di colonne
            int columnCount = tabellaSQL.getMetaData().getColumnCount();
            String[] colonne = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
            colonne[i] = tabellaSQL.getMetaData().getColumnName(i + 1);
            }

            while (tabellaSQL.next()) {
                //istanzia una nuova riga
                DTObject oggetto = new Tupla(tabellaSQL.getMetaData().getTableName(1), colonne);
    
                //e la riempie con i valori
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = tabellaSQL.getMetaData().getColumnName(i);
                    Object value = tabellaSQL.getObject(i);
                    oggetto.impostaValore(value, columnName);
                }
                listaOggetti.add(oggetto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaOggetti.toArray(new DTObject[0]);
    }

    /**
     * Ritorna i risultati dell'esecuzone di una certa query sul database
     * @param query la query da far eseguire, il metodo è specifico per le query solo di visualizzazione non di modifica, per quelle usare il registratore
     * @return Un oggetto {@code ResultSet} con le tuple risultate dalla interrogazione
     */
    private ResultSet eseguiQuery(Queries query) throws Eccezioni.DBConnectionException {
    if (connection != null) {
        try {
            return connection.createStatement().executeQuery(query.getQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: " + query.getQuery(), e);
        }
    }
    return null;
    }

    /**
     * Estrae solo tutti i valori assunti da uno specifico atributo. Siccome esso sposta avanti il puntatore va evocato su un oggetto non ancora letto altrimenti causerà eccezioni
     * @param resul un oggetto che rappresenta tutti i risultati della query su cui filtrare
     * @param campo la colonna selezionata su cui filtrare
     * @return un arraylist contenente tuttti i valori assunti dalla colonna selezionata
     * @throws IllegalArgumentException se l'oggetto results o se campo sono {@code null}
     */
    private List<String> estraiColonna(ResultSet results, String campo) throws IllegalArgumentException{
        if (results == null || campo == null) {
            throw new IllegalArgumentException("Il parametro results o campo non può essere null.");
        }

        List<String> valoriColonna = new ArrayList<>();
        try {
            while (results.next()) {
            valoriColonna.add(results.getString(campo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valoriColonna;
    }

    /**
     * Funzione che avvisa se una tabella del DB è vuota, in particolare controlla se non vi e nemmeno una tupla in esso
     * @param tabella il nome della tabella da ispezionare (per ora supporta solo "luogo" e "tipo di visita")
     * @return true se la tabella è vuota, false altrimenti
     */
    private boolean tabellaDBVuota(String tabella) throws Eccezioni.DBConnectionException {
        Map<String, String> selezioneTabella = Map.ofEntries(
            Map.entry("luogo", Queries.SELEZIONA_LUOGHI.getQuery()),
            Map.entry("tipo di visita", Queries.SELEZIONA_TIPI_VISITE.getQuery())
        );
        //next da true se trova una riga e sposta il cursore su tale riga
        try {
            return !connection.createStatement().executeQuery(selezioneTabella.get(tabella)).next();
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public boolean nonCisonoLuoghiRegistrati() throws Eccezioni.DBConnectionException {
        return tabellaDBVuota("luogo");
    }

    public DTObject[] visualizzaVisite(StatiVisite stato) throws Eccezioni.DBConnectionException {
        String query = Queries.SELEZIONA_VISITE_ARCHIVIO.getQuery();  // Usa le parentesi graffe per chiamare la stored procedure
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, stato.toString());
            return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public DTObject[] visualizzaElencoVolontari() throws Eccezioni.DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.SELEZIONA_VOLONTARI));
    }

    public DTObject[] visualizzaElencoLuoghi() throws Eccezioni.DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.SELEZIONA_LUOGHI));
    }

    public DTObject[] visualizzaElencoTipiDiVisite() throws Eccezioni.DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.SELEZIONA_TIPI_VISITE));
    }

    public List<String> listaLuoghiRegistrati() throws Eccezioni.DBConnectionException {
        List<String> luoghiDisponibili = new ArrayList<>();
        try {
            luoghiDisponibili = estraiColonna(eseguiQuery(Queries.SELEZIONA_LUOGHI), "Nome");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return luoghiDisponibili;
    }

    public DTObject[] visualizzaElenecoTipiDiVisiteAssociate(String volontarioAssociato) throws Eccezioni.DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.VISITE_ASSOCIATE_VOLONTARIO.getQuery())) {
            stmt.setString(1, volontarioAssociato);
            return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public DTObject[] estraiDOWPossibiliVolontario(String volontarioAssociato) throws Eccezioni.DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.GIORNI_POSSIBILI_VOLONTARIO.getQuery())) {
            stmt.setString(1, volontarioAssociato);
            return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public DTObject[] estraiTipiDiVisiteVolontario() throws DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.VISITE_PER_OGNI_VOLONTARIO));
    }

    
    public DTObject[] estraiGiorniTipoDiVisita() throws DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.GIORNI_POSSIBILI_VISITA));
    }

    public DTObject[] VisualizzaIstanzeVisiteDisponibili() throws DBConnectionException {
        return traduciTabella(eseguiQuery(Queries.ISTANZE_VISITE_DISPONIBILI));
    }

    public DTObject[] VisualizzaIstanzeIscritte(String volontarioAssociato) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.VISUALIZZA_ISTANZE_ISCRITTO.getQuery())) {
            stmt.setString(1, volontarioAssociato);
            return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    @Override
    public DTObject[] VisualizzaIstanzeCancellate(String volontarioAssociato) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.VISUALIZZA_ISTANZE_CANCELLATE.getQuery())) {
            stmt.setString(1, volontarioAssociato);
            return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }
    
}
