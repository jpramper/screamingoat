package Core;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import Actions.Client;
import Actions.Server;
import Actions.User;
import StringUtils.Parsers;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Listener implements Runnable {
	
	Listener() {
	  Global.isListening = true;
	}
	
	public void run(){
		DatagramPacket receivePacket;
		byte[] receiveData;

	    while(Global.isListening){
	    	try {
	    		receiveData = new byte[1024];
	    		receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    		System.out.println("En espera -.-");
	    		
	    		Global.socket.receive(receivePacket);
	    		
	    		DataPacket pkg = DataPacket.parseDataPacket(receivePacket);
	    		String address = receivePacket.getAddress().toString();
	    		
	    		processMsg(pkg, address);
	    		
	    		
	    		

	    		System.out.println("RECIBI!!!!!!!!!!!!!!!!!!!!! :D");
	    	}
	    	catch (SocketTimeoutException e) {
	    		continue;
	    	}
	    	catch (Exception e) {
				e.printStackTrace();
				Global.isListening = false;
	    	}
	    }
	}
	
	public void processMsg(DataPacket pkg, String address) {
		//parse pkg
		int destType = pkg.destType;
		
		switch (pkg.dataType) {
			
			case 1: //msg
				break;
				
			case 2:	//boadcast	
				break;
				
			case 3: //login	
				String[] dtalgin = Parsers.parseSignLogin(pkg.data);
				int ret = Server.logginUser(dtalgin[0], dtalgin[1]);
				
				switch (ret){
					case 0:
						setupMsg(6, "", address,0); //success loggin in
						syncClocks();
						break;
					case 1:
						setupMsg(6, "1", address,0); //incorrect name
						break;
					case 2:
						setupMsg(6, "2", address,0); //incorrect password
						break;
					default:
						break;
				}
				
				break;
				
				
			//shareReq	
			case 4:
				setupMsg(5, Server.actives(), address,0);
				break;
				
			//shareRsp  who is active ?	
			case 5:
				break;
				
			//ack	
			case 6:
				break;
				
			//exit	
			case 7:
				Server.removeActiveUser(address);
				setupMsg(6, "", address,0);
				break;
				
			//error	
			case 8:
				break;
				
			//handshake
			case 9:
				setupMsg(9, "", address,0);
				break;
				
			//sign in
			case 10:
				String[] dtasgnin = Parsers.parseSignLogin(pkg.data);
				
				if(Server.createUser(dtasgnin[0], address ,dtasgnin[1]))
					setupMsg(6, "", address,0); //success sign in
				else
					setupMsg(8, "", address,0); //name already taken
				break;
				
			//time	
			case 11:
				if(destType == 1){ //server mode (receive time and process it)
					Server.saveDate(pkg.data,address);
					if(Server.checkifLastTIme()){
						
					}
				}else if (destType == 0){ // Client mode (send my timestamp)
					String time = Client.getTime();
					setupMsg(11,time,Global.serverIp.toString(),1);
				}
				
			default:
				break;
		
		}
	}
	
	public void setupMsg(int type, String data, String destiny, int destType) {
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.serverIp = Global.serverIp.toString();
		
		DatagramPacket sendPacket = null;
		try {
			// create a UDP packet with the data packet
			sendPacket = new DatagramPacket(
					p.toString().getBytes(), 
					p.toString().length(), 
					InetAddress.getByName(destiny), 
					Global.portNumber);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		

		// send the login packet
	    try {
			Global.socket.send(sendPacket);
			System.out.println("se envió");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public void brodcast(int type, String data) {
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.serverIp = Global.serverIp.toString();
		DatagramPacket sendPacket = null;
		
		for (User usr : Server.active) {
			sendPacket = null;
			
			try {
				// create a UDP packet with the data packet
				sendPacket = new DatagramPacket(
						p.toString().getBytes(), 
						p.toString().length(), 
						InetAddress.getByName(usr.ip), 
						Global.portNumber);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String syncClocks(){
		brodcast(11,"");
		
		return "";
	}
	
}
