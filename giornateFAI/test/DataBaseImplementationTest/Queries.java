package DataBaseImplementationTest;

import java.util.Map;

import DataBaseImplementation.QueryAccess;
import ServicesAPI.Configuratore;
import ServicesAPI.Fruitore;
import ServicesAPI.Utente;
import ServicesAPI.Volontario;

public enum Queries implements QueryAccess {

    //Login utenti e controllo password
    NICKNAME_UNIVOCO("SELECT Nickname FROM(SELECT * FROM dbingeswtest.configuratore UNION ALL SELECT * FROM dbingeswtest.volontario UNION ALL SELECT * FROM dbingeswtest.fruitori) AS utenti WHERE utenti.Nickname = ?"),
    PASSWORD_ACCESSO_CONFIGURATORE("SELECT Password, Salt FROM `dbingeswtest`.`configuratore` WHERE `Nickname` = ?"),
    PASSWORD_ACCESSO_VOLONTARIO("SELECT Password, Salt FROM `dbingeswtest`.`volontario` WHERE `Nickname` = ?"),
    PASSWORD_ACCESSO_FRUITORE("SELECT Password, Salt FROM `dbingeswtest`.`fruitori` WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_CONFIGURATORE("UPDATE `dbingeswtest`.`configuratore` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),
    CAMBIO_PASSWORD_VOLONTARIO("UPDATE `dbingeswtest`.`volontario` SET `Password` = ?,`Salt` = ? WHERE `Nickname` = ?"),
    
    //VisualizzatoreSQL e configuratore
    SELEZIONA_VOLONTARI("SELECT `Volontario Nickname`,Titolo FROM dbingeswtest.`volontari disponibili` join dbingeswtest.`Tipo di Visita` on `volontari disponibili`.`Tipo di Visita` = `Tipo di Visita`.`Codice Tipo di Visita` ORDER BY `Volontario Nickname`"),
    SELEZIONA_LUOGHI("SELECT * FROM `dbingeswtest`.`luogo`"),
    SELEZIONA_TIPI_VISITE("SELECT * FROM dbingeswtest.`tipo di visita`"),
    SELEZIONA_VISITE_ARCHIVIO("{call GetVisite(?)}"),
    VISITE_ASSOCIATE_VOLONTARIO("SELECT `Codice Tipo di Visita`, `Punto di Incontro`, `Titolo`, `Descrizione`, `Giorno di Inizio (periodo anno)`, `Giorno di Fine (periodo anno)`, `Ora di inizio`, `Durata`, `Min Partecipanti`, `Max Partecipanti` FROM dbingeswtest.`tipo di visita` JOIN dbingeswtest.`volontari disponibili` ON `tipo di visita`.`Codice Tipo di Visita` = `volontari disponibili`.`Tipo di Visita` WHERE `Volontario Nickname` = ?"),
    VISUALIZZA_ISTANZE_ISCRITTO("SELECT `Codice Archivio`,`Stato Visita`, titolo, Descrizione, `Data programmata`,`Ora di inizio`, `Necessita Biglietto`, `Numero iscritti` FROM dbingeswtest.`archivio visite attive` as av JOIN dbingeswtest.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` JOIN dbingeswtest.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` IN ('proposta', 'completa', 'confermata') AND Fruitore = ?"),
    ISTANZE_VISITE_DISPONIBILI("SELECT `Codice Archivio`, titolo, Descrizione, `Data programmata`,`Ora di inizio`, `Necessita Biglietto`, (MAX(`Max Partecipanti`) - COALESCE(SUM(`Numero iscritti`), 0)) AS `Posti Disponibili` FROM dbingeswtest.`archivio visite attive` as av JOIN dbingeswtest.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN dbingeswtest.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'proposta' AND `Codice Archivio` NOT IN ( SELECT `Visita iscritta` FROM dbingeswtest.`fruitori iscritti alle visite` WHERE Fruitore = ?) GROUP BY `Codice Archivio`"),
    VISUALIZZA_ISTANZE_CANCELLATE("SELECT `Codice Archivio`, titolo,`Data programmata` FROM dbingeswtest.`archivio visite attive` as av JOIN dbingeswtest.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` JOIN dbingeswtest.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'cancellata' AND Fruitore = ?"),
    VISUALIZZA_ISTANZE_VOLONTARIO("SELECT `Codice Archivio`, Titolo, COUNT(Fruitore) AS `Numero Iscrizioni`, COALESCE(SUM(`Numero iscritti`),0) AS Partecipanti, `Stato Visita`,  `Min Partecipanti`, `Max Partecipanti` FROM   dbingeswtest.`archivio visite attive` as av JOIN dbingeswtest.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN dbingeswtest.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Stato Visita` = 'confermata' AND `Volontario Selezionato` = ? GROUP BY `Codice Archivio`"),
    VISUALIZZA_ELENCO_ISCRITTI("SELECT Fruitore, `Codice prenotazione`, `Numero iscritti` FROM dbingeswtest.`fruitori iscritti alle visite` WHERE `Visita iscritta` = ?"),

