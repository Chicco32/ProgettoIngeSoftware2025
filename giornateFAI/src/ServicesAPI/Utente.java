package ServicesAPI;


public abstract class Utente {

    private String ruolo; //Configuratore, Fruitore o Volontario
    private String nickname;
    private Boolean PrimoAccesso;
    protected Visualizzatore visualizzatore;
    protected Calendario calendario;
    

    public Utente(boolean PrimoAccesso, String nickname, Visualizzatore visualizzatore) {
        this.nickname = null;
        this.calendario = new Calendario(); 
        this.PrimoAccesso = PrimoAccesso;
        this.nickname = nickname;
        this.visualizzatore = visualizzatore;
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

    public Visualizzatore getVisualizzatore() {
        return this.visualizzatore;
    }

    public Calendario getCalendario() {
        return this.calendario;
    }

}
