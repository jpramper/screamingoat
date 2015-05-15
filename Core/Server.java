package Core;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import StringUtils.Parsers;
import Users.Users;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Server implements Runnable {
	public boolean isRunning;
	
	Server() {
	  isRunning = true;
	}
	
	public void run(){
		DatagramPacket receivePacket;
		byte[] receiveData;

	    while(isRunning){
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
				isRunning = false;
	    	}
	    }
	}
	
	public void processMsg(DataPacket pkg, String address) {
		//parse pkg
		
		switch (pkg.dataType) {
			
			case 1: //msg
				break;
				
			case 2:	//boadcast	
				break;
				
			case 3: //login	
				String[] dtalgin = Parsers.parseSignLogin(pkg.data);
				int ret = Users.logginUser(dtalgin[0], dtalgin[1]);
				
				switch (ret){
					case 0:
						setupMsg(6, "", address); //success loggin in
						break;
					case 1:
						setupMsg(6, "1", address); //incorrect name
						break;
					case 2:
						setupMsg(6, "2", address); //incorrect password
						break;
					default:
						break;
				}
				
				break;
				
				
			//shareReq	
			case 4:
				setupMsg(5, Users.actives(), address);
				break;
				
			//shareRsp  who is active ?	
			case 5:
				break;
				
			//ack	
			case 6:
				break;
				
			//exit	
			case 7:
				Users.removeActiveUser(address);
				setupMsg(6, "", address);
				break;
				
			//error	
			case 8:
				break;
				
			//handshake
			case 9:
				setupMsg(9, "", address);
				break;
				
			//sign in
			case 10:
				String[] dtasgnin = Parsers.parseSignLogin(pkg.data);
				
				if(Users.createUser(dtasgnin[0], address ,dtasgnin[1]))
					setupMsg(6, "", address); //success sign in
				else
					setupMsg(8, "", address); //name already taken
				break;
				
			default:
				break;
		
		}
	}
	
	public void setupMsg(int type, String data, String destiny) {
		
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
	
	public String syncClocks(){
		
		return "";
	}
	
}
