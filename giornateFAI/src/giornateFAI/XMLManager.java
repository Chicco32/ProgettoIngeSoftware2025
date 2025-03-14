package giornateFAI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.text.DateFormatter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Classe per la gestione dei file XML, provvede gli strumenti per la lettura e la scrittura di file XML
 * e per la gestione dei file stessi. Per usare la libreria anzichè interagire direttamente con il file XML
 * si interagisce con questa classe che si occuperà di gestire il file XML
 */
public class XMLManager {

	private static final DateFormat df=new SimpleDateFormat("yyyy-MM-dd");

    public static final String pathRegistratore = "giornateFAI\\default_data\\registratore.xml";
    public static final String pathDatePrecluse = "giornateFAI\\default_data\\datePrecluse.xml";

    public static final String INIZIO_LETTURA = "Inizio la lettura di %s";
    public static final String FINE_LETTURA = "Ho finito di leggere %s";
    public static final String INIZIO_SCRITTURA = "Inizio la scrittura di %s";
    public static final String FINE_SCRITTURA = "Ho finito di scrivere %s";

    /**
     * Inizializza un reader per il file XML
     * @param path il percorso del file XML
     * @return il reader inizializzato
     */
    public static XMLStreamReader inzializzaReader(String path) {
        XMLInputFactory xmlif = null;
        XMLStreamReader xmlr = null;
        try {
            xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader(path, new FileInputStream(path));
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato");
            System.out.println(e.getMessage());
        } 
        catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }
        return xmlr;
    }

    /**
     * Inizializza un writer per il file XML
     * @param path il percorso del file XML
     * @return il writer inizializzato
     */
    private static XMLStreamWriter inizializzaWriter(String path) {
        XMLOutputFactory xmlof = null;
        XMLStreamWriter xmlw = null;
        try {
            xmlof = XMLOutputFactory.newInstance();
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(path), "utf-8");
            xmlw.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }
        return xmlw;
    }

    /**
     * Chiude il reader
     * @param xmlr il reader da chiudere
     */
    private static void chiudiReader(XMLStreamReader xmlr) {
        try {
            xmlr.close();
        } catch (Exception e) {
            System.out.println("Errore nella chiusura del reader:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Chiude il writer
     * @param xmlw il writer da chiudere
     */
    private static void chiudiWriter(XMLStreamWriter xmlw) {
        try {
            xmlw.writeEndDocument();
            xmlw.flush();
            xmlw.close();
        } catch (Exception e) {
            System.out.println("Errore nella chiusura del writer:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Controlla se un file esiste
     * @param path il percorso del file
     * @return true se il file esiste, false altrimenti
     */
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    /**
     * Crea un file
     * @param path il percorso del file
     */
    public static void creaFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();
        } catch (Exception e) {
            System.out.println("Errore nella creazione del file:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Legge una variabile da un file XML sapendone il tag associato.
     * Questo metodo permette di leggere solo un valore e funziona solo se di quel tag c'è una sola occorrenza
     * altrmenti legge la prima occorrenza disponibile nel file.
     * @param path il percorso del file XML
     * @param tag il tag associato alla variabile da leggere
     * @return Il valore della variabile letta nel file XML
     */
    public static String leggiVariabile (String path, String tag) {
        XMLStreamReader xmlr = inzializzaReader(path);
        String variabile = null;
        try {
            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamReader.START_ELEMENT:
                        if (xmlr.getLocalName().equals(tag)) {
                            //System.out.println(String.format(INIZIO_LETTURA, tag));
                            variabile = xmlr.getElementText();
                        }
                        break;
                    default:
                        break;
                }
                xmlr.next();
            }
            //System.out.println(String.format(FINE_LETTURA, tag));
        } catch (Exception e) {
            System.out.println("Errore nella lettura del file:");
            System.out.println(e.getMessage());
        } finally {
            chiudiReader(xmlr);
        }
        return variabile;
    }

    /**
     * Scrive il file XML di default del registratore i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     */
    public static void scriviRegistratoreDefault (String areaCompetenza, int maxPartecipanti) {
        XMLStreamWriter xmlw = inizializzaWriter(pathRegistratore);
        try {
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
            System.out.println(e.getMessage());
        } finally {
            chiudiWriter(xmlw);
        }
    }

	/**
	 * Scrive il file XML delle date precluse del mese corrente e del successivo.
	 * @param path il path a cui trovare il file
	 * @param today la data odierna
	 * @param current l'array di date precluse
	 */
    public static void scriviDatePrecluse(String path, Date today, Date[] current) {
	    System.out.println("Inizio Scrittura");
	    DateFormatter form=new DateFormatter(df);
	    XMLStreamWriter wri = inizializzaWriter(path);
	    try{
		wri.writeStartElement("registro");
		wri.writeStartElement("meseCorrente");
		wri.writeCharacters(form.valueToString(today));
		wri.writeEndElement();
		wri.writeStartElement("datePrecluse");
		if(current.length!=0){
			wri.writeCharacters("[");
			for(int i=0;i<current.length-1;i++){
				wri.writeCharacters(form.valueToString(current[i])+",");
			}
			wri.writeCharacters(form.valueToString(current[current.length-1])+"]");
		}
		wri.writeEndElement();
		wri.writeEndElement();
	    }catch(Exception e){
		    System.out.println("Errore nella scrittura del log del registro delle date:");
		    e.printStackTrace();
	    }finally{
		    chiudiWriter(wri);
	    }
	    System.out.println("Fine scrittura");
    }

	/**
	 * Funzione che legge le date precluse per il mese corrente
	 * @param path il path al file XML da interrogare
	 * @return l'array di date precluse
	 */
    public static Date[] leggiDatePrecluse(String path){
	String[] aux={leggiVariabile(path,"datePrecluse")};
	Date[] res; 
	if(!aux[0].equals("")){aux=aux[0].substring(1,aux[0].length()-1).split(",");
		res=new Date[aux.length];
		for(int i=0;i<aux.length;i++){
			try{
				res[i]=df.parse(aux[i]);
			}catch(Exception e){
				System.out.println("Errore nella lettura della data:");
				e.printStackTrace();
			}
		}
	} 
    else {
	    res=new Date[0];
    }
	return res;
    }

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param path il percorso del file dove sono salvate le date precluse
     * @param data la data odierna
     */
	public static void cleanDates(String path,Date data){
		scriviDatePrecluse(path,data,new Date[0]);
	}
}
