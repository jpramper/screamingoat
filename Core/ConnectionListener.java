package Core;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Se encarga de escuchar en el puerto 5001 a los broadcasts de nueva gente conectandose,
 * y responder sólo si ésta instancia es el servidor.
 * 
 * @author john
 *
 */
public class ConnectionListener implements Runnable {
	public boolean isListening;
	
	public void run(){
		isListening = true;
		DatagramPacket receivePacket;
		byte[] receiveData;
		
		if (Global.DEBUG) 
    		System.out.println("ConnectionListener.run()| " + 
    	"listener started.");

	    while(isListening){
	    	try {
	    		receiveData = new byte[1024];
	    		receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    		
	    		Global.discoverySocket.receive(receivePacket);
	    		
	    		DataPacket pkg = DataPacket.parseDataPacket(receivePacket);
	    		String address = receivePacket.getAddress().toString();
	    		
	    		if (Global.DEBUG) 
	        		System.out.println("ConnectionListener.run()| " + 
	        	"received message. type " + pkg.dataType);
	    		
	    		processMsg(pkg, address.replace("/", ""));
	    	}
	    	catch (Exception e) {
				e.printStackTrace();
				isListening = false;
	    	}
	    }
	}
	
	public void processMsg(DataPacket pkg, String address) {

		pkg.data = pkg.data.trim();
		
		switch (pkg.dataType) {
			
			case 13: // who listens?
				if (Global.isServer) {
					// if i'm the server, send a response on the ACK channel
					Global.sendMessage(
							14, 
							"", 
							address, 
							0, 
							Global.ackSocket, 
							Global.ackPort);
				}
				break;
				
			case 14:
				// if a declaration of server is received, change the server IP
				try {
					Global.serverIp = InetAddress.getByName(address);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
				break;
				
			case 15: 
				if (Global.isServer) {
					// if i'm the server, send a response on the ACK channel
					Global.sendMessage(
							15, 
							"", 
							address, 
							0, 
							Global.ackSocket, 
							Global.ackPort);
					DataPacket p = new DataPacket();
					p.dataType = 15;
					p.data = "";
					p.destType = 0;
					p.serverIp = Global.serverIp.toString().replace("/", "");

					// create a UDP packet with the data packet
					DatagramPacket sendPacket = null;
					try {
						sendPacket = new DatagramPacket(
								p.toString().getBytes(), 
								p.toString().length(), 
								InetAddress.getByName(address.replace("/", "")), 
								Global.ackPort);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					
					// send the packet
				    try {
						Global.ackSocket.send(sendPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
				
			default:
				break;
		
		}
	}
}