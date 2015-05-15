import java.net.DatagramPacket;

/*
 * Ip del Servidor (string)
 * string variable
 * 
 * ~|~
 * 
 * Tipo de Mensaje (int)
 * msg(1), 
 * broadcast(2), 
 * login(3), 
 * shareReq(4), 
 * shareRsp(5), 
 * ack(6), 
 * exit(7), 
 * error(8), 
 * handshake(9),
 * signIn(10)
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

	public String serverIp;
	public int dataType;
	public String data;
	
	public DataPacket() {
		init();
	}
	
	public void init() {
		serverIp = "";
		dataType = -1;
		data = "";
	}
	
	@Override
	public String toString() {
		return 
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
			p.serverIp = xmlParts[0];
			p.dataType = Integer.parseInt(xmlParts[1]);
			p.data = xmlParts[2];
		} catch (Exception e) {
			p.init(); // set invalid packet
		}
		
		return p;
	}
}
