package Actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client {
	
	public static String getTime(){
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String Sdate = dateFormat.format(date);
		
		return Sdate;
	}
	
	public static void saveAndSincData(String allData){
		ArrayList<User> Newusers = new ArrayList<User>();
		
		String[] dta = new String[2];
		dta = allData.split("-");
		//dta[0] -> numero de usuarios
		//dta[1] -> usuarios
		
		String[] usuarios = new String[Integer.parseInt(dta[0])];
		usuarios = dta[1].split("~");
		for(int i = 0; i < Integer.parseInt(dta[0]); i++){
			String[] info = new String[8];
			info = usuarios[i].split(",");
			User u = new User(info[0], info[1], info[2],info[3],info[4],info[5],info[6],info[7]);
			Newusers.add(u);
		}
		Server.users = Newusers;
		
	}

}
