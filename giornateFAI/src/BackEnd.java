import java.sql.Connection;

public class BackEnd {

    private Utente utente;
    private Connection connection;
    private Registratore registratore;

    public BackEnd(Utente utente) {
        this.utente = utente;
        this.connection = ConnesioneSQL.getConnection();
        this.registratore = new Registratore(this.connection);
        
    }

    //questa funzione servirà da menu in cui il configuratore potra decidere a che opzione accedere
    public void menuConfiguratore() {

        while (true) {
            
            if (utente.isPrimoAccesso()) {
                switch (utente.getRuolo()) {
                    case "Configuratore":
                        ((Configuratore) utente).registrati(registratore);
                        break;
                    default:
                        break;
                }
               
            }

            System.out.println("Benvenuto " + this.utente.getNickname());
            System.out.println("1) Inserisci nuova giornata FAI");
            System.out.println("2) Modifica giornata FAI");
            System.out.println("3) Elimina giornata FAI");
            System.out.println("4) Visualizza giornate FAI");
            System.out.println("5) Logout");
        }
    }
    
}
