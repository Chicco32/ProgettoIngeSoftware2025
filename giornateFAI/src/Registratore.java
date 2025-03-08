
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Classe per la gestione della registrazione di un nuovi uelementi nel DB.
 * Quasi tutte le sue funzioni sono void in quanto non ritornanto nulla ma inseriscono i valori richiesti nel DB.
 * Per inserire i dati necessita di connettersi al DB con un connettore.
 * Utilizza un file XML per la memorizzazione dei dati di default.
 * 
 * @see XMLManager
 * @see ConnesioneSQL
 */
public class Registratore {

    private Connection connection;
    private int maxPartecipanti;
    private String areaCompetenza;
    public static final String TABELLATIPOVISITE = "tipo di visita";
    public static final String TABELLAARCHIVIOVISITE = "archivio delle visite";
    private static final String CODICETIPOVISITE = "Codice Tipo di Visita";
    private static final String CODICEARCHIVIO = "Codice Archivio";


    public Registratore() {
        this.connection = ConnesioneSQL.getConnection();

            try {
                // Carica i dati di default dal file XML
                if (XMLManager.fileExists(XMLManager.pathRegistratore)) {
                this.maxPartecipanti = Integer.parseInt(XMLManager.leggiVariabile(XMLManager.pathRegistratore, "maxPartecipanti"));
                this.areaCompetenza = XMLManager.leggiVariabile(XMLManager.pathRegistratore, "areaCompetenza");
                }

                //avvia la creazione di un nuovo file default
                else {
                    XMLManager.creaFile(XMLManager.pathRegistratore);
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
            String insert = "INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES ('" + nickname + "', '" + password + "')";
            this.connection.createStatement().executeUpdate(insert);
            return true;            
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
            String insert = "INSERT into `dbingesw`.`volontario` (`Nickname`,`Password`) VALUES ('" + nickname + "', '" + password + "')";
            this.connection.createStatement().executeUpdate(insert);
            return true;
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
            String insert = "INSERT into `dbingesw`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES ('" + nome + "','" + descrizione + "', '" + indirizzo + "')";
            this.connection.createStatement().executeUpdate(insert);
            return true;
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

    //conta la quantita di elementi e ritorna il numero successivo come nuova chiave primaria in modo progressivo
    public int generaNuovaChiave(String tabella) {

        //il nome della colonna codici non è consistente fra le varie tabelle
        String nomeColonna = "";
        switch (tabella) {
            case TABELLATIPOVISITE:
                nomeColonna = CODICETIPOVISITE;
                break;
            case TABELLAARCHIVIOVISITE:
                nomeColonna = CODICEARCHIVIO;
            default:
                break;
        }

        int nuovaChiave = 0;
        if (this.connection != null) {
            try {
            String query = "SELECT MAX(`" + nomeColonna + "`) AS maxCodice FROM `dbingesw`.`" + tabella + "`";
            var resultSet = this.connection.createStatement().executeQuery(query);
            if (resultSet.next()) {
                nuovaChiave = resultSet.getInt("maxCodice") + 1;
            } else {
                nuovaChiave = 1;
            }
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
        return nuovaChiave;
    }

    /**
     * Funzione che registra un nuovo tipo di visita nel DB. 
     * @param codice
     * @param luogo
     * @param titolo
     * @param descrizione
     * @param dataInizio
     * @param dataFine
     * @param oraInizio
     * @param durata
     * @param necessitaBiglietto
     * @param minPartecipanti
     * @param maxPartecipanti
     * @param configuratore
     * @return true se la registrazione è andata a buon fine, false altrimenti
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
                                    "VALUES" +
                                    "("+ codice +"," +
                                    "'" + luogo + "'," + 
                                    "'" + titolo + "'," + 
                                    "'" + descrizione + "'," + 
                                    "'" + dataIniziosql + "'," + 
                                    "'" + dataFinesql + "'," + 
                                    "'" + oraInziosql + "'," +  
                                    "" + durata + "," + 
                                    "" + biglietto +"," + 
                                    "" + minPartecipanti + "," + 
                                    "" + maxPartecipanti + "," + 
                                    "'" +  configuratore + "')";
            this.connection.createStatement().executeUpdate(insert);
            return true;
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
            String insert = "INSERT INTO `dbingesw`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (" + codiceVisita +",'" + volontarioSelezionato +"');";
            this.connection.createStatement().executeUpdate(insert);
            return true;
        }
        return false;
    }

    /**
     * Modifica l'area di competenza della società, Ogni volta che viene invocata questa funzione viene anche scritta nel file XML
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore  in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaAreaCompetenza(String areaCompetenza) {
        this.areaCompetenza = areaCompetenza;
        XMLManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

    /**
     * Modifica il max numero di partecipanti che possono essere iscritti. Ogni volta che viene invocata questa funzione viene anche scritta nel file XML
     * di default. Puo' essere modificata solo da un Configuratore.
     * Può essere invocata la prima volta per settare il primo valore in caso non fosse ancora inserito.
     * @param areaCompetenza la nuova area di competenza in cui adopera la società che riguardeà i luoghi da inserire.
     */
    public void modificaMaxPartecipanti(int maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
        XMLManager.scriviRegistratoreDefault(areaCompetenza, maxPartecipanti);
    }

}   
