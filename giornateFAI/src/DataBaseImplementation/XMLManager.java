package DataBaseImplementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;


/**
 * Classe per la gestione dei file XML, provvede gli strumenti per la lettura e la scrittura di file XML
 * e per la gestione dei file stessi. Per usare la libreria anziché interagire direttamente con il file XML
 * si interagisce con questa classe che si occuperà di gestire i dati contenuti in esso.
 */
public class XMLManager{

    protected String path;
    protected XMLStreamReader xmlr;
    protected XMLStreamWriter xmlw;

    public static final String INIZIO_LETTURA = "Inizio la lettura di %s";
    public static final String FINE_LETTURA = "Ho finito di leggere %s";
    public static final String INIZIO_SCRITTURA = "Inizio la scrittura di %s";
    public static final String FINE_SCRITTURA = "Ho finito di scrivere %s";


    /**
     * Costruttore del XMLManager. Per utilizzare le funzioni di lettura e scrittura del manager bisogna istanza un oggetto della classe 
     * che salverà il percorso del file a cui esso fa riferimento.
     * @param path il percorso del file da modificare o leggere
     */
    public XMLManager (String path) {
        this.path = path;
    }

    /**
     * Inizializza un reader per il file XML
     * @param path il percorso del file XML
     */
    protected void inzializzaReader() throws FileNotFoundException {
        XMLInputFactory xmlif = null;
        XMLStreamReader newReader = null;
        try {
            xmlif = XMLInputFactory.newInstance();
            FileInputStream fis = new FileInputStream(this.path);
            newReader = xmlif.createXMLStreamReader(this.path, fis);
        } 
        catch (XMLStreamException | FactoryConfigurationError e) {
            System.out.println("Errore nell'inizializzazione del reader:");
            System.out.println(e.getMessage());
        }
        this.xmlr = newReader;
    }


    /**
     * Inizializza un writer per il file XML. Attenzione che esso evoca la sovrascrittura del file, perciò
     * deve essere invocato solo appena è necessaria la scrittura del file
     * @param path il percorso del file XML
     */
    protected void inizializzaWriter() throws FileNotFoundException {
        XMLOutputFactory xmlof = null;
        XMLStreamWriter newWriter = null;
        try {
            xmlof = XMLOutputFactory.newInstance();
            FileOutputStream fos = new FileOutputStream(this.path);
            newWriter = xmlof.createXMLStreamWriter(fos, "utf-8");
        } catch (FactoryConfigurationError | XMLStreamException e) {
            System.out.println("Errore nell'inizializzazione del writer:");
		e.printStackTrace();
        }
        this.xmlw = newWriter;
    }

    /**
     * Chiude il reader
     * @param xmlr il reader da chiudere
     */
    protected void chiudiReader() {
        try {
            this.xmlr.close();
        } catch (XMLStreamException e) {
            System.out.println("Errore nella chiusura del reader:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Chiude il writer
     * @param xmlw il writer da chiudere
     */
    protected void chiudiWriter() {
        try {
            this.xmlw.writeEndDocument();
            this.xmlw.flush();
            this.xmlw.close();
        } catch (XMLStreamException e) {
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
     * altrimenti legge la prima occorrenza disponibile nel file.
     * @param path il percorso del file XML
     * @param tag il tag associato alla variabile da leggere
     * @return Il valore della variabile letta nel file XML
     */
    public String leggiVariabile (String tag) throws FileNotFoundException {
        inzializzaReader();
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
        }
        chiudiReader();
        return variabile;
    }

    public String getPath() {
		return this.path;
	}

}
