import it.unibs.fp.mylib.InputDati;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Login login = new Login();
        //prova di login con credenziali inserite dall'utente
        System.err.println("Prova di login con credenziali gia registrate");
        String nickname = InputDati.leggiStringa("Inserisci il tuo nickname");
        String password = InputDati.leggiStringa("Inserisci la tua password");
        if (login.loginConfiguratore(nickname, password)) {
            Configuratore configuratore = new Configuratore(nickname);
            System.err.println("Benvenuto " + configuratore.getNickname());
        };


    }
}
