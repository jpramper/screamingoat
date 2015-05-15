package Actions;

import StringUtils.Parsers;
import java.util.ArrayList;

public class Server {
	public static ArrayList<User> active = new ArrayList<User>();
	public static ArrayList<User> users = new ArrayList<User>();
	
	static boolean addUser() {
		
		return true;
	}
	
	public static boolean createUser(String name, String ip, String password) {
		User u = new User(name, ip, password);
		
		for (User usr : users) {
			if(usr.nickname.equals(name))
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
	
	public static void berkley(){
		float totalsecs = 0;
		float secs;
		int n = 0;
		String[] dta = new String[2];
		//get prom in secs
		for (User usr : active) {
			n ++;
			secs = 0;
			dta = Parsers.parseTime(usr.timestamp);
			secs = Integer.parseInt(dta[0]) * 60;
			secs = secs + Integer.parseInt(dta[1]);
			secs *= 60;
			secs += Integer.parseInt(dta[2]);
			
			usr.secstimestamp = secs;
			totalsecs += secs;
		}
		
		float prom = totalsecs / n;
		
		for (User usr : active) {
			usr.deltatime = prom - usr.secstimestamp;
		}
	}
	
	public static String secsToTime(float secs){
		//get prom in time
		
		secs /= 60; //get minutes
		secs /= 60; //get hours
		
		int[] promtime = new int[3]; 
		
		promtime[0] = (int)secs;// subtract hours
		
		secs -= promtime[0];
		
		secs *= 60;
		
		promtime[1] = (int)secs; // substract mins
		
		secs -= promtime[1];
		
		secs *= 60;
		
		promtime[2] = (int)secs;
		String Stime = "";
		Stime.concat(promtime[2]+":"+promtime[1]+":"+promtime[0]);
		return Stime;
	}
	
	
	public static boolean endActClock(){
		
		for (User usr : active) {
				usr.actClock = false;
		}
		return true;
	}
	
	public static void banPerson(String nickname, String address){
		for (User usr : active) {
			if(usr.ip.equals(address)){
				User.banneds.add(nickname);
			}
		}
	}
	
	public static String returnPerson(String name, String sender){
		
		
		for (User usr : active) {
			if(usr.nickname.equals(name)){
				if(User.banneds.contains(sender)){
					return "banned";
				}
				return usr.ip;
			}
		}
		return null;
	}
	
	public static String[] privateMessage(String data){
		String[] datas = new String[2];
		datas = Parsers.parsePrivateMessage(data);
		datas[0].replaceAll("@", "");
		//get destinatarium
		//write mssg
		return datas;
		
	}
	
}
