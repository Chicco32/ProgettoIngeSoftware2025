package DataBaseImplementation;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ServicesAPI.DTObject;
import ServicesAPI.GestoreConfigurazioneRegistratore;
import ServicesAPI.GestoreFilesConfigurazione;
import ServicesAPI.Registratore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Classe per la gestione della registrazione di un nuovi uelementi nel DB.
 * Quasi tutte le sue funzioni sono void in quanto non ritornanto nulla ma inseriscono i valori richiesti nel DB.
 * Per inserire i dati necessita di connettersi al DB con un connettore.
 * Richiede un implementazione dell'interfaccia Gestore Configurazione e in questo caso 
 * utilizza un file XML per la memorizzazione dei dati di default ma può essere sostiuto cambiando il gestore della scrittura.
 * I path sono salvati nella classe Percorsi files ma essendo richiesti solo in fase di creaizone possono essere modificati
 * 
 * @see GestoreFilesConfigurazione
 * @see XMLConfigurator
 * @see ConnessioneSQL
 * @see PercorsiFiles
 * @see Registratore
 */
public class RegistratoreSQL implements Registratore{

    private static final Map<String, Queries> inserimenti = Map.of(
        "Nuovo configuratore", Queries.REGISTRA_CONFIGURATORE,
        "Nuovo volontario", Queries.REGISTRA_VOLONTARIO,
        "Nuovo luogo", Queries.REGISTRA_LUOGO,
        "Nuovo tipo visita", Queries.REGISTRA_TIPO_VISITA,
        "Associa volontario", Queries.ASSOCIA_VOLONTARIO_VISITA

    );

    private Connection connection;
    private int maxPartecipanti;
    private String areaCompetenza;

    private GestoreConfigurazioneRegistratore fileManager;

    public RegistratoreSQL(String path){
        this.connection = ConnessioneSQL.getConnection();
        this.fileManager = new XMLConfigurator(path);

        try {
            // Carica i dati di default dal file
            if (GestoreFilesConfigurazione.fileExists(path)) {
                this.maxPartecipanti = Integer.parseInt(fileManager.leggiVariabile("maxPartecipanti"));
                this.areaCompetenza = fileManager.leggiVariabile( "areaCompetenza");
            }
            //avvia la creazione di un nuovo file default
            else {
                GestoreFilesConfigurazione.creaFile(path);
                this.areaCompetenza = null;
                this.maxPartecipanti = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserisce dinamicamente un generico Elemento nel database. Il metodo per inserire l'oggetto nello schema corretto
     * non potendolo capire dal tipo ha bisongo di un comando per eseguire la query. Il comando interpella la tabella dei comandi
     * disponibili e ne estrae la query corrispondente.
     * @param object L'oggetto da registrare con i suoi attributi
     * @param comando l'azione specifica su dove debba essere registrato l'oggetto
     */
    private boolean inserisciElementoDB(DTObject object, String comando) throws IllegalArgumentException, SQLException {
        
        String query = inserimenti.get(comando) != null ? inserimenti.get(comando).getQuery() : null;
        if (query == null) {
            throw new IllegalArgumentException("Comando SQL non trovato per: " + comando);
        }

        List<String> campi = object.getCampi();
        if (this.connection != null) {
            try (PreparedStatement stmt = connection.prepareStatement(inserimenti.get(comando).getQuery())) {
                int placeHolder = 1;
                for (String campo : campi) {
                    Object valore = object.getValoreCampo(campo);
                    if (valore instanceof Integer)stmt.setInt(placeHolder, ((Integer)valore).intValue());
                    else if (valore instanceof String) stmt.setString(placeHolder, (String)valore.toString());
                    else if (valore instanceof Date) stmt.setString(placeHolder, formatoDataPerSQL((Date) valore));
                    else if (valore instanceof Time) stmt.setString(placeHolder, formatoOrarioPerSQL((Time)valore));
                    else if (valore instanceof Boolean) stmt.setInt(placeHolder, (Boolean) valore ? 1:0);
                    else throw new IllegalArgumentException("Tipo dato non supportato");
                    placeHolder++;
                }
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public boolean registraNuovoConfiguratore (DTObject configuratore) throws Exception {
        return inserisciElementoDB(configuratore, "Nuovo configuratore");
    }

    public boolean registraNuovoVolontario (DTObject volontario) throws Exception{
        return inserisciElementoDB(volontario, "Nuovo volontario");
    }

    public boolean registraNuovoLuogo (DTObject luogo) throws Exception {
        return inserisciElementoDB(luogo, "Nuovo luogo");
    }

    public boolean associaVolontarioVisita (DTObject associazione) throws Exception {
        return inserisciElementoDB(associazione, "Associa volontario");
    }

    public boolean registraNuovoTipoVisita(DTObject tipoVisita) throws Exception {
        return inserisciElementoDB(tipoVisita, "Nuovo tipo visita");
    }

    private static String formatoDataPerSQL(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String formatoOrarioPerSQL(Time time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);
    }

    Map<CostantiDB, CostantiDB> chiaviPrimarie = Map.of(
        CostantiDB.TIPO_VISITA, CostantiDB.CHIAVE_TIPO_VISITA,
        CostantiDB.ARCHIVIO_VISITE, CostantiDB.CHIAVE_ARCHIVIO_VISITE
    );
    //migliorare la gestione eccezzioni, inserire il throws
    public int generaNuovaChiave(String tabella) {

        //il nome della colonna codici non è consistente fra le varie tabelle
        CostantiDB nomeColonna;
        int nuovaChiave = -1;
        try {
            nomeColonna = chiaviPrimarie.get(CostantiDB.fromString(tabella));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return nuovaChiave;
        }
        ResultSet resultSet;
        if (this.connection != null) {
            String query = "SELECT MAX(" + nomeColonna.getNome() + ") AS maxCodice FROM `dbingesw`." + tabella + "";
            try {
                resultSet = connection.createStatement().executeQuery(query);
                if (resultSet.next()) {
                    nuovaChiave = resultSet.getInt("maxCodice") + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return nuovaChiave;
            }
        }
        return nuovaChiave;
    }

    public void modificaAreaCompetenza(String areaCompetenza) {
        this.areaCompetenza = areaCompetenza;
       fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

    public void modificaMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
        fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

}   
