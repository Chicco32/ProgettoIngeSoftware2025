public abstract class Utente {

    String ruolo; //Configuratore, Fruitore o Volontario
    String nickname;
    Boolean PrimoAccesso;
    

    public Utente(boolean PrimoAccesso) {
        this.nickname = null; 
        this.PrimoAccesso = PrimoAccesso;
    }
    
    public void setRuolo(String ruolo){
        this.ruolo = ruolo;
    }

    public String getRuolo() {
        return this.ruolo;
    }

    public boolean isPrimoAccesso() {
        return this.PrimoAccesso;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
