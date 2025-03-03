import java.sql.Connection;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Registratore reg = new Registratore(ConnesioneSQL.getConnection());


        Login login = new Login();
        Utente utente = login.loginConfiguratore(CliUtente.chiediNickname(), CliUtente.chiediPassword());

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
