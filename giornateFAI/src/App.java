import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

         Connection conn = ConnesioneSQL.getConnection();
        if (conn != null) {
            try {

                //inserisce un nuovo fruitore
                String inserimentoProva = "INSERT INTO `dbingesw`.`fruitori` (`Nickname`, `Password`) VALUES ('paola3prova', 'qidubhiodb')";
                conn.createStatement().executeUpdate(inserimentoProva);
                System.out.println("Inserimento riuscito!");

                //legge la tabella fruitori
                String selectProva = "SELECT * FROM fruitori"; 
                ResultSet rs = conn.createStatement().executeQuery(selectProva);
                while (rs.next()) {
                    System.out.println("Nickname: " + rs.getString("Nickname") + " Password: " + rs.getString("Password"));
                }

                conn.close(); // Chiude la connessione
                System.out.println("Connessione chiusa.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
