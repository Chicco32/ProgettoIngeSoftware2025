import java.io.File;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Registratore {

    private int maxPartecipanti;
    private String areaCompetenza;

    private String nickname;
    private String password;


    public Registratore() {
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
                creaFileDefault();
            }
    }

    /*
     * Da finire, rispecchia il caso d'uso di inseirmento della prima volta di un configuratore
     */
    private void creaFileDefault() {
        this.maxPartecipanti = 100;
        this.areaCompetenza = "Tecnologia";
    }

    public void registraNuovoConfiguratore(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        
        Connection conn = ConnesioneSQL.getConnection();
        if (conn != null) {
            try {
                String insert = "INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES ('" + this.nickname + "', '" + this.password + "')";
                conn.createStatement().executeUpdate(insert);
                System.err.println("Configuratore correttamente reigstrato!");
            } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore nella registrazione!");
            }
        }

        this.nickname = null;
        this.password = null;
    }
}
