
/**
 * Questa classe rappresenta il back-end del sistema, ovvero la parte che si occupa di gestire i dati e le operazioni
 * che il configuratore può fare sul sistema. Questa classe si occupa dell'interazione con l'utente e delega le operazioni
 * all'utente stesso in base alle scelte effettuate e alle azioni che l'utente ha i diritti di fare.
 * 
 * @see Utente
 * @see Configuratore
 */
public class BackEnd {

    private Utente utente;

    public BackEnd(Utente utente) {
        this.utente = utente;
        
    }

    /**
     * Questa funzione servirà da menu in cui il configuratore potra decidere a che opzione accedere
     *   TODO da fare
     */
    public void menuConfiguratore() {

        //posso fare un casting sicuro a configuratore dopo il login
        Configuratore configuratore = (Configuratore) this.utente;
        Boolean continua = true;

        //Se è la prima volta che il configuratore accede al db dei luoghi e non ci sono dati
        if(configuratore.controllaDBVuoti("Luogo")) {
            //prima chiede all'utente di inserire l'area di competenza
            configuratore.inserisciAreaCompetenza();
            //configuratore.inserisciNuoviLuoghi();
        }
        
        //Se è la prima volta che il configuratore accede al db delle visite e non ci sono dati
        if (configuratore.controllaDBVuoti("Tipo di Visita")) {
            //prima chiede all'utente di inserire il numero massimo di partecipanti
            configuratore.inserisciMaxPartecipanti();
            //configuratore.inserisciNuoveVisite();
        }

        while (continua) {
            
            /* Completamente da fare e moidifcare il menu. di seguito la lista nel cli
            1. Modifica max numero partecipanti
            2. Introduzione nuovo tipo di visita
            3. Introduzione nuovo volontario
            4. Visualizza elenco volontari
            5. Visualizza luoghi visitabili
            6. Visualizza tipi di visite
            7. Visualizza visite in archivio a seconda dello stato
            8. Esci"
             */
            
            int scelta = CliUtente.menuConfiguratore();
            switch (scelta) {
                case 1:
                configuratore.inserisciMaxPartecipanti();
                    break;
                case 4:
                    configuratore.visualizzaVolontari();
                    break;
                case 5:
                    configuratore.visualizzaLuoghiDaVisitare();
                    break;
                case 7:
                    configuratore.chiediStatoDaVisualizzare();
                    break;
                //per per uscire
                case 8:
                    continua = false;
                    break;
                default:
                    continua = false;
                    break;
            }

        }
    }
    
}
