package ServicesAPI;


public abstract class Utente {

    private String ruolo; //Configuratore, Fruitore o Volontario
    private String nickname;
    private Boolean PrimoAccesso;
    protected Calendario calendario;
    

    public Utente(boolean PrimoAccesso, String nickname) {
        this.nickname = null;
        this.calendario = new Calendario(); 
        this.PrimoAccesso = PrimoAccesso;
        this.nickname = nickname;
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

    public Calendario getCalendario() {
        return this.calendario;
    }

}
