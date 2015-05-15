package Core;

import java.net.DatagramSocket;
import java.net.InetAddress;

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
	public static Server listener = null;
	public static boolean isServer = false;
	public static boolean isListening = false;

	// client's socket
	public static DatagramSocket socket = null;

}
