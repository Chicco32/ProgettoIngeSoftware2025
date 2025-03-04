
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        
        Login login = new Login();
        Utente utente = login.loginUtente(CliUtente.chiediNickname(), CliUtente.chiediPassword());
        while (utente == null) {
            utente = login.loginUtente(CliUtente.chiediNickname(), CliUtente.chiediPassword()); 
        }
        
        if (utente.isPrimoAccesso()) {
            switch (utente.getRuolo()) {
                case "Configuratore":
                    ((Configuratore) utente).registrati();
                    break;
                default:
                    break;
            }
        }

        switch (utente.getRuolo()) {
            case "Configuratore":
            CliUtente.benvenutoConfiguratore();
            BackEnd backEnd = new BackEnd(utente);
            backEnd.menuConfiguratore();
                break;
            default:
                break;
        }
    }
}
