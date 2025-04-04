package DataBaseImplementation;

import org.mindrot.jbcrypt.BCrypt;

import ServicesAPI.DTObject;

public class ServizioHash {

	//Usa BCrypt per aggiungere almeno un livello di hashing con sale
    public static void cifraPassword(DTObject utente) {
        String salt = BCrypt.gensalt(15);
        utente.impostaValore(salt, "Salt");
        String hashpsw = BCrypt.hashpw((String)utente.getValoreCampo("Password"), salt);
        utente.impostaValore(hashpsw, "Password");
    }

	public static boolean passwordValida(String passwordInserita, String passwordSalvata) {
		return BCrypt.checkpw(passwordInserita, passwordSalvata);
	}

    public static String generaCodice(String dati) {
        String salt = BCrypt.gensalt(15);
        return BCrypt.hashpw(dati, salt);
    }
}
