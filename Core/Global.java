package Core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import Actions.Server;
import Actions.User;
import Main.Goat;
import UserInterface.ChatWindow;

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
	
	public static boolean isConnectionAlive() {
		boolean result = false;
		// ask the server if its alive through the discovery port
		// don't use send message since it would be recursive
		DataPacket p = new DataPacket();
		p.dataType = 15;
		p.data = "";
		p.destType = 0;
		p.serverIp = Global.serverIp.toString().replace("/", "");

		// create a UDP packet with the data packet
		DatagramPacket sendPacket = new DatagramPacket(
				p.toString().getBytes(), 
				p.toString().length(), 
				Global.serverIp, 
				Global.discoveryPort);
		
		// send the packet
	    try {
			Global.discoverySocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// wait for confirmation of the server on the ack port
		DatagramPacket receivePacket;
		byte[] receiveData;
		try {
    		receiveData = new byte[1024];
    		receivePacket = new DatagramPacket(receiveData, receiveData.length);
    		
    		Global.ackSocket.receive(receivePacket);
    		
    		result = true;
    	}
    	catch (SocketTimeoutException e) {
    		// do a server discovery again
    		System.out.println("isConnectionAlive: SERVER IS DOWN!");
    	}
    	catch (Exception e) {
			e.printStackTrace();
    	}
		
		return result;
	}
	
	public static void sendMessage(
			int type, 
			String data, 
			String destiny, 
			int destType,
			DatagramSocket socket,
			int port) {
		if (!isConnectionAlive())
			searchServer();
		
		if(destiny.contains("/")) destiny = destiny.replace("/", ""); //   avoid /
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.destType = destType;
		p.serverIp = Global.serverIp.toString().replace("/", "");
		
		DatagramPacket sendPacket = null;
		try {
			// create a UDP packet with the data packet
			sendPacket = new DatagramPacket(
					p.toString().getBytes(), 
					p.toString().length(), 
					InetAddress.getByName(destiny), 
					port);
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
		if (!isConnectionAlive())
			searchServer();
		
		DataPacket p = new DataPacket();
		p.dataType = type;
		p.data = data;
		p.destType = destType;
		p.serverIp = Global.serverIp.toString().replace("/", "");
		DatagramPacket sendPacket = null;
		
		for (User usr : Server.users) {
			if(usr.isactive){
				if(User.banneds.contains(sender)) continue; //skip banned persons
				
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
        	ChatWindow.getInstance().chkIsServer.setSelected(true);
    		
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
