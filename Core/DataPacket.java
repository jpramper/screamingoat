package Core;

import java.net.DatagramPacket;

/*
 * 
 * Mensaje para servidor o para cliente
 * para cliente (0)
 * para servidor (1)
 * 
 * ~|~
 * 
 * Ip del Servidor (string)
 * string variable
 * 
 * ~|~
 * 
 * Tipo de Mensaje (int)
 * msg(1), 
 * broadcastmsg(2), 
 * login(3), 
 * shareReq(4), 
 * shareRsp(5), 
 * ack(6), 
 * exit(7), 
 * error(8), 
 * handshake(9),
 * signIn(10),
 * timereq(11),
 * timeack(12),
 * hello?(13) [¿quién está escuchando?]
 * hello!(14) [isServer ? data = "true": data = "false"]
 * 
 * ~|~
 * 
 * Datos (string)
 * 
 */

/**
 * 
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class DataPacket {

	public int destType;
	public String serverIp;
	public int dataType;
	public String data;
	
	public DataPacket() {
		init();
	}
	
	public void init() {
		destType = -1;
		serverIp = (Global.serverIp == null) ? "-1" : Global.serverIp.toString();
		dataType = -1;
		data = "";
	}
	
	@Override
	public String toString() {
		return 
				destType +
				"~|~" + 
				serverIp + 
				"~|~" + 
				dataType + 
				"~|~" + 
				data;
	}
	
	public static DataPacket parseDataPacket(DatagramPacket udpPacket) {
		DataPacket p = new DataPacket();
		String xml = new String(udpPacket.getData());

		// split the XML in parts
		String[] xmlParts = xml.split("~|~");
		try {
			// parse the halves
			p.destType = Integer.parseInt(xmlParts[0]);
			p.serverIp = xmlParts[1];
			p.dataType = Integer.parseInt(xmlParts[2]);
			p.data = xmlParts[3];
		} catch (Exception e) {
			p.init(); // set invalid packet
		}
		
		return p;
	}
}
