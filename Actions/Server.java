package Actions;
import StringUtils.Parsers;
import java.util.ArrayList;
import java.util.Date;

public class Server {
	public static ArrayList<User> active = new ArrayList<User>();
	public static ArrayList<User> users = new ArrayList<User>();
	
	static boolean addUser() {
		
		return true;
	}
	
	public static boolean createUser(String name, String ip, String password) {
		User u = new User(name, ip, password);
		
		for (User usr : users) {
			if(usr.nickname == name)
			{
				return false;	
			}
		}
		users.add(u);
		
		return true;
	}
	
	public static int logginUser(String name, String password) {
		
		for (User usr : users) {
			if(usr.nickname.equals(name)){
					if(usr.password.equals(password)) {
						active.add(usr); //add user to actives
						break;
					} 
					return 2;
			}
			return 1;
		}
		
		return 0;
		//0 no error
		//1 user error
		//2 password error
	}
	
	public static String actives(){
		String actives = "";
		for (User usr : active) {
			actives.concat(usr.nickname + ",");
		}
		
		return actives;
	}
	
	public static boolean removeActiveUser(String ip){
		int index = 0;
		for (User usr : users) {
			if(usr.ip.contains(ip)) {
				System.out.println("voy a sacar a: " + usr.nickname);
				break;
			}
			index ++;
		}
		active.remove(index);
		return true;
	}
	
	public static boolean saveDate(String date, String ip){
		
		for (User usr : active) {
			if(usr.ip.contains(ip)) {
				usr.timestamp = date;
				usr.actClock = true;
				break;
			}
		}
		return true;
	}
	
	public static boolean checkifLastTIme(){
		for (User usr : active) {
			if(usr.actClock == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean berkley(){
		float totalsecs = 0;
		float secs;
		int n = 0;
		String[] dta = new String[2];
		for (User usr : active) {
			n ++;
			secs = 0;
			dta = Parsers.parseTime(usr.timestamp);
			secs = Integer.parseInt(dta[0]) * 60;
			secs = secs + Integer.parseInt(dta[1]);
			secs *= 60;
			secs += Integer.parseInt(dta[2]);
			
			totalsecs += secs;
		}
		
		float prom = totalsecs / n;
		
		
		prom /= 60; //get minutes
		prom /= 60; //get hours
		
		int[] promtime = new int[2]; 
		
		promtime[0] = (int)prom;// subtract hours
		
		prom -= promtime[0];
		
		prom *= 60;
		
		promtime[1] = (int)prom; // substract mins
		
		prom -= promtime[1];
		
		
		
		return true;
	}
	
	
	public static boolean endActClock(Date date, String ip){
		
		for (User usr : active) {
				usr.actClock = false;
		}
		return true;
	}
	
}
