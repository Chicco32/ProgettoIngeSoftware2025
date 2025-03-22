package DataBaseImplementation;

public enum Queries {

    //Login utenti e controllo password
    NICKNAME_UNIVOCO("SELECT Nickname FROM(SELECT * FROM dbingesw.configuratore UNION ALL SELECT * FROM dbingesw.volontario UNION ALL SELECT * FROM dbingesw.fruitori) AS utenti WHERE utenti.Nickname = ?"),
    PASSWORD_ACCESSO_CONFIGURATORE("SELECT Password, Salt FROM `dbingesw`.`configuratore` WHERE `Nickname` = ?"),
    PASSWORD_ACCESSO_VOLONTARIO("SELECT Password, Salt FROM `dbingesw`.`volontario` WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_CONFIGURATORE("UPDATE `dbingesw`.`configuratore` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_VOLONTARIO("UPDATE `dbingesw`.`volontario` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),

    //VisualizzatoreSQL e configuratore
    SELEZIONA_VOLONTARI("SELECT `Tipo di Visita`,`Volontario Nickname`,`Titolo` FROM dbingesw.`volontari disponibili` join dbingesw.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita`;"),
    SELEZIONA_LUOGHI("SELECT * FROM `dbingesw`.`luogo`"),
    SELEZIONA_TIPI_VISITE("SELECT * FROM dbingesw.`tipo di visita`"),
    SELEZIONA_VISITE_ARCHIVIO("{call GetVisite(?)}"),
    VISITE_ASSOCIATE_VOLONTARIO("SELECT `Codice Tipo di Visita`, `Punto di Incontro`, `Titolo`, `Descrizione`, `Giorno di Inizio (periodo anno)`, `Giorno di Fine (periodo anno)`, `Ora di inizio`, `Durata`, `Min Partecipanti`, `Max Partecipanti` FROM dbingesw.`tipo di visita` JOIN dbingesw.`volontari disponibili` ON `tipo di visita`.`Codice Tipo di Visita` = `volontari disponibili`.`Tipo di Visita` WHERE `Volontario Nickname` = ?"),
    

    //RegistratoreSQL
    REGISTRA_CONFIGURATORE("INSERT into `dbingesw`.`configuratore` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_VOLONTARIO("INSERT into `dbingesw`.`volontario` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_LUOGO("INSERT into `dbingesw`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES (?,?,?)"),
    REGISTRA_VISITA_ARCHIVIO("INSERT INTO `dbingesw`.`archivio visite attive` (`Codice Archivio`, `Stato Visita`, `Tipo di Visita`,`Volontario Selezionato`, `Data programmata`) VALUES (generaChiaveArchivio(), 'proponibile', ?, ?, ?);"),
    REGISTRA_TIPO_VISITA ("INSERT INTO `dbingesw`.`tipo di visita` (`Codice Tipo di Visita`,`Punto di Incontro`,`Titolo`,`Descrizione`,`Giorno di Inizio (periodo anno)`,`Giorno di Fine (periodo anno)`,`Ora di inizio`,`Durata`,`Necessita Biglietto`,`Min Partecipanti`,`Max Partecipanti`,`Configuratore referente`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"),
    ASSOCIA_VOLONTARIO_VISITA("INSERT INTO `dbingesw`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (?,?);"),
    ASSOCIA_GIORNI_SETTIMANA_VISITA("INSERT INTO `dbingesw`.`giorni programmabili delle visite` (`Tipo di Visita`, `Giorno della Settimana`) VALUES (?,?)"),

    //XMLDateDisponibili
    GIORNI_POSSIBILI_VOLONTARIO("SELECT gpv.`Giorno della Settimana` FROM dbingesw.`giorni programmabili delle visite` AS gpv JOIN dbingesw.`volontari disponibili` AS vd ON vd.`Tipo di Visita` = gpv.`Tipo di Visita` WHERE vd.`Volontario Nickname` = ?"),

    //gestione chiavi
    GENERA_CHIAVE_TIPO_VISITA("select dbingesw.generaChiaveTipoVisita() as maxCodice"),
    GENERA_CHIAVE_ARCHIVIO("select dbingesw.generaChiaveArchivio() as maxCodice");

    private final String query;
    
    private Queries(String string) {
        this.query= string;
    }

    public String getQuery() {
        return query;
    }
    
}
