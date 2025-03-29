package DataBaseImplementation;

import java.io.FileNotFoundException;

import ServicesAPI.GestoreConfiguratore;
import ServicesAPI.GestoreFilesConfigurazione;

/**
 * Classe implementativa di XmlManager specifica per questo programma. Esso cerca i file di configuraizone richiesti e li mostra all'applicazione.
 * In particolare implementa la verisone XML dei mietodi richiesti da Gestore File di configurazione e per farlo si appoggia sulla creaiozne di
 * oggetti di lettura e scrittura xml forniti da XMLManagare
 * 
 * @see XMLManager
 * @see GestoreFilesConfigurazione
 */
public class XMLConfigurator extends XMLManager implements GestoreConfiguratore {

    public XMLConfigurator (String path) {
        super(path);
    }

    public void scriviRegistratoreDefault (String areaCompetenza, int maxPartecipanti) throws FileNotFoundException {
		inizializzaWriter();
    	try {
			xmlw.writeStartDocument("utf-8", "1.0");
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("registratore");
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("maxPartecipanti");
        	xmlw.writeCharacters(String.valueOf(maxPartecipanti));
        	xmlw.writeEndElement();
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeStartElement("areaCompetenza");
        	xmlw.writeCharacters(areaCompetenza);
        	xmlw.writeEndElement();
        	xmlw.writeCharacters(System.getProperty("line.separator"));
        	xmlw.writeEndElement();
    	} catch (Exception e) {
        	System.out.println("Errore nella scrittura del file:");
		}
        chiudiWriter();
    }

}
