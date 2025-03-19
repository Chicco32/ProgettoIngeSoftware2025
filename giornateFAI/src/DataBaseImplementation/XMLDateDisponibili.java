package DataBaseImplementation;

import java.util.Date;

import ServicesAPI.GestoreDateDisponibili;

public class XMLDateDisponibili extends XMLManager  implements GestoreDateDisponibili{

    public XMLDateDisponibili(String path) {
        super(path);
    }
    
    //private static final DateFormat formatoData =new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void dichiaraDateDisponibili(Date[] dateDisponibli, String nomeVolontario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dichiaraDateDisponibili'");
    }

    @Override
    public Date[] leggiDateDisponibli(String nomeVolontario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leggiDateDisponibli'");
    }

    @Override
    public void cleanDates(Date data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cleanDates'");
    }


}
