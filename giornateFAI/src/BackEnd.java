import java.sql.Connection;

public class BackEnd {

    private Utente utente;

    public BackEnd(Utente utente) {
        this.utente = utente;
        this.connection = ConnesioneSQL.getConnection();
        
    }

    /*
     * questa funzione servirà da menu in cui il configuratore potra decidere a che opzione accedere
     *   TODO da fare
     */
    public void menuConfiguratore() {

        Boolean continua = true;
        while (continua) {
            
               
            
            System.out.println("Benvenuto " + this.utente.getNickname());
            System.out.println("1) Inserisci nuova giornata FAI");
            System.out.println("2) Modifica giornata FAI");
            System.out.println("3) Elimina giornata FAI");
            System.out.println("4) Visualizza giornate FAI");
            System.out.println("5) Logout");

        }
    }
    
}
