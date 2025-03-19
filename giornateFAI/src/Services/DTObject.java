package Services;
import java.util.List;

public interface DTObject {

    /**
     * Permette di ottenere una lista con tutti gli attributi
     * @return la lista con i valori degli attributi
     */
    public List<Object> getValori();

    /**
     * Permette di ottenere una lista con tutti i campi
     * @return la lista con i valori degli attributi
     */
    public List<String> getCampi();
    
    /**
     * ritorna il valore salvato corrispondente all'attributo inserito
     * @param campo il nome dell'attributo da restituire 
     * @return
     */
    public Object getValoreCampo(String campo);

    /**
     * Permette di impostare il valore di un attributo dato il nome del campo
     * @param valore
     * @param campo
     */
    public void impostaValore(Object valore, String campo); 

    /**
     * ritorna il nome dell'oggetto, ovvero il nome della tabella di cui è una tupla.
     * @return
     */
    public String getNomeDAO();

    /**
     * Ritorna il numero di campi che quell'oggetto possiede
     * @return
     */
    public int getNumeroCampi();

    /**
     * Controlla se ha dei valori, in particolare controlla se possiede almeno un campo inserito
     * @return true se tutti i campi sono vuoti, false altrimenti
     */
    public boolean eVuoto();
}
