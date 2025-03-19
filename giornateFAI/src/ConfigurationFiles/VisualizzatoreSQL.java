package ConfigurationFiles;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Services.CoerenzaException;
import Services.DTObject;
import Services.StatiVisite;
import Services.Visualizzatore;


/**
 * Classe per la gestione della estrazione di elementi all'interno del DB e per la loro visualizzazione.
 * Questa classe serve per interpellare il DB attraverso le query SQL e visualizzare i risultati.
 * Il risultato restitutito dai metodi è il contenuto del DB richiesto dalla query specifica del metodo.
 * Per inserire i dati necessita di connettersi al DB con un connettore.
 * Utilizza un file XML per la memorizzazione dei dati di default.
 * 
 * @see ConnesioneSQL
 * @see Visualizzatore
 */
public class VisualizzatoreSQL implements Visualizzatore {

    private Connection connection;

    public VisualizzatoreSQL() {
        this.connection = ConnessioneSQL.getConnection();
    }

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

    public DTObject[] visualizzaTabella(String query) throws CoerenzaException {
    if (connection != null) {
        try {
            return traduciTabella(connection.createStatement().executeQuery(query));
        } catch (SQLException e) {
            throw new CoerenzaException("Errore nella coerenza dei dati", e);
        }
    }
    return null;
    }

    public DTObject[] visualizzaVisite(StatiVisite stato) throws CoerenzaException {
    if (connection != null) {
        String query = Queries.SELEZIONA_VISITE_ARCHIVIO.getQuery();  // Usa le parentesi graffe per chiamare la stored procedure
        CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall(query);
            stmt.setString(1, stato.toString());
        return traduciTabella(stmt.executeQuery());
        } catch (SQLException e) {
            throw new CoerenzaException("Errore nella coerenza dei dati", e);
        }    
    }
    return null;
    }

    /**
     * Riporta la lista con anche i volontari non associati, da eliminare in futuro per mantenere la coerenza
     * @deprecated
     */
    public List<String> listaCompletaVolontari() throws IllegalArgumentException{
        //riporta la lista completa dei volontari
        String queryVolontari = "SELECT * FROM dbingesw.volontario;";
        try {
            ResultSet results = connection.createStatement().executeQuery(queryVolontari);
            return estraiColonna(traduciTabella(results), "Nickname");
        }
        catch (SQLException e) {
            //Non può non esserci il campo dato che è inserito forzatamente dentro, da migliorare con la seprazione delle query
        }
        return null;
    }

    public List<String> estraiColonna(DTObject[] results, String campo) throws IllegalArgumentException{
        if (results == null || campo == null ) throw new IllegalArgumentException();
        ArrayList<String> valori = new ArrayList<>();
        for (DTObject tupla : results) valori.add(tupla.getValoreCampo(campo).toString());
        return valori;
    }

    public boolean tabellaDBVuota(String tabella) throws CoerenzaException {
        Map<String, String> selezioneTabella = Map.ofEntries(
            Map.entry("luogo", Queries.SELEZIONA_LUOGHI.getQuery()),
            Map.entry("tipo di visita", Queries.SELEZIONA_TIPI_VISITE.getQuery())
        );
        if (connection != null) {
            //next da true se trova una riga e sposta il cursore su tale riga
            try {
                return !connection.createStatement().executeQuery(selezioneTabella.get(tabella)).next();
            } catch (SQLException e) {
                throw new CoerenzaException("Errore nella coerenza dei dati", e);
            }
        }
        return false;
    }
    
}
