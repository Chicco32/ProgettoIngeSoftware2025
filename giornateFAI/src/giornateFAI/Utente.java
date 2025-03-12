package giornateFAI;

import java.sql.Connection;

public abstract class Utente {

    private String ruolo; //Configuratore, Fruitore o Volontario
    private String nickname;
    private Boolean PrimoAccesso;
    protected Visualizzatore visualizzatore;
    

    public Utente(boolean PrimoAccesso,Connection conn) {
        this.nickname = null; 
        this.PrimoAccesso = PrimoAccesso;
        this.visualizzatore = new Visualizzatore(conn);
    }
    
    protected void setRuolo(String ruolo){
        this.ruolo = ruolo;
    }

    public String getRuolo() {
        return this.ruolo;
    }

    public boolean isPrimoAccesso() {
        return this.PrimoAccesso;
    }

    protected void setPrimoAccesso(boolean PrimoAccesso) {
        this.PrimoAccesso = PrimoAccesso;
    }

    public String getNickname() {
        return this.nickname;
    }

    protected void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public abstract void registrati();

    /**
     * Questo metodo permette di controllare se una tabella del database è vuota. In particolare questa funzione permette l'interazione
     * fra L'Utente e il database per controllare se una tabella è vuota.
     * A seconda del tipo di utente può avere accesso ad alcune tabelle anzichè ad altre.
     * Il metodo ritornerà true se la tabella è vuota, false altrimenti.
     * IL metodo si occupa anche della gesione degli errori e delle eccezioni.
     * @param tabella la tabella da controllare
     * @return true se la tabella è vuota, false altrimenti
     */
    public abstract boolean controllaDBVuoti (String tabella);

}
