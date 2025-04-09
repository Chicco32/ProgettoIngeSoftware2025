package DataBaseImplementation;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import ServicesAPI.DTObject;
import ServicesAPI.Eccezioni;
import ServicesAPI.Eccezioni.DBConnectionException;
import ServicesAPI.GestoreConfiguratore;
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
 * @see Queries
 */
public class RegistratoreSQL implements Registratore{

    private static final Map<String, Queries> inserimenti = Map.of(
        "Nuovo volontario", Queries.REGISTRA_VOLONTARIO,
        "Nuovo luogo", Queries.REGISTRA_LUOGO,
        "Nuovo tipo visita", Queries.REGISTRA_TIPO_VISITA,
        "Associa volontario", Queries.ASSOCIA_VOLONTARIO_VISITA,
        "Associa giorno settimana", Queries.ASSOCIA_GIORNI_SETTIMANA_VISITA
    );

    private Connection connection;
    private int maxPartecipanti;
    private String areaCompetenza;

    private GestoreConfiguratore fileManager;

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
                    else if (valore instanceof Time) stmt.setString(placeHolder, formatoOrarioPerSQL((Time)valore));
                    else if (valore instanceof Date) stmt.setString(placeHolder, formatoDataPerSQL((Date) valore));
                    else if (valore instanceof Boolean) stmt.setInt(placeHolder, (Boolean) valore ? 1:0);
                    else throw new IllegalArgumentException("Tipo dato non supportato");
                    placeHolder++;
                }
                stmt.executeUpdate();
                return true;
            } catch (MysqlDataTruncation e) {
                e.printStackTrace();
                throw new IllegalArgumentException(e);
            }
        }
        return false;
    }

    
    public boolean nomeUtenteUnivoco(String nomeUtente) throws Eccezioni.DBConnectionException{
        
        try (PreparedStatement stmt = connection.prepareStatement(Queries.NICKNAME_UNIVOCO.getQuery())) {
            stmt.setString(1, nomeUtente);
            //prova a cercare il nome nel DB
            ResultSet rs = stmt.executeQuery();
            //se non è presente nel DB è un nome valido
            if (!rs.next()) return true;
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore nella connessione al DB", e);            
        }
        return false;
    }

    public boolean registraNuovoVolontario (DTObject volontario) throws Eccezioni.DBConnectionException{
        ServizioHash.cifraPassword(volontario);
        try {
            return inserisciElementoDB(volontario, "Nuovo volontario");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore nella connessione al DB", e);
        }
        return false;
    }

    public boolean registraNuovoLuogo (DTObject luogo) throws Eccezioni.DBConnectionException {
        try {
            return inserisciElementoDB(luogo, "Nuovo luogo");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore nella connessione al DB", e);
        }
        return false;
    }

    public boolean associaVolontarioVisita (DTObject associazione) throws Eccezioni.DBConnectionException {
        try {
            return inserisciElementoDB(associazione, "Associa volontario");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore nella connessione al DB", e);
        }
        return false;
    }

    public boolean registraNuovoTipoVisita(DTObject tipoVisita) throws Eccezioni.DBConnectionException {
        //genero la chiave per la nuova visita
        int nuovoCodice = generaNuovaChiave(CostantiDB.TIPO_VISITA.getNome());
        tipoVisita.impostaValore(nuovoCodice, "Codice Tipo di Visita");

        //Prima registro nel DB la visita filtrata senza i giorni della settimana
        String [] filtro = {"Codice Tipo di Visita","Punto di Incontro","Titolo", "Descrizione","Giorno inzio", 
         "Giorno fine", "Ora di inizio", "Durata", "Necessita Biglietto", "Min Partecipanti", "Max Partecipanti", "Configuratore referente"};
        DTObject visitaFiltrata = ((Tupla) tipoVisita).filtraCampi(filtro);
        Boolean visitaInserita = false;
        try {
            visitaInserita = inserisciElementoDB(visitaFiltrata, "Nuovo tipo visita");
        
            //poi estraggo i giorni della settimana e lavoro su di essi
            String[] giorniSettimana = (String[]) tipoVisita.getValoreCampo("Giorni settimana");
            for (String giorno : giorniSettimana) {
                //inserisco i giorni uno alla volta nel DataBase
                Tupla tupla = new Tupla("Giorni settimana", new String[]{"Codice Tipo di Visita","Giorno della settimana"});
                tupla.impostaValore(nuovoCodice,"Codice Tipo di Visita");
                tupla.impostaValore(giorno, "Giorno della settimana");
                inserisciElementoDB(tupla, "Associa giorno settimana");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore nella connessione al DB", e);
        }
        return visitaInserita;
    }

    private static String formatoDataPerSQL(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String formatoOrarioPerSQL(Time time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time) + ":00";
    }

    //estraggo l'interrogazione corretta in base alla tabella
    Map<CostantiDB, Queries> chiaviNuove = Map.of(
        CostantiDB.TIPO_VISITA, Queries.GENERA_CHIAVE_TIPO_VISITA,
        CostantiDB.ARCHIVIO_VISITE_ATTIVE, Queries.GENERA_CHIAVE_ARCHIVIO
    );

    /**
     * Il metodo genera una nuova chiave univoca valida per la tabella selezionata. In particolare la funzione conta il numero più alto fra le chiavi e ritorna il
     * numero progressivo successivo come numero da usare come chiave, in questa maniera permette la generazione di chiavi anche in caso di eliminazioni di righe dalla tabella. 
     * 
     * @param tabella la tabella da selezionare in cui generare la chiave
     * @return un {@code int} che rappresenta il valore della chiave da inserire. In caso di tabella di tabella vuota restitutisce valore {@code 1} e in caso di errori nella generezione restituisce {@code -1}
     */
    private int generaNuovaChiave(String tabella) {

        int nuovaChiave = -1;
        try{
            //delega al server la generazione della chiave
            String query = chiaviNuove.get(CostantiDB.fromString(tabella)).getQuery();
            ResultSet result = connection.createStatement().executeQuery(query);
            if (result.next()) {
                //la chiave deve essere in un campo chimato maxCodice per essere letta
                nuovaChiave = result.getInt("maxCodice");
            }
        }

        //tutte le eccezioni dovrebbero essere causate da errori di codice non dall'utente
        catch (IllegalArgumentException | SQLException e) {
            e.printStackTrace();
            return nuovaChiave;
        }
        return nuovaChiave;
    }

    public boolean rimozioneLuogo(String nomeLuogo) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.RIMUOVI_LUOGO.getQuery())) {
            stmt.setString(1, nomeLuogo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public boolean rimozioneVisita(String titoloVisita) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.RIMUOVI_TIPO_DI_VISITA.getQuery())) {
            stmt.setString(1, titoloVisita);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public boolean rimozioneVolontario(String nickname) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.RIMUOVI_VOLONTARIO.getQuery())) {
            stmt.setString(1, nickname);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public void verificaCoerenzaPostRimozione() throws DBConnectionException {
        try {
            connection.createStatement().executeQuery(Queries.ELIMINA_DATI_ORFANI.getQuery());
        } catch (SQLException e) {
            throw new DBConnectionException("Errore nella connessione al DB", e);
        }
    }

    public boolean registraIstanzaDiVisita(DTObject istanza) throws DBConnectionException {
        try (PreparedStatement stmt = connection.prepareStatement(Queries.REGISTRA_ISTANZA_VISITA.getQuery())) {
            stmt.setInt(1, generaNuovaChiave("archivio visite attive"));
            stmt.setString(2, "proposta");
            stmt.setString(3, (String) istanza.getValoreCampo("codice visita"));
            stmt.setString(4, (String) istanza.getValoreCampo("nickname volontario"));
            stmt.setString(5, formatoDataPerSQL((Date) istanza.getValoreCampo("data")));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Eccezioni.DBConnectionException("Errore durante l'esecuzione della query: ", e);
        }
    }

    public void modificaAreaCompetenza(String areaCompetenza) throws Eccezioni.ConfigFilesException {
        this.areaCompetenza = areaCompetenza;
       try {
        fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    } catch (FileNotFoundException e) {
        throw new Eccezioni.ConfigFilesException("File non trovato", e);
    }
    }

    public void modificaMaxPartecipanti(int maxPartecipanti) throws Eccezioni.ConfigFilesException {
        this.maxPartecipanti = maxPartecipanti;
        try {
            fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
        } catch (FileNotFoundException e) {
            throw new Eccezioni.ConfigFilesException("File non trovato", e);
        }
    }

}   
