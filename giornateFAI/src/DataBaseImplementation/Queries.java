package DataBaseImplementation;

public enum Queries {

    //VisualizzatoreSQL e configuratore
    SELEZIONA_VOLONTARI("SELECT `Tipo di Visita`,`Volontario Nickname`,`Titolo` FROM dbingesw.`volontari disponibili` join dbingesw.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita`;"),
    SELEZIONA_LUOGHI("SELECT * FROM `dbingesw`.`luogo`"),
    SELEZIONA_TIPI_VISITE("SELECT * FROM dbingesw.`tipo di visita`"),
    SELEZIONA_VISITE_ARCHIVIO("{call GetVisite(?)}"),
    
    //RegistratoreSQL
    REGISTRA_CONFIGURATORE("INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`) VALUES (?,?)"),
    REGISTRA_VOLONTARIO("INSERT into `dbingesw`.`volontario` (`Nickname`,`Password`) VALUES (?,?)"),
    REGISTRA_LUOGO("INSERT into `dbingesw`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES (?,?,?)"),
    REGISTRA_TIPO_VISITA ("INSERT INTO `dbingesw`.`tipo di visita` (`Codice Tipo di Visita`,`Punto di Incontro`,`Titolo`,`Descrizione`,`Giorno di Inizio (periodo anno)`,`Giorno di Fine (periodo anno)`,`Ora di inizio`,`Durata`,`Necessita Biglietto`,`Min Partecipanti`,`Max Partecipanti`,`Configuratore referente`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"),
    ASSOCIA_VOLONTARIO_VISITA("INSERT INTO `dbingesw`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (?,?);");

    private final String query;
    
    private Queries(String string) {
        this.query= string;
    }

    public String getQuery() {
        return query;
    }
    
}
