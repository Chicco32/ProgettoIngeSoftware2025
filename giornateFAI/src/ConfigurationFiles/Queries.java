package ConfigurationFiles;

public enum Queries {

    SELEZIONA_VOLONTARI("SELECT `Tipo di Visita`,`Volontario Nickname`,`Titolo` FROM dbingesw.`volontari disponibili` join dbingesw.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita`;"),
    SELEZIONA_LUOGHI("SELECT * FROM `dbingesw`.`luogo`"),
    SELEZIONA_TIPI_VISITE("SELECT * FROM dbingesw.`tipo di visita`"),
    SELEZIONA_VISITE_ARCHIVIO("{call GetVisite(?)}");

    private final String query;
    
    private Queries(String string) {
        this.query= string;
    }

    public String getQuery() {
        return query;
    }
    
}