    //RegistratoreSQL
    REGISTRA_CONFIGURATORE("INSERT into `dbingeswtest`.`configuratore` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_VOLONTARIO("INSERT into `dbingeswtest`.`volontario` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_FRUITORE("INSERT into `dbingeswtest`.`fruitori` (`Nickname`,`Password`, `Salt`) VALUES (?,?,?)"),
    REGISTRA_LUOGO("INSERT into `dbingeswtest`.`luogo` (`Nome`,`Descrizione`, `Indirizzo`) VALUES (?,?,?)"),
    REGISTRA_VISITA_ARCHIVIO("INSERT INTO `dbingeswtest`.`archivio visite attive` (`Codice Archivio`, `Stato Visita`, `Tipo di Visita`,`Volontario Selezionato`, `Data programmata`) VALUES (generaChiaveArchivio(), 'proponibile', ?, ?, ?);"),
    REGISTRA_TIPO_VISITA ("INSERT INTO `dbingeswtest`.`tipo di visita` (`Codice Tipo di Visita`,`Punto di Incontro`,`Titolo`,`Descrizione`,`Giorno di Inizio (periodo anno)`,`Giorno di Fine (periodo anno)`,`Ora di inizio`,`Durata`,`Necessita Biglietto`,`Min Partecipanti`,`Max Partecipanti`,`Configuratore referente`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"),
    ASSOCIA_VOLONTARIO_VISITA("INSERT INTO `dbingeswtest`.`volontari disponibili` (`Tipo di Visita`,`Volontario Nickname`) VALUES (?,?);"),
    ASSOCIA_GIORNI_SETTIMANA_VISITA("INSERT INTO `dbingeswtest`.`giorni programmabili delle visite` (`Tipo di Visita`, `Giorno della Settimana`) VALUES (?,?)"),
    ELIMINA_DATI_ORFANI("CALL dbingeswtest.EliminaDatiOrfani()"),
    RIMUOVI_LUOGO("DELETE FROM `dbingeswtest`.`luogo` WHERE Nome = ?"),
    RIMUOVI_VOLONTARIO("DELETE FROM `dbingeswtest`.`volontario` WHERE Nickname = ?"),
    RIMUOVI_TIPO_DI_VISITA("DELETE FROM `dbingeswtest`.`tipo di visita` WHERE Titolo = ?"), //se ci sono due visite omonime le elimina entrambe non essendo chiave primaria
    REGISTRA_ISTANZA_VISITA("INSERT INTO `dbingeswtest`.`archivio visite attive` (`Codice Archivio`, `Stato Visita`, `Tipo di Visita`, `Volontario Selezionato`, `Data programmata`) VALUES (?,?,?,?,?)"),
    OTTENI_INFO_PRE_ISCRIZIONE("SELECT `Max Partecipanti`, `Stato Visita`, COALESCE(SUM(`Numero iscritti`), 0) AS partecipanti FROM dbingeswtest.`archivio visite attive` as av JOIN dbingeswtest.`tipo di visita` as tdv ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` LEFT JOIN dbingeswtest.`fruitori iscritti alle visite` as fi ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Codice Archivio` = ? GROUP BY `Visita iscritta`"),
    REGISTRA_ISCRIZIONE("INSERT INTO `dbingeswtest`.`fruitori iscritti alle visite` (`Fruitore`, `Visita iscritta`, `Codice prenotazione`, `Numero iscritti`) VALUES (?,?,?,?)"),
    OTTENI_INFO_ISCRIZIONE("SELECT `Codice prenotazione`, `Stato Visita` FROM dbingeswtest.`fruitori iscritti alle visite` AS fi JOIN dbingeswtest.`archivio visite attive` AS av ON fi.`Visita iscritta` = av.`Codice Archivio` WHERE `Visita iscritta` = ? AND Fruitore = ?"),
    RIMUOVI_ISCRIZIONE("DELETE FROM dbingeswtest.`fruitori iscritti alle visite` WHERE `Codice prenotazione` = ?"),
    AGGIORNA_POST_ISCRIZIONE("UPDATE `dbingeswtest`.`archivio visite attive` SET `Stato Visita` = 'completa' WHERE `Codice Archivio` = ?"),
    AGGIORNA_POST_RIMOZIONE("UPDATE `dbingeswtest`.`archivio visite attive` SET `Stato Visita` = 'proposta' WHERE `Codice Archivio` = ?"),
    OTTIENI_STATO_ISTANZA("SELECT `Stato Visita` FROM dbingeswtest.`archivio visite attive` WHERE `Codice Archivio` = ?"),
    CALCOLA_POSTI_DISPONIBILI("SELECT COALESCE(`Max Partecipanti` - dbingeswtest.calcolaIscrittiAttuali(av.`Codice Archivio`), 0) AS `Posti Disponibili` FROM dbingeswtest.`tipo di visita` AS tdv JOIN dbingeswtest.`archivio visite attive` AS av ON av.`Tipo di Visita` = tdv.`Codice Tipo di Visita` WHERE `Codice Archivio` = ?"),

    //XMLDateDisponibili
    GIORNI_POSSIBILI_VOLONTARIO("SELECT `Codice Tipo di Visita`, `Giorno di Inizio (periodo anno)`, `Giorno di Fine (periodo anno)`, `Giorno della Settimana` FROM dbingeswtest.`tipo di visita` AS tpv JOIN dbingeswtest.`volontari disponibili` AS vd ON tpv.`Codice Tipo di Visita` = vd.`Tipo di Visita` JOIN dbingeswtest.`giorni programmabili delle visite` AS gpv ON tpv.`Codice Tipo di Visita` = gpv.`Tipo di Visita` WHERE vd.`Volontario Nickname` = ? ORDER BY `Codice Tipo di Visita`"),
    VISITE_PER_OGNI_VOLONTARIO("SELECT * FROM dbingeswtest.`volontari disponibili` ORDER BY `Tipo di Visita`"),
    GIORNI_POSSIBILI_VISITA("SELECT tv.`codice tipo di visita`,tv.`giorno di inizio (periodo anno)`,tv.`giorno di fine (periodo anno)`,gv.`giorno della settimana` FROM dbingeswtest.`giorni programmabili delle visite` AS gv JOIN dbingeswtest.`tipo di visita` AS tv ON tv.`codice tipo di visita`=gv.`tipo di visita`"),

    //gestione chiavi
    GENERA_CHIAVE_TIPO_VISITA("select dbingeswtest.generaChiaveTipoVisita() as maxCodice"),
    GENERA_CHIAVE_ARCHIVIO("select dbingeswtest.generaChiaveArchivio() as maxCodice");

    //CREAZIONE PIANO VISITE
    // per poter tornare il tipo di visita dato il volontario e il giorno della settimana
    private final String query;
    
    private Queries(String string) {
        this.query= string;
    }

    public String getQuery() {
        return query;
    }

    public static final Map<Class<? extends Utente>, QueryAccess> accessi = Map.of(
        Configuratore.class, Queries.PASSWORD_ACCESSO_CONFIGURATORE,
        Volontario.class, Queries.PASSWORD_ACCESSO_VOLONTARIO,
        Fruitore.class, Queries.PASSWORD_ACCESSO_FRUITORE
    );
    
}

