package Actions;

import java.util.ArrayList;

public class User {
	public String nickname;
	public String ip;
	public String password;
	public String timestamp; //mark of time  hh:mm:ss
	public boolean isactive;
	public float secstimestamp; //mark of time in seconds
	public float deltatime; //diff time
	public boolean actClock;
	public ArrayList<String> banneds = new ArrayList<String>();
	
	User(String name, String ip, String password){
		nickname = name;
		this.ip = ip;
		this.ip = this.ip.replace("/", "");
		this.password = password;
	}
	User(String name, 
			String ip, 
			String password,
			String timestamp,
			String isactive,
			String secstimestamp,
			String deltatime,
			String actClok
			){
		
		this.nickname = name;
		this.ip = ip;
		this.password = password;
		this.timestamp = timestamp;
		this.isactive = Boolean.parseBoolean(isactive);
		this.secstimestamp = Float.parseFloat(secstimestamp);
		this.deltatime = Float.parseFloat(deltatime);
		this.actClock = Boolean.parseBoolean(actClok);
	}
}
