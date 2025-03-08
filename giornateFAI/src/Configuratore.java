import java.sql.Connection;

public class Configuratore extends Utente {
    
    private Registratore registratore;

    public Configuratore(boolean PrimoAccesso, Connection conn) {
        super(PrimoAccesso,conn);
        this.setRuolo("Configuratore");
        this.registratore = new Registratore(conn);
    }

    public void registrati() {
        CliUtente.creaNuovoConfiguratore();
        do {
            // Chiede il nickname e la password finche' non vengono inseriti correttamente
            this.setNickname(CliUtente.chiediNickname());
        } while (!registratore.registraNuovoConfiguratore(this.getNickname(), CliUtente.chiediPassword()));
        this.setPrimoAccesso(false);
    }

    public void inserisciAreaCompetenza() {
        String areaCompetenza = CliUtente.chiediAreaCompetenza();
        registratore.modificaAreaCompetenza(areaCompetenza);
    }

    public void inserisciMaxPartecipanti() {
        int maxPartecipanti = CliUtente.chiediMaxPartecipanti();
        registratore.modificaMaxPartecipanti(maxPartecipanti);
    }

    /**
     * Questo metodo permette di controllare se una tabella del database è vuota. In particolare questa funzione permette l'interazione
     * fra il Configuratore e il database per controllare se una tabella è vuota.
     * @param tabella la tabella da controllare accetta come valore "Luogo" o "Tipo di Visita"
     * @return true se la tabella è vuota, false altrimenti
     */
    public boolean controllaDBVuoti(String tabella) {
        try {
            switch (tabella) {
                case "Luogo":
                    return this.visualizzatore.DBLuoghiIsEmpty();
                case "Tipo di Visita":
                    return this.visualizzatore.DBTipiVisiteIsEmpty();
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void visualizzaVolontari() {
        this.visualizzatore.visualizzaVolontari();
    }

    public void visualizzaLuoghiDaVisitare() {
        this.visualizzatore.visualizzaLuoghiDaVisitare();
    }
    
    public void chiediStatoDaVisualizzare() {
        this.visualizzatore.visualizzaVisite(CliUtente.chiediStatoVisita());
    }
}
