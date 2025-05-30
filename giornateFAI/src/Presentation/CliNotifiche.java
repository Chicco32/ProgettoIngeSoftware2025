package Presentation;

public enum CliNotifiche {

    CONNESSIONE_RIUSCITA("Connessione al database riuscita, database operativo e raggiungibile"),
    BENVENUTO("Benvenuto nel servizio di gestione delle visite guidate di GiornateFAI, per prima cosa esegui l'accesso \n o digita il nickname 'new' per registrare un nuovo fruitore:"),
    BENVENUTO_NUOVO_CONFIGURATORE("Per creare un nuovo Configuratore inserisci le seguenti informazioni:"),
    BENVENUTO_NUOVO_VOLONTARIO("Volontario questo è il tuo primo accesso, ti chiediamo cortesemente inserisci le seguenti informazioni:"),
    BENVENUTO_NUOVO_FRUITORE("Ciao! Siamo felici che hai deciso di iscriverti sulla nostra piattaforma! Per iniziare a conoscerci meglio, ti chiediamo di inserire le seguenti informazioni:"),
    CAMBIO_PASSWORD_CORRETTAMENTE_REGISTRATO("La nuova password è stata salvata, ricordatela da qui in avanti per accedere"),
    CONFIGURATORE_CORRETTAMENTE_REGISTRATO ("Configuratore correttamente registrato"),
    VOLONTARIO_CORRETTAMENTE_REGISTRATO ("Volontario correttamente registrato"),
    FRUITORE_CORRETTAMENTE_REGISTRATO("Sei stato correttamente registrato!"),
    NECESSARIO_ABBINARE_VOLONTARIO("Necessario abbinare una visita al volontario registrato"),
    LUOGO_CORRETTAMENTE_REGISTRATO ("Luogo correttamente registrato"),
    VISITA_CORRETTAMENTE_REGISTRATA ("Visita correttamente registrata"),
    VISITA_CORRETTAMENTE_RIMOSSA("Visita correttamente rimossa"),
    VOLONTARIO_CORRETTAMENTE_ASSOCIATO("Volontario correttamente associato"),
    LOGIN_RIUSCITO("Login riuscito!"),
    CREDENZIALI_ERRATE("Credenziali errate!"),
    NICKNAME_GIA_USATO("Nickname già in uso, inseriscine un altro!"),
    NOME_LUOGO_GIA_USATO("Questo luogo risulta gia inserito o ve nè un'altro con lo stesso nome, inseriscine un altro!"),
    VOLONTARIO_GIA_ABBINATO_VISITA("Questo volontario è gia stato registrato per questo tipo di visita"),
    GIORNO_CONFIGURAZIONE("Oggi è il giorno della configurazione dei prossimi mesi"),
    ISCRIZIONE_IMPOSSIBILE("l'iscrizione con questi parametri non è possibile, controlla che il numero di partecipanti non superi il massimo consentito o che la visia sia disponibile"),
    RIMOZIONE_IMPOSSIBILE("La rimozione è stata annullata, controlla di non aver selezionato una visita gia confermata o controlla che il codice inserito sia corretto"),
    ERRORE_REGISTRAZIONE("Errore nella registrazione!"),
    ERRORE_CONNESSIONE ("Errore nella connessione col DB, controlla che sia acceso e che l'indirizzo sia giusto"),
    //ERRORE_DRIVER("Driver non trovato! Controlla che il driver sia correttamente installato"),
    //ERRORE_INSERIMENTO("Attenzione hai inserito un valore non valido"),
    //ERRORE_QUERY("Errore durante l'inserimento della query, potrebbe essere che non ha trovato la tabella o i parametri sono errati"),
    ERRORE_LETTURA_FILE ("Errore nella lettura del file"),
    ERRORE_SCRITTURA_FILE ("Errore nella scrittura del file"),
    ERRORE_CREAZIONE_FILE("Errore nella creazione del file"),
    ERRORE_ACCESSO("Errore durante l'accesso"), 
    ERRORE_NUMERO_PARTECIPANTI("Il numero di partecipanti è maggiore del massimo consentito");




    private final String nome;
    
    private CliNotifiche(String string) {
        this.nome = string;
    }

    public String getNome() {
        return nome;
    }

    public static void avvisa(CliNotifiche notifica) {
        System.out.println(notifica.getNome());
    }
}
