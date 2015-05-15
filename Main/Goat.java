package Main;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import Core.DataPacket;
import Core.Global;
import UserInterface.Login;

/**
 * Application start point. 
 *  
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class Goat {

	public static void main(String[] args) {
		// initiate server lookup
		searchServer();
		
		// create and present the login window
		Login login = Login.getInstance();
		login.setVisible(true);
	}
	
	public static void searchServer() {
		// create a handshake packet
		DataPacket p = new DataPacket();
		p.destType = 0;
		p.serverIp = "8";
		p.dataType = 13; // who is online?
		p.data = "";
		
		// create a UDP packet with the data packet as a broadcast
		DatagramPacket sendPacket = new DatagramPacket(
				p.toString().getBytes(), 
				p.toString().length(), 
				Global.broadcastIp, // send to anyone
				Global.discoveryPort); // use discovery port
		System.out.println(p.dataType);
		// send the hello packet
	    try {
	    	Global.discoverySocket.send(sendPacket);
	    	
	    	if (Global.DEBUG) 
	    		System.out.println("Goat.searchServer()| " + 
	    	"sent pak 'who listens?', awaiting response.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    // create a packet to get a server handshake
	    byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    	
		try {
			Global.ackSocket.receive(receivePacket);
			
			DataPacket pkg = DataPacket.parseDataPacket(receivePacket);
			
			if (pkg.dataType == 14)
			{
				// if the server responded, store its IP and exit 
				Global.serverIp = receivePacket.getAddress();
				
		    	if (Global.DEBUG) 
		    		System.out.println("Goat.searchServer()| " + 
		    	"server responded - ip: " + receivePacket.getAddress().toString());
			}
			
		} 
    	catch (SocketTimeoutException e) {
    		// if no server responded, set self as server
        	if (Global.DEBUG) 
        		System.out.println("Goat.searchServer()| " + 
        	"no server response after 'who listens?'; set self as server");
        	
        	Global.isServer = true;
    		
    		// create a handshake packet
    		p = new DataPacket();
    		p.dataType = 14; // i'm server
    		
    		// create a UDP packet with the data packet as a broadcast
    		sendPacket = new DatagramPacket(
    				p.toString().getBytes(), 
    				p.toString().length(), 
    				Global.broadcastIp, 
    				Global.discoveryPort);

    		// send the hello packet
    	    try {
    	    	Global.discoverySocket.send(sendPacket);
    	    	
	        	if (Global.DEBUG) 
	        		System.out.println("Goat.searchServer()| " + 
	        	"sent 'im server' message");
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
