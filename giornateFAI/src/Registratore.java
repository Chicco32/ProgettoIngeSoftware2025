import java.io.File;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;


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
                    XMLManager.scriviRegistratoreDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public boolean registraNuovoConfiguratore(String nickname, String password) {

        if (this.connection != null) {
            try {
                String insert = "INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES ('" + nickname + "', '" + password + "')";
                this.connection.createStatement().executeUpdate(insert);
                System.err.println("Configuratore correttamente reigstrato!");
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                CliUtente.nicknameGiaInUso();
            } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore nella registrazione!");
            }
        }
        return false;
    }
}
