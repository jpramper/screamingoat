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
		p.destType = 1;
		p.dataType = 13;
		p.data = "";
		
		// create a UDP packet with the data packet as a broadcast
		DatagramPacket sendPacket = new DatagramPacket(
				p.toString().getBytes(), 
				p.toString().length(), 
				Global.broadcastIp, 
				Global.portNumber);

		// send the hello packet
	    try {
	    	Global.socket.send(sendPacket);
	    	
	    	if (Global.DEBUG) 
	    		System.out.println("Goat.searchServer()| " + 
	    	"sent pak who listens");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    // create a packet to get a server handshake
	    byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		boolean serverAnswered = false;
		
		try {
			Global.socket.setSoTimeout(500);
		} catch (SocketException e) {
			e.printStackTrace();
		}

    	if (Global.DEBUG) 
    		System.out.println("Goat.searchServer()| " + 
    	"waiting for response");
    	
		while (!serverAnswered)
		{
			try {
				Global.socket.receive(receivePacket);
				
				DataPacket pkg = DataPacket.parseDataPacket(receivePacket);
				
				if (pkg.destType == 0)
					if (pkg.dataType == 14)
					{
						// if the server responded, store its IP and exit 
						Global.serverIp = receivePacket.getAddress();
						serverAnswered = true;
						
				    	if (Global.DEBUG) 
				    		System.out.println("Goat.searchServer()| " + 
				    	"server responded - ip: " + receivePacket.getAddress().toString());
					}
				
			} 
	    	catch (SocketTimeoutException e) {
	    		// if no server responded, set self as server
	        	if (Global.DEBUG) 
	        		System.out.println("Goat.searchServer()| " + 
	        	"no server response; starting listener");
	        	
	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	        	
	    		Global.startListener();
	    		
	    		// create a handshake packet
	    		DataPacket p1 = new DataPacket();
	    		p1.destType = 0;
	    		p1.dataType = 14;
	    		p1.data = "";
	    		
	    		// create a UDP packet with the data packet as a broadcast
	    		DatagramPacket sendPacket1 = new DatagramPacket(
	    				p1.toString().getBytes(), 
	    				p1.toString().length(), 
	    				Global.broadcastIp, 
	    				Global.portNumber);

	    		// send the hello packet
	    	    try {
	    	    	Global.socket.send(sendPacket1);
	    	    	
		        	if (Global.DEBUG) 
		        		System.out.println("Goat.searchServer()| " + 
		        	"sent 'im server' message");
	    		} catch (Exception ex) {
	    			ex.printStackTrace();
	    		}
	    	    
				serverAnswered = true;
	    	}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
