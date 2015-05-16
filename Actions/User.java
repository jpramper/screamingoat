package Actions;

import java.util.ArrayList;

public class User {
	public String nickname;
	public String ip;
	public String password;
	public String timestamp;
	public boolean isactive;
	public float secstimestamp;
	public float deltatime;
	public boolean actClock;
	public static ArrayList<String> banneds = new ArrayList<String>();
	
	User(String name, String ip, String password){
		nickname = name;
		this.ip = ip;
		this.ip = this.ip.replace("/", "");
		this.password = password;
	}
}
