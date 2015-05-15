package Actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
	
	public static String getTime(){
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String Sdate = dateFormat.format(date);
		
		return Sdate;
	}

}
