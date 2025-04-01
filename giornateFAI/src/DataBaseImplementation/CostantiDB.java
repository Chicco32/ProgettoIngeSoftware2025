package DataBaseImplementation;

/**
 * Classe contenitore con tutti i nomi di schemi e chiavi per poter accedere ai valori corretti durante le query senza ambiguità.
 * Questa classe permetee anche di evitare conflitti in caso di cambi di nomi all'interno del database.
 */
public enum CostantiDB {

    //schema
    DATABASE("dbingesw"),
    //tabelle
    TIPO_VISITA("`tipo di visita`"),
    ARCHIVIO_VISITE_ATTIVE("`archivio visite attive`"),
    ARCHIVIO_STORICO_VISITE("`archivio storico visite`"),
    LUOGHI("luogo"),
    GIORNI_PROGRAMMABILI_VISITE("`giorni programmabili delle visite`"),
    GIORNI_SETTIMANA("`giorni della settimana`"),
    CONFIGURATORE("configuratore"),
    VOLONTARIO("volontario"),
    FRUITORE("fruitori"),
    VOLONTARI_DISPONIBILI("`volontari disponibili`"),
    FRUITORI_ISCRITTI("`fruitori iscritti alle visite`"),

    //chiavi primarie
    CHIAVE_CONFIGURATORE("Nickname"),
    CHIAVE_VOLONTARIO("Nickname"),
    CHIAVE_FRUITORE("Nickname"),
    CHIAVE_TIPO_VISITA("`Codice Tipo di Visita`"),
    CHIAVE_LUOGO("Nome"),
    CHIAVE_ARCHIVIO_VISITE("`Codice Archivio`"),
    CHIAVE_VOLONTARI_DISPONIBILI_VOLONTARIO("`Volontario Nickname`"),
    CHIAVE_VOLONTARI_DISPONIBILI_VISITA("`Tipo di Visita`"),
    CHIAVE_FRUITORI_ISCRITTI_FRUITORE("`Fruitore`"),
    CHIAVE_FRUITORI_ISCRITTI_VISITA("`Visita iscritta`");


    private final String nome;
    
    private CostantiDB(String string) {
        this.nome = string;
    }

    public static CostantiDB fromString(String valore) throws IllegalArgumentException {
        for (CostantiDB costante : CostantiDB.values()) {
            if (costante.nome.equals(valore)) {
            return costante;
            }
        }
        throw new IllegalArgumentException("Valore non valido: " + valore);
    }

    public String getNome() {
        return nome;
    }

}
