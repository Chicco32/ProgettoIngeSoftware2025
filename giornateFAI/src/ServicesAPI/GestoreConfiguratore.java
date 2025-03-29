package ServicesAPI;

import java.io.FileNotFoundException;

public interface GestoreConfiguratore extends GestoreFilesConfigurazione {

    /**
     * Scrive il file di default del registratore con i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     * @throws FileNotFoundException se il file non esiste o ci sono problemi di accesso
     */
    public void scriviRegistratoreDefault(String areaCompetenza, int maxPartecipanti) throws FileNotFoundException;

}
