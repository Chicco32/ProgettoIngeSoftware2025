package giornateFAI;

import giornateFAI.*;
import java.sql.ResultSet;

import it.unibs.fp.mylib.InputDati;

public class CliUtente {

    //Interazione per la creazione di un nuovo Configuratore
    public static void creaNuovoConfiguratore() {
        System.out.println("Benvenuto! Per creare un nuovo Configuratore inserisci le seguenti informazioni:");
    }

    //Interazione di bentoranto al Configuratore
    public static void benvenutoConfiguratore() {
        System.out.println("Benvenuto Configuratore! Stai per accedere al backEnd di sistema");
        InputDati.leggiStringa("Premi invio per continuare");
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

    // Menu di interazione con il configuratore
    public static int menuConfiguratore() {
        boolean conferma = false;
        int scelta = 0;
        do {
            //codice demoniaco per invocare la pulizia dello schermo
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Scegli un'opzione dal menu:");
            System.out.println("1. Modifica max numero partecipanti");
            System.out.println("2. Introduzione nuovo tipo di visita");
            System.out.println("3. Introduzione nuovo volontario");
            System.out.println("4. Visualizza elenco volontari");
            System.out.println("5. Visualizza luoghi visitabili");
            System.out.println("6. Visualizza tipi di visite");
            System.out.println("7. Visualizza visite in archivio a seconda dello stato");
            System.out.println("8. Esci");
            
            scelta = InputDati.leggiIntero("Inserisci il numero della tua scelta", 1, 8);
            conferma = InputDati.yesOrNo("Confermi la scelta inserita? [S/N]");
            //codice demoniaco per invocare la pulizia dello schermo
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } while (!conferma);
        
        return scelta;
    }

    public static void visualizzaRisulati(ResultSet results, String tabella) {
        System.out.println("Visualizzazione dei risultati della tabella: " + tabella);
        try {

            //ottengo il numero di colonne
            int numColonne = results.getMetaData().getColumnCount();
            
            //stampo i nomi delle colonne
            for (int i = 1; i <= numColonne; i++) {
                System.out.print(results.getMetaData().getColumnName(i) + "\t");
            }
            System.out.println("\n");

            //stampo i risultati
            while (results.next()) {
                for (int i = 1; i <= numColonne; i++) {
                    System.out.print(results.getString(i) + "\t");
                }
                System.out.println("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Fine tabella");
        InputDati.leggiStringa("Premi invio per continuare");
    }

    public static void visualizzaArchivioVisite(StatiVisite stato) {
        System.out.println("Visualizzazione delle visite con stato: " + stato);
    }

    public static StatiVisite chiediStatoVisita() {
        System.out.println("Stati possibili su cui filtrare le visite: ");
        System.out.println("1. Proposta");
        System.out.println("2. Completa");
        System.out.println("3. Annullata");
        System.out.println("4. Confermata");
        System.out.println("5. Effettuata");
        int scelta = InputDati.leggiIntero("Inserisci il numero dello stato da visualizzare", 1, 5);
        StatiVisite stato = null;
        switch (scelta) {
            case 1:
                stato = StatiVisite.PROPOSTA;
                break;
            case 2:
                stato = StatiVisite.COMPLETA;
                break;
            case 3:
                stato = StatiVisite.ANNULLATA;
                break;
            case 4:
                stato = StatiVisite.CONFERMATA;
                break;
            case 5:
                stato = StatiVisite.EFFETTUATA;
                break;
            default:
                break;
        }
        return stato;
    }

}
