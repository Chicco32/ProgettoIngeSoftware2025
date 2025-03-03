import it.unibs.fp.mylib.InputDati;

public class CliUtente {

    //Interazione per la creazione di un nuovo Configuratore
    public static void creaNuovoConfiguratore() {
        System.out.println("Benvenuto! Per creare un nuovo Configuratore inserisci le seguenti informazioni:");
    }

    //Interazione di bentoranto al Configuratore
    public static void benvenutoConfiguratore() {
        System.out.println("Benvenuto! Questo è il login per il Configuratore per accedere al backEnd di sistema");
    }

    //Interazione per chiedere il nickname
    public static String chiediNickname() {
        return InputDati.leggiStringaNonVuota("Inserisci il tuo Nickname");
    }

    //Interazione per chiedere una password
    public static String chiediPassword() {
        return InputDati.leggiStringaNonVuota("Inserisci la tua Password");
    }

    //avviso di reindirizzamento alla creaizone del profilo
    public static void creaProfilo() {
        System.out.println("Ti rimandiamo alla creazione del tuo profilo!");
    }

    //avviso di login riuscito
    public static void loginRiuscito() {
        System.out.println("Login riuscito!");
    }

    //avviso di credenziali errate
    public static void credenzialiErrate() {
        System.out.println("Credenziali errate!");
    }

}
