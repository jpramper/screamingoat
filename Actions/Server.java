package Actions;

import StringUtils.Parsers;
import java.util.ArrayList;

public class Server {
	public static ArrayList<User> users = new ArrayList<User>();
	
	static boolean addUser() {
		
		return true;
	}
	
	public static boolean createUser(String name, String ip, String password) {
		System.out.println("voy a guardar su password: " + password);
		User u = new User(name, ip, password);
		
		System.out.println("aqui debe salir: " + u.nickname);
		for (User usr : users) {
			if(usr.nickname.equals(name))
			{
				return false;	
			}
		}
		users.add(u);
		
		System.out.println("users: ");
		for (User usr : users) {
			System.out.println(usr.nickname);
		}
		
		return true;
	}
	
	public static int logginUser(String name, String password) {
		System.out.println("el usuario: " + name);
		System.out.println("el password: " + password);
		
		for (User usr : users) {
			if(usr.nickname.equals(name)){
				System.out.println("encontre el nombre");
				System.out.println("voy a buscar:" + usr.password);
				if(usr.password.equals(password)) {
					if(usr.isactive == true) return 3; //skip if already logged in
					usr.isactive = true; //add user to actives
					System.out.println("los usuarios estan activos: ");
					
					for (User usr2 : users) {
						if(usr2.isactive)
						System.out.println(usr2.nickname);
					}
					
					return 0;
				} 
				return 2;
			}
		}
		return 1;
		//0 no error
		//1 user error
		//2 password error
	}
	
	public static String actives(){
		String actives = "";
		for (User usr : users) {
			if(usr.isactive)
				actives = actives.concat(usr.nickname + ",");
		}
		
		return actives;
	}
	
	public static boolean removeActiveUser(String ip){
		ip = ip.replace("/", "");
		for (User usr : users) {
			if(usr.ip.contains(ip)) {
				System.out.println("voy a sacar a: " + usr.nickname);
				usr.isactive = false;
				return true;
			}
		}
		return false;
	}
	
	public static boolean saveDate(String date, String ip){
		ip = ip.replace("/", "");
		for (User usr : users) {
			if(usr.isactive)
				if(usr.ip.contains(ip)) {
					usr.timestamp = date;
					usr.actClock = true;
					break;
				}
		}
		return true;
	}
	
	public static boolean checkifLastTIme(){
		for (User usr : users) {
			if(usr.isactive)
				if(usr.actClock == false) {
					System.out.println("falta " + usr.nickname + " de mandar su tiempo");
					return false;
				}
		}
		System.out.println("todos mandaron su tiempo");
		return true;
	}
	
	public static void berkley(){
		float totalsecs = 0;
		float secs;
		int n = 0;
		String[] dta = new String[2];
		//get prom in secs
		for (User usr : users) {
			if(usr.isactive){
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
		}
		
		float prom = totalsecs / n;
		
		for (User usr : users) {
			if(usr.isactive)
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
		
		for (User usr : users) {
			if(usr.isactive)
				usr.actClock = false;
		}
		return true;
	}
	
	public static void banPerson(String nickname, String address){
		for (User usr : users) {
			if (usr.isactive)
				if(usr.ip.equals(address)){
					User.banneds.add(nickname);
				}
		}
	}
	
	public static String returnPerson(String name, String sender){
		
		
		for (User usr : users) {
			if(usr.isactive)
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
		//get destinatarium
		//write mssg
		return datas;
		
	}
	
	public static String collectToSincData(){
		String fullData = "";
		String userInfo = "";
		int i = 0;
		
		for (User usr : users) {
			userInfo = "";
			userInfo = userInfo.concat(usr.nickname+",");
			userInfo = userInfo.concat(usr.ip+",");
			userInfo = userInfo.concat(usr.password+",");
			userInfo = userInfo.concat(usr.timestamp+",");
			userInfo = userInfo.concat(usr.isactive+",");
			userInfo = userInfo.concat(usr.secstimestamp+",");
			userInfo = userInfo.concat(usr.deltatime+",");
			userInfo = userInfo.concat(usr.actClock+"~");
			fullData = fullData.concat(userInfo);
			i++;
		}
		String iData = i+"";
		iData = iData.concat("-"+fullData);
		fullData = iData;
		return fullData;
	}
}
