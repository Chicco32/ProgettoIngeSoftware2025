package ServicesAPI;
import java.util.Date;

public interface GestoreDateDisponibili extends GestoreFilesConfigurazione {

    public void registraDateDisponibili(Date today, Date[] dateDisponibli, String nomeVolontario);

    /**
	 * Funzione che legge le date precluse per il mese corrente
	 * @return l'array di date precluse
	 */
    public Date[] leggiDateDisponibili(String nomeVolontario);

    /**
     * Funzione che resetta le date precluse salvate sul file
     * @param data la data odierna
     */
	public void cleanDates(Date data, String nomeVolontario);

}
