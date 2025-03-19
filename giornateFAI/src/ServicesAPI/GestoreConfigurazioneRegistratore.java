package ServicesAPI;

public interface GestoreConfigurazioneRegistratore extends GestoreFilesConfigurazione {

    /**
     * Scrive il file di default del registratore con i valori inseriti.
     * @param maxPartecipanti il numero massimo di partecipanti che lo stesso Fruitore può inserire
     * @param areaCompetenza l'area geografica di competenza della società
     */
    public void scriviRegistratoreDefault(String areaCompetenza, int maxPartecipanti);

}
