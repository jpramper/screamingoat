package Core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class ConnectionManager {
	
	private static ConnectionManager instance = null; 
	
	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	/*
	 * ************************************************************************
	 * 							Connection Attributes
	 * ************************************************************************
	 * */
    
    // server listener
    private Escuchador listen = null;

	/*
	 * ************************************************************************
	 * 								Constructors
	 * ************************************************************************
	 * */
	
	private ConnectionManager() {
		
	}
	
	public void buscaServidor() {
		// create a handshake packet
		DataPacket p = new DataPacket();
		p.dataType = 9;
		p.data = "";
		
		// create a UDP packet with the data packet as a broadcast
		DatagramPacket sendPacket = new DatagramPacket(
				p.toString().getBytes(), 
				p.toString().length(), 
				Global.broadcastIp, 
				Global.portNumber);

		// send the login packet
	    try {
	    	Global.socket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    // create a packet to get a server handshake
	    byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {

			try {
				Global.socket.setSoTimeout(500);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			Global.socket.receive(receivePacket); // clientSocket recibe desde el wildcard
			
			receivePacket.getAddress();
			
			System.out.println(receivePacket.getAddress()); //TODO tostring, sacarlo del protocolo 
			
			try {
				Global.serverIp = InetAddress.getByName(Global.BROADCAST_IP);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} 
    	catch (SocketTimeoutException e) {
    		// TODO 
    	}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void iniciaServidor() {
		// create the server listener to react to communications
		try{
	    	listen = new Escuchador();
			Thread t = new Thread(listen);
			t.start();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	
	public void escucha() {
		// create the response listener to react to communications
		try{
	    	listen = new Escuchador();
			Thread t = new Thread(listen);
			t.start();
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (Global.socket.isConnected())
			Global.socket.close();
		
		super.finalize();
	}

	/*
	 * ************************************************************************
	 * 							Connection Methods
	 * ************************************************************************
	 * */

	/*
	 * ************************************************************************
	 * 							Server Listener
	 * ************************************************************************
	 * */
	
	class Escuchador implements Runnable {
		public boolean isRunning;
		
		Escuchador() {
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
	}

}
