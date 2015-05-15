package Actions;

import java.util.Date;

public class User {
	public String nickname;
	public String ip;
	public String password;
	public String timestamp;
	public float secstimestamp;
	public float deltatime;
	public boolean actClock;
	
	User(String name, String ip, String password){
		nickname = name;
		ip = this.ip;
		password = this.password;
	}
}
