package Actions;

public class Pendings {
	public String towho;
	public String message;
	public int id;
	
	Pendings(int id, String to, String message){
		this.id = id;
		this.towho = to;
		this.message = message;
	}
}
