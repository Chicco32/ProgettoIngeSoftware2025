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

    //avviso di nickname già in uso
    public static void nicknameGiaInUso() {
        System.out.println("Nickname già in uso, inseriscine un altro!");
    }

    //chiede all'utente di inserire il campo area di competenza e di confermare la scelta
    public static String chiediAreaCompetenza() {
        boolean conferma = false;
        do{
            String area = InputDati.leggiStringaNonVuota("Inserisci la area di competenza della società");
            conferma = InputDati.yesOrNo("Confermi l'area di competenza inserita? [S/N]");
            if(conferma){
                return area;
            }
        }while(!conferma);
        return null;
    }

    //chiede all'utente di inserire il campo massimo partecipanti e di confermare la scelta
    public static int chiediMaxPartecipanti() {
        boolean conferma = false;
        do{
            int maxPartecipanti = InputDati.leggiIntero("Inserisci il numero massimo di partecipanti che lo stesso fruitore può avere", 1, 1000);
            conferma = InputDati.yesOrNo("Confermi il numero massimo di partecipanti inserito? [S/N]");
            if(conferma){
                return maxPartecipanti;
            }
        }while(!conferma);
        return 0;
    }

}
