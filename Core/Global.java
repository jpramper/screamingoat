package Core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import Actions.Server;
import Actions.User;

/**
 * Application-wide variable container class.
 *  
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class Global {
	
	/**
	 * Shows or hides console status messages throughout the program
	 */
	public static final boolean DEBUG = true;
	
	public static final String BROADCAST_IP = "192.168.2.255";
	
	// connection data
	public static InetAddress broadcastIp;
	public static InetAddress wildcardIp;
	public static InetAddress serverIp;

	// listener
	public static MessageListener msgListener = null;
	public static ConnectionListener connListener = null;
	public static boolean isServer = false;

	// client's sockets
	public static DatagramSocket discoverySocket = null;
	public static DatagramSocket ackSocket = null;
	public static DatagramSocket messagingSocket = null;
	
	/**
	 * Dedicated to listen to "who listens?" network broadcasts of new clients.
	 * (while(true) listener, occasional sender)
	 */
	public static int discoveryPort;
	/**
	 * Dedicated for client-side blocking confirmations of "isServerAlive?"
	 * before sending messages
	 * (occasional receiver)
	 */
	public static int ackPort;
	/**
	 * Dedicated to listening and sending messaging requests of users.
	 * (while(true) listener, occasional sender)
	 */
	public static int messagingPort;
	
	// static initializer
	static {
		// establish connection data
		try {
			broadcastIp = InetAddress.getByName(BROADCAST_IP);
			wildcardIp = InetAddress.getByName("0.0.0.0");
			discoveryPort = 5001;
			ackPort = 5002;
			messagingPort = 5003;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// create the socket connections
		try {
			// discovery socket receives broadcast packages:
			Global.discoverySocket = new DatagramSocket(discoveryPort, wildcardIp);
			try {
				// infinite timeout wait delay for listener thread
				Global.discoverySocket.setSoTimeout(0);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
			// connection socket receives targeted packages:
			Global.ackSocket = new DatagramSocket(ackPort, wildcardIp);
			try {
				// 0.5 timeout wait delay for server ACKs
				Global.ackSocket.setSoTimeout(500);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
			// messaging socket receives only targeted packages:
			Global.messagingSocket = new DatagramSocket(messagingPort);
			try {
				// infinite timeout wait delay for listener thread
				Global.messagingSocket.setSoTimeout(0);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		// create the server listeners
		try{
	    	connListener = new ConnectionListener();
			Thread t = new Thread(connListener);
			t.start();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
		try{
	    	msgListener = new MessageListener();
			Thread t = new Thread(msgListener);
			t.start();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	
	public static void sendMessage(
			int type, 
			String data, 
			String destiny, 
			int destType,
			DatagramSocket socket) {
		
		if(destiny.contains("/")) destiny = destiny.replace("/", ""); //   avoid /
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.destType = destType;
		p.serverIp = Global.serverIp.toString();
		
		DatagramPacket sendPacket = null;
		try {
			// create a UDP packet with the data packet
			sendPacket = new DatagramPacket(
					p.toString().getBytes(), 
					p.toString().length(), 
					InetAddress.getByName(destiny), 
					Global.messagingPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// send the packet
	    try {
			socket.send(sendPacket);
			
			if (Global.DEBUG) 
	    		System.out.println("Global.sendMessage()| " + 
			"Se envió mensaje: " + p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public static void sendBroadcast(
			int type, 
			String data, 
			int destType, 
			String sender, 
			DatagramSocket socket) {
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.destType = destType;
		p.serverIp = Global.serverIp.toString();
		DatagramPacket sendPacket = null;
		
		for (User usr : Server.active) {
			if(usr.banneds.contains(sender)) continue; //skip banned persons
			
			sendPacket = null;
			
			try {
				// create a UDP packet with the data packet
				sendPacket = new DatagramPacket(
						p.toString().getBytes(), 
						p.toString().length(), 
						InetAddress.getByName(usr.ip), 
						Global.messagingPort);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			// send the packet
		    try {
				socket.send(sendPacket);
				
				if (Global.DEBUG) 
		    		System.out.println("Global.sendBroadcast()| " + 
				"Se envió broadcast: " + p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
