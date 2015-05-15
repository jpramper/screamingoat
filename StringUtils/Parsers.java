package StringUtils;

public class Parsers {
	
	public static String[] parseSignLogin(String Data) {
		String[] dta = new String[2];
		dta = Data.split("~");
		
		return dta;
	}
	
	public static String[] parseTime(String time){
		String[] dta = new String[2];
		dta = time.split(":");
		
		return dta;
	}
	
	public static String[] parsePrivateMessage(String data){
		String[] dta = new String[2];
		dta = data.split("~");
		
		return dta;
	}

}
