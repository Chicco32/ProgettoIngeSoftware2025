import java.io.File;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Registratore {

    private Connection connection;

    private int maxPartecipanti;
    private String areaCompetenza;

    private String nickname;
    private String password;


    public Registratore(Connection connection) {
        this.connection = connection;
        this.nickname = null;
        this.password = null;

            // Carica i dati di default dal file XML
            try {
                File xmlFile = new File("default_data/registratore.xml");
                if (xmlFile.exists()) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);
                    doc.getDocumentElement().normalize();

                    Element root = doc.getDocumentElement();
                    this.maxPartecipanti = Integer.parseInt(root.getElementsByTagName("maxPartecipanti").item(0).getTextContent());
                    this.areaCompetenza = root.getElementsByTagName("areaCompetenza").item(0).getTextContent();
                } else {
                    creaFileDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /*
     * Da finire, rispecchia il caso d'uso di inseirmento della prima volta di un configuratore
     */
    private void creaFileDefault() {
        this.maxPartecipanti = CliUtente.chiediMaxPartecipanti();
        this.areaCompetenza = CliUtente.chiediAreaCompetenza();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element root = doc.createElement("registratore");
            doc.appendChild(root);

            Element maxPartecipanti = doc.createElement("maxPartecipanti");
            maxPartecipanti.appendChild(doc.createTextNode(String.valueOf(this.maxPartecipanti)));
            root.appendChild(maxPartecipanti);

            Element areaCompetenza = doc.createElement("areaCompetenza");
            areaCompetenza.appendChild(doc.createTextNode(this.areaCompetenza));
            root.appendChild(areaCompetenza);

            // Scrive il contenuto nel file XML
            File xmlFile = new File("default_data/registratore.xml");
            xmlFile.getParentFile().mkdirs();
            xmlFile.createNewFile();
            xmlFile.setReadable(true);
            
            

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
