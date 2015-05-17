package Actions;

import Core.Global;
import StringUtils.Parsers;

import java.util.ArrayList;

public class Server {
	public static ArrayList<User> users = new ArrayList<User>();
	public static ArrayList<Pendings> pendings = new ArrayList<Pendings>();
	
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
		System.out.println("resultado en hrs " + secs);
		
		int[] promtime = new int[3]; 
		
		promtime[0] = (int)secs;// subtract hours
		
		secs -= promtime[0];
		
		secs *= 60;
		
		promtime[1] = (int)secs; // substract mins
		
		secs -= promtime[1];
		
		secs *= 60;
		
		promtime[2] = (int)secs;
		String Stime = "";
		Stime = Stime.concat(promtime[0]+":"+promtime[1]+":"+promtime[2] + " ");
		return Stime;
	}
	
	
	public static boolean endActClock(){
		
		for (User usr : users) {
			if(usr.isactive)
				usr.actClock = false;
		}
		return true;
	}
	
	public static String banPerson(String nickname, String address){
		for (User usr : users) {
			if (usr.isactive)
				if(usr.ip.equals(address)){
					for(User usr2 : users){
						if(usr2.nickname.equals(nickname)){
							usr.banneds.add(nickname);
							return nickname;
						}
					}
					//no existand user
					return null;
				}
		}
		return nickname;
	}
	
	public static String unbanPerson(String nickname, String address){
		for (User usr : users) {
			if (usr.isactive)
				if(usr.ip.equals(address)){
					for(User usr2 : users){
						if(usr2.nickname.equals(nickname)){
							usr.banneds.remove(nickname);
							return nickname;
						}
					}
					//no existand user
					return null;
				}
		}
		return nickname;
	}
	
	public static String returnPersonfromIp(String ip){
		String name = "";
		for(User usr: users){
			if(usr.ip.equals(ip)){
				name = usr.nickname;
			}
		}
		return name;
	}
	
	public static String returnPerson(String name, String sender){
		
		if(sender.equals(name)) return "3";
		
		System.out.println("busco a :" + name);
		
		for (User usr : users) {
				System.out.println("estoy en: " + usr.nickname + "y busco a: " + name);
				if(usr.nickname.equals(name)){
					if(usr.isactive){
						if(usr.banneds.contains(sender)){
							//usuario banneado
							return "0";
						}
						return usr.ip;
					}else{
						//el usuario esta inactivo
						return "1";
					}
				}
		}
		//el usuario no existe
		return "2";
	}
	
	public static String returnIp(String name){
		String ip = "";
		for(User usr: users){
			if(usr.nickname.equals(name)){
				ip = usr.ip;
			}
		}
		return ip;
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
	
	public static void addPending(int id, String ip, String message){
		System.out.println("guardando pending message");
		System.out.println(id+","+ip+","+message);
		String user = Server.returnPersonfromIp(ip);
		Pendings p = new Pendings(id,user,message);
		pendings.add(p);
		for(Pendings r : Server.pendings){
			System.out.println(r.towho + ", "+ r.message);
		}
	}
	
	public static void removePending(int id){
		System.out.println("ya se entrego un mensaje ("+id+")");
		for(Pendings p : pendings){
			if (p.id == id){
				pendings.remove(p);
			}
		}
	}
	
	public static void revireveMessages(String name){
		System.out.println("retriveando mensajes de: " + name);
		for(Pendings p : Server.pendings){
			if(p.towho.equals(name)){
				//send message
				Global.sendMessage(17, p.message, Server.returnIp(name), 0, Global.messagingSocket, Global.messagingPort);
				Server.pendings.remove(p);
			}
		}
		Global.sendMessage(17, "no pendings messages", Server.returnIp(name), 0, Global.messagingSocket, Global.messagingPort);
	}
}
