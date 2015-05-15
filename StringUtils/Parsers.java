package StringUtils;

public class Parsers {
	
	public static String[] parseSignLogin(String Data) {
		String[] dta = new String[2];
		dta = Data.split("¬|¬");
		
		return dta;
	}

}
