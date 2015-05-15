package Core;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Application-wide variable container class.
 *  
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class Global {
	
	public static final String BROADCAST_IP = "192.168.2.255";
	
	// connection data
	public static InetAddress broadcastIp;
	public static InetAddress wildcardIp;
	public static InetAddress serverIp;
	public static int portNumber;

	// listener
	public static Listener listener = null;
	public static boolean isServer = false;
	public static boolean isListening = false; // TODO cambiar en server

	// client's socket
	public static DatagramSocket socket = null;
	
	// static initializer
	static {
		// establish connection data
		try {
			broadcastIp = InetAddress.getByName(BROADCAST_IP);
			wildcardIp = InetAddress.getByName("0.0.0.0");
			portNumber = 5001;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// create the socket connections
		try {
			Global.socket = new DatagramSocket(portNumber, wildcardIp);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
