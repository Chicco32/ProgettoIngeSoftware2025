

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Login login = new Login();
        CliUtente.benvenutoConfiguratore();
        BackEnd backEnd = login.loginConfiguratore(CliUtente.chiediNickname(), CliUtente.chiediPassword());
        backEnd.menuConfiguratore();


    }
}
