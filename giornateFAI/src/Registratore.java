
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;

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
    public boolean registraNuovoConfiguratore(String nickname, String password) {

        if (this.connection != null) {
            try {
                String insert = "INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES ('" + nickname + "', '" + password + "')";
                this.connection.createStatement().executeUpdate(insert);
                CliUtente.configuratoreCorrettamenteRegistrato();
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                CliUtente.nicknameGiaInUso();
            } catch (Exception e) {
            e.printStackTrace();
            CliUtente.erroreRegistrazione();
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
    public boolean registraNuovoVolontario (String nickname, String password) {
     
        if (this.connection != null) {
            try {
                String insert = "INSERT into `dbingesw`.`volontario` (`Nickname`,`Password`) VALUES ('" + nickname + "', '" + password + "')";
                this.connection.createStatement().executeUpdate(insert);
                CliUtente.volontarioCorrettamenteRegistrato();
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                CliUtente.nicknameGiaInUso();
            } catch (Exception e) {
            e.printStackTrace();
            CliUtente.erroreRegistrazione();
            }
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
