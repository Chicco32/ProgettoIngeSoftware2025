package lib.Test;
import static org.junit.Assert.*;

import org.junit.Test;
import DataBaseImplementation.*;

public class XMLTest {

	@Test
	public void verificaFunzionamentoExists(){
		String notARealPath="ieaiaio";
		assertFalse(XMLManager.fileExists(notARealPath));
	}
}
