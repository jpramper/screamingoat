package Users;
import java.util.ArrayList;

public class Users {
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
		String active = "";
		for (User usr : users) {
			active.concat(usr.nickname + ",");
		}
		
		return active;
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
}
