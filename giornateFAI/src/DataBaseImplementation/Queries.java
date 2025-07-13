package DataBaseImplementation;

public enum Queries{

    //Login utenti e controllo password
    NICKNAME_UNIVOCO("SELECT Nickname FROM(SELECT * FROM {{db}}.configuratore UNION ALL SELECT * FROM {{db}}.volontario UNION ALL SELECT * FROM {{db}}.fruitori) AS utenti WHERE utenti.Nickname = ?"),
    PASSWORD_ACCESSO_CONFIGURATORE("SELECT Password, Salt FROM `{{db}}`.`configuratore` WHERE `Nickname` = ?"),
    PASSWORD_ACCESSO_VOLONTARIO("SELECT Password, Salt FROM `{{db}}`.`volontario` WHERE `Nickname` = ?"),
    PASSWORD_ACCESSO_FRUITORE("SELECT Password, Salt FROM `{{db}}`.`fruitori` WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_CONFIGURATORE("UPDATE `{{db}}`.`configuratore` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_VOLONTARIO("UPDATE `{{db}}`.`volontario` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),
    
    //VisualizzatoreSQL e configuratore
    SELEZIONA_VOLONTARI("SELECT `Volontario Nickname`,Titolo FROM {{db}}.`volontari disponibili` join {{db}}.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita` ORDER BY `Volontario Nickname`"),
    SELEZIONA_LUOGHI("SELECT * FROM `{{db}}`.`luogo`"),
    SELEZIONA_TIPI_VISITE("SELECT * FROM {{db}}.`tipo di visita`"),
    SELEZIONA_VISITE_ARCHIVIO("{call GetVisite(?)}"),
    VISITE_ASSOCIATE_VOLONTARIO("SELECT `Codice Tipo di Visita`, `Punto di Incontro`, `Titolo`, `Descrizione`, `Giorno di Inizio (periodo anno)`, `Giorno di Fine (periodo anno)`, `Ora di inizio`, `Durata`, `Min Partecipanti`, `Max Partecipanti` FROM {{db}}.`tipo di visita` JOIN {{db}}.`volontari disponibili` ON `tipo di visita`.`Codice Tipo di Visita` = `volontari disponibili`.`Tipo di Visita` WHERE `Volontario Nickname` = ?"),
    VISUALIZZA_ISTANZE_ISCRITTO("SELECT `Codice Archivio`,`Stato Visita`, titolo, Descrizione, `Data programmata`,`Ora di inizio`, `Necessita Biglietto`, `Numero iscritti` FROM {{db}}.`archivio visite attive` as av JOIN {{db}}.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` JOIN {{db}}.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` IN ('proposta', 'completa', 'confermata') AND Fruitore = ?"),
    ISTANZE_VISITE_DISPONIBILI("SELECT `Codice Archivio`, titolo, Descrizione, `Data programmata`,`Ora di inizio`, `Necessita Biglietto`, (MAX(`Max Partecipanti`) - COALESCE(SUM(`Numero iscritti`), 0)) AS `Posti Disponibili` FROM {{db}}.`archivio visite attive` as av JOIN {{db}}.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN {{db}}.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'proposta' AND `Codice Archivio` NOT IN ( SELECT `Visita iscritta` FROM {{db}}.`fruitori iscritti alle visite` WHERE Fruitore = ?) GROUP BY `Codice Archivio`"),
    VISUALIZZA_ISTANZE_CANCELLATE("SELECT `Codice Archivio`, titolo,`Data programmata` FROM {{db}}.`archivio visite attive` as av JOIN {{db}}.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` JOIN {{db}}.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'cancellata' AND Fruitore = ?"),
    VISUALIZZA_ISTANZE_VOLONTARIO("SELECT `Codice Archivio`, Titolo, COUNT(Fruitore) AS `Numero Iscrizioni`, COALESCE(SUM(`Numero iscritti`),0) AS Partecipanti, `Stato Visita`,  `Min Partecipanti`, `Max Partecipanti` FROM   {{db}}.`archivio visite attive` as av JOIN {{db}}.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN {{db}}.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'confermata' AND `Volontario Selezionato` = ? GROUP BY `Codice Archivio`"),
    VISUALIZZA_ELENCO_ISCRITTI("SELECT Fruitore, `Codice prenotazione`, `Numero iscritti` FROM {{db}}.`fruitori iscritti alle visite` WHERE `Visita iscritta` = ?"),

    //RegistratoreSQL
    REGISTRA_CONFIGURATORE("INSERT into `{{db}}`.`configuratore` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_VOLONTARIO("INSERT into `{{db}}`.`volontario` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_FRUITORE("INSERT into `{{db}}`.`fruitori` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_LUOGO("INSERT into `{{db}}`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES (?,?,?)"),
    REGISTRA_VISITA_ARCHIVIO("INSERT INTO `{{db}}`.`archivio visite attive` (`Codice Archivio`, `Stato Visita`, `Tipo di Visita`,`Volontario Selezionato`, `Data programmata`) VALUES (generaChiaveArchivio(), 'proponibile', ?, ?, ?);"),
    REGISTRA_TIPO_VISITA ("INSERT INTO `{{db}}`.`tipo di visita` (`Codice Tipo di Visita`,`Punto di Incontro`,`Titolo`,`Descrizione`,`Giorno di Inizio (periodo anno)`,`Giorno di Fine (periodo anno)`,`Ora di inizio`,`Durata`,`Necessita Biglietto`,`Min Partecipanti`,`Max Partecipanti`,`Configuratore referente`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"),
    ASSOCIA_VOLONTARIO_VISITA("INSERT INTO `{{db}}`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (?,?);"),
    ASSOCIA_GIORNI_SETTIMANA_VISITA("INSERT INTO `{{db}}`.`giorni programmabili delle visite` (`Tipo di Visita`, `Giorno della Settimana`) VALUES (?,?)"),
    ELIMINA_DATI_ORFANI("CALL {{db}}.EliminaDatiOrfani()"),
    RIMUOVI_LUOGO("DELETE FROM `{{db}}`.`luogo` WHERE Nome = ?"),
    RIMUOVI_VOLONTARIO("DELETE FROM `{{db}}`.`volontario` WHERE Nickname = ?"),
    RIMUOVI_TIPO_DI_VISITA("DELETE FROM `{{db}}`.`tipo di visita` WHERE Titolo = ?"), //se ci sono due visite omonime le elimina entrambe non essendo chiave primaria
    REGISTRA_ISTANZA_VISITA("INSERT INTO `{{db}}`.`archivio visite attive` (`Codice Archivio`, `Stato Visita`, `Tipo di Visita`, `Volontario Selezionato`, `Data programmata`) VALUES (?,?,?,?,?)"),
    OTTENI_INFO_PRE_ISCRIZIONE("SELECT `Max Partecipanti`, `Stato Visita`, COALESCE(SUM(`Numero iscritti`), 0) AS partecipanti FROM {{db}}.`archivio visite attive` as av JOIN {{db}}.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN {{db}}.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Codice Archivio` = ? GROUP BY `Visita iscritta`"),
    REGISTRA_ISCRIZIONE("INSERT INTO `{{db}}`.`fruitori iscritti alle visite` (`Fruitore`, `Visita iscritta`, `Codice prenotazione`, `Numero iscritti`) VALUES (?,?,?,?)"),
    OTTENI_INFO_ISCRIZIONE("SELECT `Codice prenotazione`, `Stato Visita` FROM {{db}}.`fruitori iscritti alle visite` AS fi JOIN {{db}}.`archivio visite attive` AS av ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Visita iscritta` = ? AND Fruitore = ?"),
    RIMUOVI_ISCRIZIONE("DELETE FROM {{db}}.`fruitori iscritti alle visite` WHERE `Codice prenotazione` = ?"),
    AGGIORNA_POST_ISCRIZIONE("UPDATE `{{db}}`.`archivio visite attive` SET `Stato Visita` = 'completa' WHERE `Codice Archivio` = ?"),
    AGGIORNA_POST_RIMOZIONE("UPDATE `{{db}}`.`archivio visite attive` SET `Stato Visita` = 'proposta' WHERE `Codice Archivio` = ?"),
    OTTIENI_STATO_ISTANZA("SELECT `Stato Visita` FROM {{db}}.`archivio visite attive` WHERE `Codice Archivio` = ?"),
    CALCOLA_POSTI_DISPONIBILI("SELECT COALESCE(`Max Partecipanti` - {{db}}.calcolaIscrittiAttuali(av.`Codice Archivio`), 0) AS `Posti Disponibili` FROM {{db}}.`tipo di visita` AS tdv JOIN {{db}}.`archivio visite attive` AS av ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` WHERE `Codice Archivio` = ?"),

    //XMLDateDisponibili
    GIORNI_POSSIBILI_VOLONTARIO("SELECT `Codice Tipo di Visita`, `Giorno di Inizio (periodo anno)`, `Giorno di Fine (periodo anno)`, `Giorno della Settimana` FROM {{db}}.`tipo di visita` AS tpv JOIN {{db}}.`volontari disponibili` AS vd ON tpv.`Codice Tipo di Visita` = vd.`Tipo di Visita` JOIN {{db}}.`giorni programmabili delle visite` AS gpv ON tpv.`Codice Tipo di Visita` = gpv.`Tipo di Visita` WHERE vd.`Volontario Nickname` = ? ORDER BY `Codice Tipo di Visita`"),
    VISITE_PER_OGNI_VOLONTARIO("SELECT * FROM {{db}}.`volontari disponibili` ORDER BY `Tipo di Visita`"),
    GIORNI_POSSIBILI_VISITA("SELECT tv.`codice tipo di visita`,tv.`giorno di inizio (periodo anno)`,tv.`giorno di fine (periodo anno)`,gv.`giorno della settimana` FROM {{db}}.`giorni programmabili delle visite` AS gv JOIN {{db}}.`tipo di visita` AS tv ON tv.`codice tipo di visita`=gv.`tipo di visita`"),

    //gestione chiavi
    GENERA_CHIAVE_TIPO_VISITA("select {{db}}.generaChiaveTipoVisita() as maxCodice"),
    GENERA_CHIAVE_ARCHIVIO("select {{db}}.generaChiaveArchivio() as maxCodice");

    //CREAZIONE PIANO VISITE
    // per poter tornare il tipo di visita dato il volontario e il giorno della settimana
    private final String query;
    private static final String DEFAULT_DATABASE = "dbingesw";
    private static String selectedDatabase = DEFAULT_DATABASE;
    
    private Queries(String rawQuery) {
        this.query= rawQuery;
    }

    public static void setDatabase(String dataBase) {
        selectedDatabase = dataBase;
    }

    public String getQuery() {
        return query.replace("{{db}}", selectedDatabase);
    }
    
}

