package DataBaseImplementation;

import java.util.Date;

import ServicesAPI.GestoreDateDisponibili;

public class XMLDateDisponibili extends XMLManager  implements GestoreDateDisponibili{

    public XMLDateDisponibili(String path) {
        super(path);
    }

    @Override
    public void registraDateDisponibili(Date today, Date[] dateDisponibli, String nomeVolontario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registraDateDisponibili'");
    }

    @Override
    public Date[] leggiDateDisponibili(String nomeVolontario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leggiDateDisponibili'");
    }

    @Override
    public void cleanDates(Date data, String nomeVolontario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cleanDates'");
    }

    //private static final DateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");



}
