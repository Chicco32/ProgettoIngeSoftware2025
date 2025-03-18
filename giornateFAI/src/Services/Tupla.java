package Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe che implementa il DAO per poter raggruppare e passare in maniera veloce tutti gli attributi di una particolare tupla estratta.
 * 
 * @see DTObject
 */
public class Tupla implements DTObject {

    //alcuni formati di campi gia fatti per questa applicazione, comunque si possono inserire anche formati personalizzati
    public static final String[] FORMATO_UTENTE = {"Nickname", "Password"};
    public static final String[] FORMATO_LUOGO = {"Nome", "Descrizione", "Indirizzo"};
    public static final String[] FORMATO_TIPO_VISITA = {
        "Codice Tipo di Visita", "Punto di Incontro","Titolo", "Descrizione", "Periodo anno", "Ora di inizio", "Durata", "Necessita Biglietto",
        "Min Partecipanti", "Max Partecipanti", "Configuratore referente"};

    private String tabellaOrigine;
    private int numCampi;
    private String[] campi;
    private HashMap<Integer, Object> valori;


    /**
     * Costruttore di un oggetto Tupla che crea una tupla vuota da riempire.
     * @param tabella il nome della tabella a cui riferisce o un nome identificativo per riconoscere a che relaiozne si riferisce
     * @param campi la lista di attributi che la tupla dovrà salvare
     */
    public Tupla (String tabella, String[] campi) {
        this.tabellaOrigine = tabella;
        this.numCampi = campi.length;
        this.campi = campi;
        this.valori = new HashMap<>();
        //iniziliazza una tupla vuota, la chiave è la posizione del corrispondente attributo
        for (int i = 0; i<numCampi; i++) valori.put(i, null);
    }

    public List<Object> getAttributi() {
        List<Object> aux = new ArrayList<Object>();
        for (int i = 0; i<numCampi; i++) aux.add(valori.get(i));
        return aux;

    }

    public List<String> getCampi() {
        List<String> aux = new ArrayList<String>();
        for (String campo : campi) aux.add(campo);
        return aux;
    }

    private int trovaIndice (String campo) throws IllegalArgumentException {
        if (campo == null) {
            throw new IllegalArgumentException ();
        }
    
        for (int i = 0; i < numCampi; i++) {
            if (campi[i].equals(campo)) { // Confronto esatto della stringa
                return i;
            }
        }
        throw new IllegalArgumentException(); // Se la stringa non è trovata
    }

    
    public Object getAttributeValue(String campo) throws IllegalArgumentException {
        int indice = trovaIndice(campo);
        return valori.get(indice);
    }

    
    public void impostaValore(Object valore, String campo) throws IllegalArgumentException {
        int indice = trovaIndice(campo);
        valori.put(indice, valore);
    }

    public String getNomeDAO() {
        return this.tabellaOrigine;
    }

    public int getNumeroCampi() {
        return numCampi;
    }

    public boolean eVuoto() {
        boolean vuoto = true;
        for (int i = 0; i<numCampi; i++) {
            if(valori.get(i) !=  null) {
                vuoto = false;
                break;
            }
        } 
        return vuoto;
    }

}
