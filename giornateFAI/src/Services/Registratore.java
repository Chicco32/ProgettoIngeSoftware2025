package Services;

import java.sql.Connection;
import java.util.Date;
import ConfigurationFiles.ConnessioneSQL;
import ConfigurationFiles.CostantiDB;
import ConfigurationFiles.PercorsiFiles;
import ConfigurationFiles.XMLConfigurator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
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
 * @see IGestoreFilesConfigurazione
 * @see XMLConfigurator
 * @see ConnessioneSQL
 * @see PercorsiFiles
 */
public class Registratore {

    private Connection connection;
    private int maxPartecipanti;
    private String areaCompetenza;

    private IGestoreFilesConfigurazione fileManager;

	public Registratore(String path){

		this.connection = ConnessioneSQL.getConnection();
        this.fileManager = new XMLConfigurator(path);

		try {
			// Carica i dati di default dal file
        	if (IGestoreFilesConfigurazione.fileExists(path)) {
                this.maxPartecipanti = Integer.parseInt(fileManager.leggiVariabile("maxPartecipanti"));
        		this.areaCompetenza = fileManager.leggiVariabile( "areaCompetenza");
        	}
            //avvia la creazione di un nuovo file default
            else {
            	IGestoreFilesConfigurazione.creaFile(path);
        	    this.areaCompetenza = null;
    		    this.maxPartecipanti = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
    	}
    }

    /**
     * Funzione per la registrazione di un nuovo configuratore nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoConfiguratore (String nickname, String password) throws SQLIntegrityConstraintViolationException, Exception {

        if (this.connection != null) {
            String insert = "INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES (?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setString(1, nickname);
                stmt.setString(2, password);
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    /**
     * Funzione per la registrazione di un nuovo volontario nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * 
     * @param nickname il possibile nickname da registrare
     * @param password la password inserita dall'utente
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoVolontario (String nickname, String password) throws SQLIntegrityConstraintViolationException, Exception{
     
        if (this.connection != null) {
        String insert = "INSERT into `dbingesw`.`volontario` (`Nickname`,`Password`) VALUES (?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(insert)) {
            stmt.setString(1, nickname);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
            }
        }
        return false;
    }

    /**
     * Funzione per la registrazione di un nuovo luogo nel DB.
     * In particolare la funzione richiede al DB l'inserimento dei dati forniti e riporta la risposta del DB in caso di avvenuto inserimento o meno
     * La funzione ritorna true se la registrazione è andata a buon fine, false altrimenti.
     * @param nome il nome del luogo
     * @param descrizione la descrizione di al massimo 100 caratteri del luogo
     * @param indirizzo l'inidirizzo di al massimo 45 caratteri
     * @return lo stato della registrazione, true se è andata a buon fine, false altrimenti
     */
    public boolean registraNuovoLuogo (String nome, String descrizione, String indirizzo) throws SQLIntegrityConstraintViolationException, Exception {
        if (this.connection != null) {
            String insert = "INSERT into `dbingesw`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES (?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setString(1, nome);
                stmt.setString(2, descrizione);
                stmt.setString(3,indirizzo);
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    private static String formatoDataPerSQL(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String formatoOrarioPerSQL(Time time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);
    }

    /**
     * Il metodo genera una nuova chiave univoca valida per la tabella selezionata. In particolare la funzione conta il numero più alto fra le chiavi e ritorna il
     * numero progressivo successivo come numero da usare come chiave, in questa maniera permette la generazione di chiavi anche in caso di eliminazioni di righe dalla tabella. 
     * 
     * @param tabella la tabella da selezionare in cui generare la chiave
     * @return un {@code int} che rappresenta il valore della chiave da inserire. In caso di tabella di tabella vuota restitutisce valore {@code 1} e in caso di errori nella generezione restituisce {@code -1}
     */
    public int generaNuovaChiave(CostantiDB tabella) {

        //il nome della colonna codici non è consistente fra le varie tabelle
        CostantiDB nomeColonna;
        int nuovaChiave = -1;
        switch (tabella) {
            case TIPO_VISITA:
                nomeColonna = CostantiDB.CHIAVE_TIPO_VISITA;
                break;
            case CHIAVE_ARCHIVIO_VISITE:
                nomeColonna = CostantiDB.CHIAVE_ARCHIVIO_VISITE;
                break;
            default:
                return nuovaChiave;
        }

        ResultSet resultSet;
        if (this.connection != null) {
            String query = "SELECT MAX(" + nomeColonna.getNome() + ") AS maxCodice FROM `dbingesw`." + tabella.getNome() + "";
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

    /**
     * Funzione che registra un nuovo tipo di visita nel DB.
     * La funzione richiede le funzioni aggiuntive:  
     * <PRE>
     *  this.formatoDataPerSQL(Date date); formatoOrarioPerSQL(Time time);
     * </PRE>
     * @param codice codice univoco che identifica il tipo di visita
     * @param luogo il luogo in cui si tiene, deve essere gia registrato nella tabella luoghi
     * @param titolo il titolo che riassume la visita
     * @param descrizione una breve descriizione dell'evento
     * @param dataInizio un oggetto di tipo {@code Date} che serve per rappresentare l'inzio del periodo del'evento
     * @param dataFine  un oggetto di tipo {@code Date} che serve per rappresentare la fine del periodo del'evento
     * @param oraInizio l'ora in cui l'evento si svolge
     * @param durata la durata in minuti dell'evento
     * @param necessitaBiglietto un oggetto {@code boolean} che rappreseta se la visita ha bisongo di un biglietto
     * @param minPartecipanti il numero minino di partecipanti affinche l'evento sia effettuato
     * @param maxPartecipanti il numero massimo di partecipanti che l'evento può ospitare
     * @param configuratore il nickname del configuratore che ha inserito l'evento
     * @return true se la registrazione è andata a buon fine, false altrimenti
     * 
     * @see java.sql.Date
     * @see java.sql.Time
     */
    public boolean registraNuovoTipoVisita(int codice, String luogo, String titolo, String descrizione, Date dataInizio, Date dataFine, 
    Time oraInizio, int durata, boolean necessitaBiglietto, int minPartecipanti, int maxPartecipanti, String configuratore)
    throws Exception {
        
        int biglietto;
        if (necessitaBiglietto) biglietto = 1;
        else biglietto = 0;
        String dataIniziosql = formatoDataPerSQL(dataInizio);
        String dataFinesql = formatoDataPerSQL(dataFine);
        String oraInziosql = formatoOrarioPerSQL(oraInizio);
        
        if (this.connection != null) {
            String insert = "INSERT INTO `dbingesw`.`tipo di visita`" +
                                    "(`Codice Tipo di Visita`," +
                                    "`Punto di Incontro`," +
                                    "`Titolo`," +
                                    "`Descrizione`," +
                                    "`Giorno di Inizio (periodo anno)`," + 
                                    "`Giorno di Fine (periodo anno)`," +
                                    "`Ora di inizio`," +
                                    "`Durata`," +
                                    "`Necessita Biglietto`," +
                                    "`Min Partecipanti`," +
                                    "`Max Partecipanti`," +
                                    "`Configuratore referente`)" +
                                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setInt(1, codice);
                stmt.setString(2, luogo);
                stmt.setString(3, titolo);
                stmt.setString(4, descrizione);
                stmt.setString(5, dataIniziosql);
                stmt.setString(6, dataFinesql);
                stmt.setString(7, oraInziosql);
                stmt.setInt(8, durata);
                stmt.setInt(9, biglietto);
                stmt.setInt(10, minPartecipanti);
                stmt.setInt(11, maxPartecipanti);
                stmt.setString(12, configuratore);
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    /**
     * Funziona per inserire le disponibilità dei volontari a uno specifico tipo di visita
     * @param codiceVisita il codice del tipo di visita da associare
     * @param volontarioSelezionato il nickname del volontario
     * @return true se l'inserimento è andato a buon fine, false altrimenti
     * @throws SQLIntegrityConstraintViolationException
     * @throws Exception
     */
    public boolean associaVolontarioVisita (int codiceVisita, String volontarioSelezionato) throws SQLIntegrityConstraintViolationException, Exception {
        if (this.connection != null) {
            String insert = "INSERT INTO `dbingesw`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (?,?);";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setInt(1, codiceVisita);
                stmt.setString(2, volontarioSelezionato);
                stmt.executeUpdate();
                return true;
            }
        }
        return false;
    }

    /**
     * Modifica l'area di competenza della società, Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore  in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaAreaCompetenza(String areaCompetenza) {
        this.areaCompetenza = areaCompetenza;
       fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

    /**
     * Modifica il max numero di partecipanti che possono essere iscritti. Ogni volta che viene invocata questa funzione viene anche scritta nel file
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
        fileManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

}   
